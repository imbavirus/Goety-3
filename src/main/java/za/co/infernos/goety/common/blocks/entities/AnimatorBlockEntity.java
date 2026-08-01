package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.blocks.entities.IWaystoneBlock;
import za.co.infernos.goety.api.magic.GolemType;
import za.co.infernos.goety.common.blocks.AnimatorBlock;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.undead.HauntedArmorServant;
import za.co.infernos.goety.common.entities.deco.HauntedArmorStand;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.WaystoneItem;
import za.co.infernos.goety.common.magic.construct.SpawnFromBlock;
import za.co.infernos.goety.common.research.ResearchList;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.ItemHelper;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AnimatorBlockEntity extends BlockEntity implements IWaystoneBlock, Clearable {
    private ItemStack item = ItemStack.EMPTY;
    private CursedCageBlockEntity cursedCageTile;
    private int spinning;
    public boolean showBlock;

    public AnimatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ANIMATOR.get(), blockPos, blockState);
    }

    public int getSoulCost(){
        if (this.getPosition() != null){
            double distance = this.getBlockPos().distToCenterSqr(this.getPosition().pos().getCenter());
            return (int) (za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.AnimatorCost, 0) * distance);
        }
        return 0;
    }

    public AABB getRenderBoundingBox() {
        return AABB.INFINITE;
    }

    public void summonGolem(){
        if (this.getLevel() != null) {
            if (this.getPosition() != null) {
                if (this.getLevel().dimension() == this.getPosition().dimension()) {
                    if (this.getLevel().isLoaded(this.getPosition().pos())) {
                        if (this.checkCage() && this.cursedCageTile.getSouls() >= this.getSoulCost()) {
                            ItemStack itemStack = ModItems.ANIMATION_CORE.get().getDefaultInstance();
                            BlockState blockState = this.getLevel().getBlockState(this.getPosition().pos());
                            AABB aabb = new AABB(this.getPosition().pos());
                            List<HauntedArmorStand> list = this.getLevel().getEntitiesOfClass(HauntedArmorStand.class, aabb, ItemHelper::isFullEquipped);
                            Optional<HauntedArmorStand> optional = !list.isEmpty() ? list.stream().findFirst() : Optional.empty();
                            if (optional.isPresent() && SEHelper.hasResearch(this.getOwner(), ResearchList.HAUNTING)){
                                HauntedArmorStand hauntedArmorStand = optional.get();
                                HauntedArmorServant hauntedArmorServant = new HauntedArmorServant(ModEntityType.HAUNTED_ARMOR_SERVANT.get(), this.getLevel());
                                for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                                    hauntedArmorServant.setItemSlot(equipmentSlot, hauntedArmorStand.getItemBySlot(equipmentSlot));
                                    hauntedArmorServant.setGuaranteedDrop(equipmentSlot);
                                }
                                hauntedArmorServant.setPersistenceRequired();
                                hauntedArmorServant.setTrueOwner(this.getOwner());
                                hauntedArmorServant.moveTo(hauntedArmorStand.blockPosition(), hauntedArmorStand.getYRot(), hauntedArmorStand.getXRot());
                                hauntedArmorServant.setLeftHanded(this.getOwner().getMainArm() == HumanoidArm.LEFT);
                                if (this.getLevel().addFreshEntity(hauntedArmorServant)) {
                                    hauntedArmorStand.playSound(ModSounds.SUMMON_SPELL.get());
                                    hauntedArmorStand.showBreakingParticles();
                                    hauntedArmorStand.discard();
                                }
                            } else if (GolemType.getGolemList().containsKey(blockState)) {
                                if (GolemType.getGolemList().get(blockState).spawnServant(this.getOwner(), itemStack, this.getLevel(), this.getPosition().pos())) {
                                    this.getLevel().playSound(null, this.getBlockPos(), ModSounds.SUMMON_SPELL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                    this.getLevel().playSound(null, this.getPosition().pos(), ModSounds.SUMMON_SPELL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                    this.cursedCageTile.decreaseSouls(this.getSoulCost());
                                    this.generateManyParticles();
                                }
                            } else {
                                if (SpawnFromBlock.spawnServant(this.getOwner(), itemStack, this.getLevel(), this.getPosition().pos())) {
                                    this.getLevel().playSound(null, this.getBlockPos(), ModSounds.SUMMON_SPELL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                    this.getLevel().playSound(null, this.getPosition().pos(), ModSounds.SUMMON_SPELL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                    this.cursedCageTile.decreaseSouls(this.getSoulCost());
                                    this.generateManyParticles();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack stack) {
        this.item = stack;
        this.markUpdated();
    }

    public Player getOwner(){
        if (this.getLevel() != null) {
            if (!this.getItem().isEmpty()) {
                if (this.getItem().getItem() instanceof WaystoneItem && this.getItem().has(DataComponents.CUSTOM_DATA)) {
                    if (this.getItem().get(DataComponents.CUSTOM_DATA).contains(WaystoneItem.TAG_OWNER)) {
                        UUID owner = this.getItem().get(DataComponents.CUSTOM_DATA).copyTag().getUUID(WaystoneItem.TAG_OWNER);
                        return this.getLevel().getPlayerByUUID(owner);
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public GlobalPos getPosition(){
        if (!this.getItem().isEmpty()) {
            if (this.getItem().has(DataComponents.CUSTOM_DATA)) {
                return WaystoneItem.getPosition(this.getItem().get(DataComponents.CUSTOM_DATA).copyTag());
            }
        }
        return null;
    }

    public void setPosition(BlockPos blockPos){
        if (!this.getItem().isEmpty() && this.getLevel() != null) {
            ItemStack stack = this.getItem();
            CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
            tag.putLong("targetPos", blockPos.asLong());
            stack.set(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
            this.setItem(stack);
        }
    }

    public int getSpinning(){
        return this.spinning;
    }

    public void tick() {
        if (this.spinning > 0){
            --this.spinning;
        }
        if (this.getLevel() != null) {
            this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(AnimatorBlock.POWERED, this.checkCage() && this.getPosition() != null), 3);
        }
    }

    public void generateManyParticles(){
        BlockPos blockpos = this.getBlockPos();
        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide) {
                ServerLevel serverWorld = (ServerLevel) this.getLevel();
                for(int k = 0; k < 20; ++k) {
                    double d9 = (double)blockpos.getX() + 0.5D + (this.getLevel().random.nextDouble() - 0.5D) * 2.0D;
                    double d13 = (double)blockpos.getY() + 0.5D + (this.getLevel().random.nextDouble() - 0.5D) * 2.0D;
                    double d19 = (double)blockpos.getZ() + 0.5D + (this.getLevel().random.nextDouble() - 0.5D) * 2.0D;
                    serverWorld.sendParticles(ParticleTypes.SMOKE, d9, d13, d19, 1, 0.0D, 0.0D, 0.0D, 0);
                    serverWorld.sendParticles(ParticleTypes.FLAME, d9, d13, d19, 1, 0.0D, 0.0D, 0.0D, 0);
                }
            }
        }

    }

    private boolean checkCage() {
        if (this.getLevel() != null) {
            BlockPos pos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
            BlockState blockState = this.getLevel().getBlockState(pos);
            if (blockState.is(ModBlocks.CURSED_CAGE_BLOCK.get())) {
                BlockEntity tileentity = this.getLevel().getBlockEntity(pos);
                if (tileentity instanceof CursedCageBlockEntity cageBlock) {
                    this.cursedCageTile = cageBlock;
                    return !cursedCageTile.getItem().isEmpty();
                }
            }
        }
        return false;
    }

    public boolean isShowBlock(){
        return this.showBlock;
    }

    public void setShowBlock(boolean showBlock){
        this.showBlock = showBlock;
        this.markUpdated();
    }

    public void markNetworkDirty() {

    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.writeNetwork(super.getUpdateTag(pRegistries), pRegistries);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (this.getLevel() != null) {
            this.readNetwork(pkt.getTag(), lookupProvider);
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(tag, pRegistries);
        this.readNetwork(tag, pRegistries);
    }

    public void readNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (tag.contains("item")) {
            this.item = ItemStack.parse(pRegistries, tag.getCompound("item")).orElse(ItemStack.EMPTY);
        }
        if (tag.contains("showBlock")) {
            this.showBlock = tag.getBoolean("showBlock");
        }
    }

    public CompoundTag writeNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (!this.item.isEmpty()) {
            tag.put("item", item.save(pRegistries, new CompoundTag()));
        }
        tag.putBoolean("showBlock", this.showBlock);
        return tag;
    }

    public void clearContent() {
        this.setItem(ItemStack.EMPTY);
    }

    public void markUpdated() {
        this.setChanged();
        if (this.getLevel() != null) {
            this.getLevel().sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider pRegistries) {
        this.readNetwork(compound, pRegistries);
        super.loadAdditional(compound, pRegistries);
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider pRegistries) {
        this.writeNetwork(compound, pRegistries);
        super.saveAdditional(compound, pRegistries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
