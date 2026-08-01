package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.particles.AbsorbTrailParticleOption;
import za.co.infernos.goety.client.particles.GatherTrailParticle;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.blocks.DarkAltarBlock;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.crafting.ModRecipeSerializer;
import za.co.infernos.goety.common.crafting.RitualRecipe;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SPlayWorldSoundPacket;
import za.co.infernos.goety.common.research.ResearchList;
import za.co.infernos.goety.common.ritual.EnchantItemRitual;
import za.co.infernos.goety.common.ritual.Ritual;
import za.co.infernos.goety.common.ritual.RitualRequirements;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.ColorUtil;
import za.co.infernos.goety.utils.EntityFinder;
import za.co.infernos.goety.utils.SEHelper;
import za.co.infernos.goety.utils.ServerParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DarkAltarBlockEntity extends PedestalBlockEntity implements GameEventListener {
    public long lastChangeTime;
    public ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
                @Override
                public int getSlotLimit(int slot) {
                    return 64;
                }

                @Override
                protected void onContentsChanged(int slot) {
                    if (DarkAltarBlockEntity.this.getLevel() != null) {
                        if (!DarkAltarBlockEntity.this.getLevel().isClientSide) {
                            DarkAltarBlockEntity.this.lastChangeTime = DarkAltarBlockEntity.this.getLevel()
                                    .getGameTime();
                            boolean flag = !this.stacks.get(0).isEmpty();
                            DarkAltarBlockEntity.this.getLevel().setBlockAndUpdate(DarkAltarBlockEntity.this.getBlockPos(),
                                    DarkAltarBlockEntity.this.getBlockState().setValue(DarkAltarBlock.OCCUPIED, flag));
                            DarkAltarBlockEntity.this.markNetworkDirty();
                        }
                    }
                }
    };
    private CursedCageBlockEntity cursedCageTile;
    private final BlockPositionSource blockPosSource = new BlockPositionSource(this.worldPosition);
    public RitualRecipe currentRitualRecipe;
    public ResourceLocation currentRitualRecipeId;
    public UUID castingPlayerId;
    public Player castingPlayer;
    public List<Ingredient> remainingAdditionalIngredients = new ArrayList<>();
    public List<ItemStack> consumedIngredients = new ArrayList<>();
    public int experienceTaken;
    public boolean sacrificeProvided;
    public Mob getConvertEntity;
    public int currentTime;
    public int structureTime;
    public int convertTime;
    public boolean showArea;

    public DarkAltarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.DARK_ALTAR.get(), blockPos, blockState);
    }

    public RitualRecipe getCurrentRitualRecipe(){
        if(this.currentRitualRecipeId != null){
            if(this.getLevel() != null) {
                Optional<RecipeHolder<?>> recipe = this.getLevel().getRecipeManager().byKey(this.currentRitualRecipeId);
                recipe.map(RecipeHolder::value).map(r -> (RitualRecipe) r).ifPresent(r -> this.currentRitualRecipe = r);
                this.currentRitualRecipeId = null;
            }
        }
        return this.currentRitualRecipe;
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        this.readNetwork(compound, provider);
        super.loadAdditional(compound, provider);

        this.consumedIngredients.clear();
        if (this.currentRitualRecipeId != null || this.getCurrentRitualRecipe() != null) {
            if (compound.contains("consumedIngredients")) {
                ListTag list = compound.getList("consumedIngredients", Tag.TAG_COMPOUND);
                for (int i = 0; i < list.size(); i++) {
                    ItemStack stack = ItemStack.parseOptional(provider, list.getCompound(i));
                    this.consumedIngredients.add(stack);
                }
            }
            this.restoreRemainingAdditionalIngredients();
        }
        if (compound.contains("sacrificeProvided")) {
            this.sacrificeProvided = compound.getBoolean("sacrificeProvided");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        this.writeNetwork(compound, provider);
        if (this.getCurrentRitualRecipe() != null) {
            if (this.consumedIngredients.size() > 0) {
                ListTag list = new ListTag();
                for (ItemStack stack : this.consumedIngredients) {
                    if (!stack.isEmpty()) {
                        list.add(stack.save(provider, new CompoundTag()));
                    }
                }
                compound.put("consumedIngredients", list);
            }
            compound.putBoolean("sacrificeProvided", this.sacrificeProvided);
        }
        super.saveAdditional(compound, provider);
    }

    public void readNetwork(CompoundTag compound, HolderLookup.Provider pRegistries) {
        this.itemStackHandler.deserializeNBT(pRegistries, compound.getCompound("inventory"));
        this.lastChangeTime = compound.getLong("lastChangeTime");
        if (compound.contains("currentRitual")) {
            this.currentRitualRecipeId = ResourceLocation.parse(compound.getString("currentRitual"));
        }

        if (compound.contains("castingPlayerId")) {
            this.castingPlayerId = compound.getUUID("castingPlayerId");
        }

        this.currentTime = compound.getInt("currentTime");
        this.structureTime = compound.getInt("structureTime");
        this.convertTime = compound.getInt("convertTime");
        if (compound.contains("experienceTaken")) {
            this.experienceTaken = compound.getInt("experienceTaken");
        }
        if (compound.contains("showArea")) {
            this.showArea = compound.getBoolean("showArea");
        }
    }

    public CompoundTag writeNetwork(CompoundTag compound, HolderLookup.Provider pRegistries) {
        compound.put("inventory", this.itemStackHandler.serializeNBT(pRegistries));
        compound.putLong("lastChangeTime", this.lastChangeTime);
        RitualRecipe recipe = this.getCurrentRitualRecipe();
        if (recipe != null) {
            // 1.21+: recipe IDs live on RecipeHolder; we only persist the resolved id when available.
            if (this.currentRitualRecipeId != null) {
                compound.putString("currentRitual", this.currentRitualRecipeId.toString());
            }
        }
        if (this.castingPlayerId != null) {
            compound.putUUID("castingPlayerId", this.castingPlayerId);
        }
        compound.putInt("currentTime", this.currentTime);
        compound.putInt("structureTime", this.structureTime);
        compound.putInt("convertTime", this.convertTime);
        compound.putInt("experienceTaken", this.experienceTaken);
        compound.putBoolean("showArea", this.showArea);
        return compound;
    }

    public boolean isShowArea(){
        return this.showArea;
    }

    public void setShowArea(boolean showArea){
        this.showArea = showArea;
        this.setChanged();
        this.markNetworkDirty();
    }

    public void tick() {
        boolean flag = this.checkCage();
        if (this.getLevel() == null){
            return;
        }
        if (flag) {
            if (this.cursedCageTile.getSouls() > 0){
                RitualRecipe recipe = this.getCurrentRitualRecipe();
                double d0 = (double)this.worldPosition.getX() + this.getLevel().random.nextDouble();
                double d1 = (double)this.worldPosition.getY() + this.getLevel().random.nextDouble();
                double d2 = (double)this.worldPosition.getZ() + this.getLevel().random.nextDouble();
                if (!this.getLevel().isClientSide) {
                    ServerLevel serverWorld = (ServerLevel) this.getLevel();
                    if (recipe != null) {

                        this.restoreCastingPlayer();

                        if (this.castingPlayer == null || !this.sacrificeFulfilled()) {
                            for (int p = 0; p < 4; ++p) {
                                serverWorld.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0F, 0.0F, 0.0F, 0.0F);
                            }
                        }

                        ServerParticleUtil.addAuraParticles(serverWorld, ModParticleTypes.TOTEM_EFFECT.get(), Vec3.atBottomCenterOf(this.worldPosition).add(0, 0.25, 0), 0.75F);

                        if (this.remainingAdditionalIngredients == null) {
                            this.restoreRemainingAdditionalIngredients();
                            if (this.remainingAdditionalIngredients == null) {
                                Goety.LOGGER
                                        .warn("Could not restore remainingAdditionalIngredients during tick - world seems to be null. Will attempt again next tick.");
                                return;
                            }
                        }

                        IItemHandler handler = this.itemStackHandler;
                        if (!recipe.getRitual().isValid(this.getLevel(), this.worldPosition, this, this.castingPlayer,
                                handler.getStackInSlot(0), this.remainingAdditionalIngredients)) {
                            this.stopRitual(false);
                            return;
                        }

                        if (this.castingPlayer == null || !this.sacrificeFulfilled()) {
                            return;
                        }

                        if (this.getLevel().getGameTime() % 20 == 0) {
                            if (this.cursedCageTile.getSouls() >= recipe.getSoulCost()){
                                this.cursedCageTile.decreaseSouls(recipe.getSoulCost());
                                serverWorld.playSound(null, this.worldPosition, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 1.0F, this.getLevel().random.nextFloat() * 0.1F + 0.9F);
                                serverWorld.sendParticles(ParticleTypes.SCULK_SOUL, (double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 1.15D, (double)this.worldPosition.getZ() + 0.5D, 2, 0.2D, 0.0D, 0.2D, 0.0D);
                                this.currentTime++;
                            } else {
                                this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.noSouls.fail"), true);
                                this.stopRitual(false);
                                return;
                            }
                        }

                        if (recipe.getRitual() instanceof EnchantItemRitual enchantItemRitual) {
                            if (this.experienceTaken < enchantItemRitual.getLevelCost(handler.getStackInSlot(0))) {
                                if (this.castingPlayer.experienceLevel > 1) {
                                    if (this.getLevel().getGameTime() % 10 == 0) {
                                        serverWorld.playSound(null, this.worldPosition, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 0.5F, 0.5F);
                                        this.castingPlayer.giveExperienceLevels(-1);
                                        this.experienceTaken += 1;
                                    }
                                    this.addXPParticles(serverWorld);
                                } else {
                                    this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.noXP.fail"), true);
                                    this.stopRitual(false);
                                    return;
                                }
                            }
                        }

                        recipe.getRitual().update(this.getLevel(), this.worldPosition, this, this.castingPlayer, handler.getStackInSlot(0),
                                this.currentTime, recipe.getDuration());

                        if (!recipe.getRitual()
                                .consumeAdditionalIngredients(this.getLevel(), this.worldPosition, this.castingPlayer, this.remainingAdditionalIngredients,
                                        this.currentTime, this.consumedIngredients)) {
                            this.stopRitual(false);
                            return;
                        }

                        if (recipe.getDuration() >= 0 && this.currentTime >= recipe.getDuration()) {
                            if (recipe.getRitual() instanceof EnchantItemRitual enchantItemRitual){
                                if (this.experienceTaken >= enchantItemRitual.getLevelCost(handler.getStackInSlot(0))){
                                    this.stopRitual(true);
                                }
                            } else if (!recipe.isConversion()) {
                                this.stopRitual(true);
                            } else {
                                if (this.getConvertEntity != null){
                                    this.stopRitual(true);
                                } else {
                                    this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.convert.fail"), true);
                                    this.stopRitual(false);
                                }
                            }
                        }

                        int totalTime = 60;

                        if (!RitualRequirements.getProperStructure(recipe.getCraftType(), this.castingPlayer, this, this.worldPosition, this.getLevel())){
                            ++this.structureTime;
                            if (this.structureTime >= totalTime) {
                                this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.structure.fail"), true);
                                this.stopRitual(false);
                            }
                        } else {
                            this.structureTime = 0;
                        }
                        if (recipe.isConversion()){
                            if (RitualRequirements.noConvertEntity(recipe.getEntityToConvert(), this.worldPosition, this.getLevel())){
                                ++this.convertTime;
                                if (this.getConvertEntity != null){
                                    this.getConvertEntity = null;
                                }
                                if (this.convertTime >= totalTime) {
                                    this.castingPlayer.displayClientMessage(Component.translatable("info.goety.ritual.convert.fail"), true);
                                    this.stopRitual(false);
                                }
                            } else {
                                this.getConvertEntity = RitualRequirements.getConvertEntity(recipe.getEntityToConvert(), this.worldPosition, this.getLevel());
                                this.convertTime = 0;
                            }
                        }
                    } else {
                        if (this.getLevel().getGameTime() % 20 == 0) {
                            ServerParticleUtil.addAuraParticles(serverWorld, ModParticleTypes.TOTEM_EFFECT.get(), Vec3.atBottomCenterOf(this.worldPosition).add(0, 0.25, 0), 0.75F);
                        }
                    }
                }
            } else {
                this.stopRitual(false);
            }
        } else {
            this.stopRitual(false);
        }
        this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(DarkAltarBlock.LIT, flag), 3);
    }

    private void addXPParticles(ServerLevel world) {
        ColorUtil colorUtil = new ColorUtil(0xd0e45a);
        Vec3 vec3 = new Vec3(this.castingPlayer.getX(), this.castingPlayer.getY() + (this.castingPlayer.getBbHeight() / 2.0F), this.castingPlayer.getZ());
        Vec3 vector3d1 = Vec3.atCenterOf(this.getBlockPos());
        world.sendParticles(new GatherTrailParticle.Option(colorUtil, vector3d1), vec3.x, vec3.y, vec3.z, 0, 0.0F, 0.0F, 0.0F, 0.5F);
        for (int i = 0; i < 8; ++i) {
            Vec3 vector3d = new Vec3(this.castingPlayer.getRandomX(1.0F), this.castingPlayer.getRandomY(), this.castingPlayer.getRandomZ(1.0F));
            world.sendParticles(new AbsorbTrailParticleOption(vector3d1, 0xd0e45a, 10), vector3d.x, vector3d.y, vector3d.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    public void restoreCastingPlayer() {
        if (this.castingPlayer == null && this.castingPlayerId != null) {
            if (this.getLevel() != null) {
                if (this.getLevel().getGameTime() % (20 * 30) == 0) {
                    this.castingPlayer = EntityFinder.getPlayerByUuiDGlobal(this.castingPlayerId).orElse(null);
                    this.setChanged();
                    this.markNetworkDirty();
                }
            }
        }
    }

    public boolean activate(Level world, BlockPos pos, Player player, InteractionHand hand, Direction face) {
        if (!world.isClientSide) {
            if (this.checkCage()){
                ItemStack activationItem = player.getItemInHand(hand);
                if (activationItem == ItemStack.EMPTY){
                    this.removeItem();
                }

                if (this.getCurrentRitualRecipe() == null) {

                    RitualRecipe ritualRecipe = world.getRecipeManager()
                            .getAllRecipesFor(ModRecipeSerializer.RITUAL_TYPE.get())
                            .stream()
                            .map(net.minecraft.world.item.crafting.RecipeHolder::value)
                            .filter(r -> r.matches(world, pos, player, activationItem))
                            .findFirst()
                            .orElse(null);

                    if (ritualRecipe != null) {
                        if (ritualRecipe.getRitual() instanceof EnchantItemRitual && !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.RitualEnchants, true)){
                            player.displayClientMessage(Component.translatable("info.goety.ritual.disable.fail"), true);
                            return false;
                        } else if (ritualRecipe.getRitual().isValid(world, pos, this, player, activationItem, ritualRecipe.getIngredients())) {
                            if (!RitualRequirements.getProperStructure(ritualRecipe.getCraftType(), player, this, pos, world)){
//                                player.displayClientMessage(Component.translatable("info.goety.ritual.structure.fail"), true);
                                return false;
                            } else if (ritualRecipe.getResearch().contains(ResearchList.FORBIDDEN.getId())){
                                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichScrollRequirement, true)) {
                                    if (SEHelper.hasResearch(player, ResearchList.FORBIDDEN)) {
                                        this.startRitual(player, activationItem, ritualRecipe);
                                    } else {
                                        player.displayClientMessage(Component.translatable("info.goety.ritual.fail"), true);
                                        return false;
                                    }
                                } else {
                                    this.startRitual(player, activationItem, ritualRecipe);
                                }
                            } else if (ritualRecipe.isConversion() && RitualRequirements.noConvertEntity(ritualRecipe.getEntityToConvert(), pos, world)){
                                player.displayClientMessage(Component.translatable("info.goety.ritual.convert.fail"), true);
                                return false;
                            } else if ((ritualRecipe.isSummoning() && !RitualRequirements.canSummon(world, player, ritualRecipe.getEntityToSummon())) || (ritualRecipe.isConversion()  && !RitualRequirements.canSummon(world, player, ritualRecipe.getEntityToConvertInto()))){
                                player.displayClientMessage(Component.translatable("info.goety.summon.limit"), true);
                                return false;
                            } else if (ritualRecipe.getResearch() != null
                                    && ResearchList.getResearch(ritualRecipe.getResearch()) != null){
                                if (SEHelper.hasResearch(player, ResearchList.getResearch(ritualRecipe.getResearch()))){
                                    this.startRitual(player, activationItem, ritualRecipe);
                                } else {
                                    player.displayClientMessage(Component.translatable("info.goety.ritual.fail"), true);
                                    return false;
                                }
                            } else if (world.dimension() == Level.NETHER && ritualRecipe.getEntityToSummon() == ModEntityType.SUMMON_APOSTLE.get()){
                                if (SEHelper.apostleWarned(player)){
                                    SEHelper.setApostleWarned(player, false);
                                    this.startRitual(player, activationItem, ritualRecipe);
                                } else {
                                    SEHelper.setApostleWarned(player, true);
                                    player.displayClientMessage(Component.translatable("info.goety.ritual.apostleWarn"), true);
                                    return false;
                                }
                            } else {
                                this.startRitual(player, activationItem, ritualRecipe);
                            }
                        } else {
                            if (ritualRecipe.getRitual() instanceof EnchantItemRitual enchantItemRitual){
                                if (!enchantItemRitual.compatibleEnchant(activationItem)){
                                    player.displayClientMessage(Component.translatable("info.goety.ritual.enchantCompat.fail"), true);
                                } else if (player.experienceLevel < ritualRecipe.getXPLevelCost()){
                                    player.displayClientMessage(Component.translatable("info.goety.ritual.noXP.fail"), true);
                                } else {
                                    player.displayClientMessage(Component.translatable("info.goety.ritual.enchantCompatItem.fail"), true);
                                }
                            } else if ((ritualRecipe.isSummoning() && !RitualRequirements.canSummon(world, player, ritualRecipe.getEntityToSummon())) || (ritualRecipe.isConversion()  && !RitualRequirements.canSummon(world, player, ritualRecipe.getEntityToConvertInto()))){
                                player.displayClientMessage(Component.translatable("info.goety.summon.limit"), true);
                            } else {
                                player.displayClientMessage(Component.translatable("info.goety.ritual.itemProblem.fail"), true);
                            }
                            return false;
                        }
                    } else {
                        player.displayClientMessage(Component.translatable("info.goety.ritual.itemProblem.fail"), true);
                        return false;
                    }
                } else {
                    this.stopRitual(false);
                }
            } else {
                this.removeItem();
            }
        }
        return true;
    }

    public void removeItem() {
        this.removeItem(null);
    }

    public void removeItem(@Nullable LivingEntity livingEntity){
        IItemHandler handler = this.itemStackHandler;
        ItemStack itemStack = handler.getStackInSlot(0);
        if (itemStack != ItemStack.EMPTY){
            boolean flag = false;
            ItemStack itemStack1 = handler.extractItem(0, itemStack.getCount(), false);
            if (livingEntity != null) {
                if (livingEntity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    livingEntity.setItemInHand(InteractionHand.MAIN_HAND, itemStack1);
                } else if (livingEntity instanceof Player player){
                    if (!player.addItem(itemStack1)) {
                        flag = true;
                    }
                } else {
                    flag = true;
                }
                if (!flag) {
                    livingEntity.level().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.ITEM_PICKUP, livingEntity.getSoundSource(), 1.0F, 1.0F);
                }
            } else {
                flag = true;
            }
            if (flag) {
                Containers.dropItemStack(this.getLevel(), this.worldPosition.getX(), this.worldPosition.getY() + 1.0F, this.worldPosition.getZ(),
                        itemStack1);
            }
        }
        this.currentRitualRecipe = null;
        this.castingPlayerId = null;
        this.castingPlayer = null;
        this.currentTime = 0;
        this.sacrificeProvided = false;
        if (this.remainingAdditionalIngredients != null)
            this.remainingAdditionalIngredients.clear();
        this.consumedIngredients.clear();
        this.structureTime = 0;
        this.setChanged();
        this.markNetworkDirty();
    }

    public void startRitual(Player player, ItemStack activationItem, RitualRecipe ritualRecipe) {
        if (!this.getLevel().isClientSide) {
            this.currentRitualRecipe = ritualRecipe;
            this.castingPlayerId = player.getUUID();
            this.castingPlayer = player;
            this.currentTime = 0;
            this.sacrificeProvided = false;
            this.consumedIngredients.clear();
            this.remainingAdditionalIngredients = new ArrayList<>(this.currentRitualRecipe.getIngredients());
            IItemHandler handler = this.itemStackHandler;
            handler.insertItem(0, activationItem.split(1), false);
            this.currentRitualRecipe.getRitual().start(this.getLevel(), this.worldPosition, this, player, handler.getStackInSlot(0));
            this.structureTime = 40;
            this.setChanged();
            this.markNetworkDirty();
        }
    }

    public void stopRitual(boolean finished) {
        if (!this.getLevel().isClientSide) {
            RitualRecipe recipe = this.getCurrentRitualRecipe();
            if (recipe != null && this.castingPlayer != null) {
                IItemHandler handler = this.itemStackHandler;
                if (finished) {
                    ItemStack activationItem = handler.getStackInSlot(0);
                    recipe.getRitual().finish(this.getLevel(), this.worldPosition, this, this.castingPlayer, activationItem);
                    if (recipe.getEntityToSummon() == ModEntityType.SUMMON_APOSTLE.get()){
                        if (this.getLevel() instanceof ServerLevel serverLevel) {
                            ModNetwork.sendToALL(new SPlayWorldSoundPacket(this.worldPosition, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.value(), 1.0F, 1.0F));
                            Warden.applyDarknessAround(serverLevel, Vec3.atCenterOf(this.worldPosition), (Entity)null, 32);
                        }
                        for (int i = -8; i <= 8; ++i) {
                            for (int j = -8; j <= 8; ++j) {
                                for (int k = -8; k <= 8; ++k) {
                                    BlockPos blockpos1 = this.worldPosition.offset(i, j, k);
                                    BlockState blockstate = this.getLevel().getBlockState(blockpos1);
                                    if (blockstate.getBlock() instanceof SoulFireBlock){
                                        this.getLevel().destroyBlock(blockpos1, false);
                                        this.getLevel().levelEvent((Player)null, 1009, blockpos1, 0);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    recipe.getRitual().interrupt(this.getLevel(), this.worldPosition, this, this.castingPlayer,
                            handler.getStackInSlot(0));
                    Containers.dropItemStack(this.getLevel(), this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(),
                            handler.extractItem(0, 1, false));
                }
            }
            this.currentRitualRecipe = null;
            this.castingPlayerId = null;
            this.castingPlayer = null;
            this.currentTime = 0;
            this.sacrificeProvided = false;
            if (this.remainingAdditionalIngredients != null) {
                this.remainingAdditionalIngredients.clear();
            }
            this.consumedIngredients.clear();
            this.structureTime = 0;
            this.experienceTaken = 0;
            this.setChanged();
            this.markNetworkDirty();
        }
    }

    public boolean sacrificeFulfilled() {
        return !this.getCurrentRitualRecipe().requiresSacrifice() || this.sacrificeProvided;
    }

    public void notifySacrifice(LivingEntity entityLivingBase) {
        this.sacrificeProvided = true;
    }

    public PositionSource getListenerSource() {
        return this.blockPosSource;
    }

    public int getListenerRadius() {
        return Ritual.SACRIFICE_DETECTION_RANGE;
    }

    public GameEventListener.DeliveryMode getDeliveryMode() {
        return GameEventListener.DeliveryMode.BY_DISTANCE;
    }

    public boolean handleGameEvent(ServerLevel serverLevel, Holder<GameEvent> p_282184_, GameEvent.Context p_283014_, Vec3 p_282350_) {
        if (!this.isRemoved()) {
            if (p_282184_.is(GameEvent.ENTITY_DIE)) {
                Entity sourceEntity = p_283014_.sourceEntity();
                if (sourceEntity instanceof LivingEntity livingEntity) {
                    if (this.getCurrentRitualRecipe() != null && this.getCurrentRitualRecipe().getRitual().isValidSacrifice(livingEntity)) {
                        this.notifySacrifice(livingEntity);
                    }
                    return true;
                }
            }

        }
        return false;
    }

    protected void restoreRemainingAdditionalIngredients() {
        if (this.getLevel() == null) {
            this.remainingAdditionalIngredients = null;
        } else {
            if (this.consumedIngredients.size() > 0) {
                this.remainingAdditionalIngredients = Ritual.getRemainingAdditionalIngredients(
                        this.getCurrentRitualRecipe().getIngredients(), this.consumedIngredients);
            } else {
                this.remainingAdditionalIngredients = new ArrayList<>(this.getCurrentRitualRecipe().getIngredients());
            }
        }

    }

    private boolean checkCage() {
        if (this.getLevel() == null){
            return false;
        }
        BlockPos pos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
        BlockState blockState = this.getLevel().getBlockState(pos);
        if (blockState.is(ModBlocks.CURSED_CAGE_BLOCK.get())){
            BlockEntity tileentity = this.getLevel().getBlockEntity(pos);
            if (tileentity instanceof CursedCageBlockEntity){
                this.cursedCageTile = (CursedCageBlockEntity) tileentity;
                return !cursedCageTile.getItem().isEmpty() && cursedCageTile.getSouls() > 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}

/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
