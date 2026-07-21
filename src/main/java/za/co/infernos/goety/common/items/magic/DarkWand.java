package za.co.infernos.goety.common.items.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.api.entities.ally.IServant;
import za.co.infernos.goety.api.items.magic.IFocus;
import za.co.infernos.goety.api.items.magic.IWand;
import za.co.infernos.goety.api.magic.IBlockSpell;
import za.co.infernos.goety.api.magic.IBreathingSpell;
import za.co.infernos.goety.api.magic.IChargingSpell;
import za.co.infernos.goety.api.magic.ISpell;
import za.co.infernos.goety.api.magic.ITouchSpell;
import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.blocks.BrewCauldronBlock;
import za.co.infernos.goety.common.blocks.entities.ArcaBlockEntity;
import za.co.infernos.goety.common.blocks.entities.BrewCauldronBlockEntity;
import za.co.infernos.goety.common.entities.neutral.AbstractVine;
import za.co.infernos.goety.common.events.spell.GoetyEventFactory;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SPlayEntitySoundPacket;
import za.co.infernos.goety.common.network.server.SPlayPlayerSoundPacket;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModKeybindings;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.ConfigHelper;
import za.co.infernos.goety.utils.ItemHelper;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.SEHelper;
import za.co.infernos.goety.utils.WandUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Learned item capabilities from codes made by @vemerion & @MrCrayfish
 *
 * Ported from the 1.20 reference. Per-stack state lives under the vanilla
 * {@code DataComponents.CUSTOM_DATA} component (see Phase 3 helpers below).
 */
public class DarkWand extends Item implements IWand {
    public SpellType spellType;

    public DarkWand(Properties properties, SpellType spellType) {
        super(properties);
        this.spellType = spellType;
    }

    public DarkWand(SpellType spellType) {
        this(wandProperties(), spellType);
    }

    public DarkWand() {
        this(SpellType.NONE);
    }

    @Override
    public SpellType getSpellType() {
        return this.spellType;
    }

    public static Item.Properties wandProperties() {
        return new Properties()
                .rarity(Rarity.RARE)
                .setNoRepair()
                .stacksTo(1);
    }

    // ------------------------------------------------------------------
    // Phase 3: per-stack int state via DataComponents.CUSTOM_DATA
    // ------------------------------------------------------------------

    private static int getInt(ItemStack stack, String key) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.isEmpty() ? 0 : data.copyTag().getInt(key);
    }

    private static boolean hasKey(ItemStack stack, String key) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return !data.isEmpty() && data.copyTag().contains(key);
    }

    private static void setInt(ItemStack stack, String key, int value) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(key, value));
    }

    private static boolean hasState(ItemStack stack) {
        return hasKey(stack, SOULUSE);
    }

    private static void initState(ItemStack stack, LivingEntity entity) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putInt(SOULUSE, 0);
            tag.putInt(SOULCOST, 0);
            tag.putInt(CASTTIME, 0);
            tag.putInt(COOL, 0);
            tag.putInt(SHOTS, 0);
            tag.putInt(SECONDS, 0);
        });
    }

    // ------------------------------------------------------------------
    // Phase 4: behavior — direct port of the 1.20 reference, with API fixes
    // ------------------------------------------------------------------

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity livingEntity) {
            if (!hasState(stack)) {
                initState(stack, livingEntity);
            } else if (!hasKey(stack, SHOTS)) {
                setInt(stack, SHOTS, 0);
            }
            ISpell spell = this.getSpell(stack);
            this.setSpellConditions(spell, stack, livingEntity);
            setInt(stack, SOULUSE, SoulUse(livingEntity, stack));
            setInt(stack, CASTTIME, CastDuration(stack));
            ItemStack focus = IWand.getFocus(stack);
            if (focus != null && !focus.isEmpty()) {
                focus.inventoryTick(worldIn, entityIn, itemSlot, isSelected);
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        initState(pStack, pPlayer);
        setInt(pStack, SOULUSE, SoulUse(pPlayer, pStack));
        this.setSpellConditions(null, pStack, pPlayer);
    }

    @Override
    public int SoulUse(LivingEntity entityLiving, ItemStack stack) {
        ItemStack focus = IWand.getFocus(stack);
        boolean enchanted = focus != null && focus.isEnchanted();
        float discount = SEHelper.soulDiscount(entityLiving);
        if (enchanted && ConfigHelper.getBoolean(SpellConfig.EnchantMultiCost, false)) {
            return (int) (SoulCost(stack) * 2 * discount);
        }
        return (int) (SoulCost(stack) * discount);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        boolean flag = false;
        ItemStack focus = IWand.getFocus(stack);
        if (focus == null || focus.isEmpty()) {
            return false;
        }
        if (entity instanceof LivingEntity target && target instanceof IOwned owned
                && (owned.getTrueOwner() == player
                    || (owned.getTrueOwner() instanceof IOwned owned1 && owned1.getTrueOwner() == player))) {
            if (!player.level().isClientSide) {
                if (focus.getItem() instanceof CallFocus && !CallFocus.hasSummon(focus)) {
                    CustomData.update(DataComponents.CUSTOM_DATA, focus, tag -> CallFocus.setSummon(tag, target));
                    player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                    flag = true;
                } else if (focus.getItem() instanceof TroopFocus && !TroopFocus.hasSummonType(focus)) {
                    CustomData.update(DataComponents.CUSTOM_DATA, focus, tag -> TroopFocus.setSummonType(tag, target.getType()));
                    player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                    flag = true;
                } else if (focus.getItem() instanceof CommandFocus && owned instanceof IServant servant
                        && servant.canBeCommanded() && !CommandFocus.hasServant(focus)) {
                    CustomData.update(DataComponents.CUSTOM_DATA, focus, tag -> CommandFocus.setServant(tag, target));
                    player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                    flag = true;
                } else if (focus.getItem() instanceof OrderFocus && owned instanceof IServant servant
                        && servant.canBeCommanded()) {
                    List<LivingEntity> list = OrderFocus.getServants(player.level(), focus);
                    if (list.isEmpty() || list.size() < 8) {
                        if (!list.contains(target)) {
                            OrderFocus.setServants(focus, player, target);
                            player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                            ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                            flag = true;
                        }
                    }
                }
                if (!flag) {
                    if (owned instanceof IServant summonedEntity) {
                        if (player.isShiftKeyDown() || player.isCrouching()) {
                            if (ConfigHelper.getInt(SpellConfig.OwnerHitKill, 0) == 0) {
                                summonedEntity.tryKill(player);
                            }
                        } else {
                            if (ConfigHelper.getBoolean(SpellConfig.OwnerHitCommand, false)) {
                                if (summonedEntity.canUpdateMove()) {
                                    summonedEntity.updateMoveMode(player);
                                }
                            }
                        }
                    } else if (owned instanceof AbstractVine vine) {
                        if (player.isShiftKeyDown() || player.isCrouching()) {
                            if (ConfigHelper.getInt(SpellConfig.OwnerHitKill, 0) == 0) {
                                vine.kill();
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        ItemStack focus = IWand.getFocus(stack);
        if (focus != null && !focus.isEmpty()) {
            if (focus.getItem() instanceof CommandFocus) {
                LivingEntity servant = CommandFocus.getServant(player.level(), focus);
                if (servant instanceof IServant summoned && servant != target) {
                    if (summoned.getTrueOwner() == player && target.distanceTo(player) <= 64) {
                        summoned.setCommandPosEntity(target);
                        player.playSound(ModSounds.COMMAND.get(), 1.0F, 0.45F);
                        if (!player.level().isClientSide) {
                            ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(ModSounds.COMMAND.get(), 1.0F, 0.45F));
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            } else if (focus.getItem() instanceof OrderFocus) {
                List<LivingEntity> servants = OrderFocus.getServants(player.level(), focus);
                if (!servants.isEmpty()) {
                    int i = 0;
                    for (LivingEntity living : servants) {
                        if (living instanceof IServant summoned && living != target) {
                            if (summoned.getTrueOwner() == player && target.distanceTo(player) <= 64) {
                                summoned.setCommandPosEntityOrder(target);
                                ++i;
                            }
                        }
                    }
                    if (i > 0) {
                        player.playSound(ModSounds.COMMAND.get(), 1.0F, 0.45F);
                        if (!player.level().isClientSide) {
                            ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(ModSounds.COMMAND.get(), 1.0F, 0.45F));
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        if (target instanceof IOwned owned) {
            if (owned.getTrueOwner() == player
                    || (owned.getTrueOwner() instanceof IOwned owned1 && owned1.getTrueOwner() == player)) {
                if (owned instanceof IServant summonedEntity) {
                    if (player.isShiftKeyDown() || player.isCrouching()) {
                        if (ConfigHelper.getInt(SpellConfig.OwnerHitKill, 0) == 1) {
                            summonedEntity.tryKill(player);
                            return InteractionResult.SUCCESS;
                        }
                    } else {
                        if (!ConfigHelper.getBoolean(SpellConfig.OwnerHitCommand, false)) {
                            if (summonedEntity.canUpdateMove()) {
                                summonedEntity.updateMoveMode(player);
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                } else if (owned instanceof AbstractVine vine) {
                    if (player.isShiftKeyDown() || player.isCrouching()) {
                        if (ConfigHelper.getInt(SpellConfig.OwnerHitKill, 0) == 1) {
                            vine.kill();
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        if (this.getSpell(stack) instanceof ITouchSpell touchSpell) {
            if (this.canCastTouch(stack, player.level(), player)) {
                if (player.level() instanceof ServerLevel serverLevel) {
                    touchSpell.touchResult(serverLevel, player, target, stack, WandUtil.getStats(player, touchSpell));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        InteractionHand hand = pContext.getHand();
        ItemStack stack = pContext.getItemInHand();
        if (player != null) {
            ItemStack focus = IWand.getFocus(stack);
            if (focus != null && !focus.isEmpty() && focus.getItem() instanceof RecallFocus) {
                if (!RecallFocus.hasRecall(focus)) {
                    BlockEntity tileEntity = level.getBlockEntity(blockpos);
                    if (tileEntity instanceof ArcaBlockEntity arcaTile) {
                        if (player == arcaTile.getPlayer() && arcaTile.getLevel() != null) {
                            CustomData.update(DataComponents.CUSTOM_DATA, focus, tag ->
                                    RecallFocus.addRecallTags(arcaTile.getLevel().dimension(), arcaTile.getBlockPos(), tag));
                            player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                            if (!level.isClientSide) {
                                ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                            }
                            return InteractionResult.sidedSuccess(level.isClientSide);
                        }
                    }
                    if (level.getBlockState(blockpos).is(ModTags.Blocks.RECALL_BLOCKS)) {
                        CustomData.update(DataComponents.CUSTOM_DATA, focus, tag ->
                                RecallFocus.addRecallTags(level.dimension(), blockpos, tag));
                        player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                        if (!level.isClientSide) {
                            ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            } else if (focus != null && !focus.isEmpty() && focus.getItem() instanceof CommandFocus) {
                if (CommandFocus.hasServant(focus)) {
                    LivingEntity servant = CommandFocus.getServant(level, focus);
                    if (servant instanceof IServant summoned) {
                        if (summoned.getTrueOwner() == player && servant.distanceTo(player) <= 64) {
                            BlockPos above = blockpos.above();
                            boolean ok = false;
                            if (summoned.canCommandToBlock(level, blockpos)) {
                                summoned.setCommandPos(blockpos);
                                ok = true;
                            } else if (summoned.canCommandToBlock(level, above)) {
                                summoned.setCommandPos(above);
                                ok = true;
                            }
                            if (ok) {
                                player.playSound(ModSounds.COMMAND.get(), 1.0F, 0.45F);
                                if (!level.isClientSide) {
                                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(ModSounds.COMMAND.get(), 1.0F, 0.45F));
                                }
                                return InteractionResult.sidedSuccess(level.isClientSide);
                            }
                        }
                    }
                }
            } else if (focus != null && !focus.isEmpty() && focus.getItem() instanceof OrderFocus) {
                List<LivingEntity> servants = OrderFocus.getServants(level, focus);
                if (!servants.isEmpty()) {
                    int i = 0;
                    for (LivingEntity living : servants) {
                        if (living instanceof IServant summoned && summoned.canBeCommanded()) {
                            if (summoned.getTrueOwner() == player && living.distanceTo(player) <= 64) {
                                BlockPos above = blockpos.above();
                                if (summoned.canCommandToBlock(level, blockpos)) {
                                    summoned.setCommandPos(blockpos);
                                    ++i;
                                } else if (summoned.canCommandToBlock(level, above)) {
                                    summoned.setCommandPos(above);
                                    ++i;
                                }
                            }
                        }
                    }
                    if (i > 0) {
                        player.playSound(ModSounds.COMMAND.get(), 1.0F, 0.45F);
                        if (!level.isClientSide) {
                            ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(ModSounds.COMMAND.get(), 1.0F, 0.45F));
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            } else if (this.getSpell(stack) instanceof IBlockSpell blockSpell0) {
                ISpell spell2 = GoetyEventFactory.onBlockBasedSpell(player.level(), blockpos, player.level().getBlockState(blockpos), blockSpell0, pContext.getClickedFace(), player);
                if (spell2 instanceof IBlockSpell blockSpell) {
                    if (player.level() instanceof ServerLevel serverLevel) {
                        if (blockSpell.rightBlock(serverLevel, player, blockpos, pContext.getClickedFace(), WandUtil.getStats(player, blockSpell))) {
                            if (this.canCastTouch(stack, level, player)) {
                                blockSpell.blockResult(serverLevel, player, stack, blockpos, pContext.getClickedFace(), WandUtil.getStats(player, blockSpell));
                            }
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            } else if (level.getBlockState(blockpos).is(BlockTags.BANNERS) && level.getBlockEntity(blockpos) instanceof BannerBlockEntity bannerBlock) {
                if (!level.isClientSide) {
                    // 1.21: banner patterns moved off NBT into BannerPatternLayers component.
                    // SEHelper.setBannerPattern still wants a legacy ListTag; banner copy remains
                    // a TODO until SEHelper is migrated. Base color still works.
                    if (bannerBlock.getBaseColor() != null) {
                        SEHelper.setBannerBaseColor(player, bannerBlock.getBaseColor());
                        player.displayClientMessage(Component.translatable("info.goety.banner.add", player.getDisplayName()), true);
                        level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), ModSounds.CAST_SPELL.get(), SoundSource.BLOCKS, 1.0F, 0.5F);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else if (level.getBlockState(blockpos).getBlock() instanceof BrewCauldronBlock) {
                if (!level.isClientSide) {
                    if (level.getBlockEntity(blockpos) instanceof BrewCauldronBlockEntity cauldronBlock) {
                        if (MobUtil.isShifting(player)) {
                            if (stack.getItem() instanceof IWand) {
                                cauldronBlock.fullReset();
                                level.playSound(null, blockpos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                                level.playSound(null, blockpos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }
            } else if (!level.getBlockState(blockpos).isAir()) {
                if (!level.isClientSide) {
                    BlockHitResult hit = new BlockHitResult(pContext.getClickLocation(), pContext.getClickedFace(), pContext.getClickedPos(), pContext.isInside());
                    ItemInteractionResult itemResult = level.getBlockState(blockpos).useItemOn(stack, level, player, hand, hit);
                    if (itemResult.consumesAction()) {
                        return itemResult.result();
                    }
                    return level.getBlockState(blockpos).useWithoutItem(level, player, hit);
                }
            }
        }
        return super.useOn(pContext);
    }

    @Override
    public void onUseTick(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        ISpell iSpell = GoetyEventFactory.onStartSpell(livingEntityIn, stack, this.getSpell(stack));
        if (worldIn instanceof ServerLevel && this.cannotCast(livingEntityIn, stack, iSpell)) {
            livingEntityIn.stopUsingItem();
            return;
        }
        int castTime = this.getUseDuration(stack, livingEntityIn) - count;
        if (livingEntityIn.getUseItem() == stack && iSpell != null && this.isNotInstant(iSpell, livingEntityIn, stack)) {
            SoundEvent soundevent = this.CastingSound(stack, livingEntityIn);
            if (castTime == 1 && soundevent != null) {
                if (worldIn instanceof ServerLevel serverLevel) {
                    iSpell.startSpell(serverLevel, livingEntityIn, stack, WandUtil.getStats(livingEntityIn, iSpell));
                }
                worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), soundevent, SoundSource.PLAYERS, this.castingVolume(stack), this.castingPitch(stack));
            }
            if (worldIn instanceof ServerLevel serverLevel) {
                iSpell = GoetyEventFactory.onCastingSpell(livingEntityIn, stack, iSpell, castTime);
                if (iSpell != null) {
                    iSpell.useSpell(serverLevel, livingEntityIn, stack, castTime, WandUtil.getStats(livingEntityIn, iSpell));
                } else {
                    livingEntityIn.stopUsingItem();
                }
            }
            if (iSpell != null) {
                if (iSpell instanceof IChargingSpell chargingSpell && chargingSpell.castUp(livingEntityIn, stack) > 0) {
                    this.useParticles(worldIn, livingEntityIn, stack, iSpell);
                } else if (!(iSpell instanceof IChargingSpell)) {
                    this.useParticles(worldIn, livingEntityIn, stack, iSpell);
                }
                if (iSpell instanceof IChargingSpell chargingSpell) {
                    if (hasState(stack)) {
                        if (castTime >= chargingSpell.castUp(livingEntityIn, stack) || chargingSpell.castUp(livingEntityIn, stack) <= 0) {
                            setInt(stack, COOL, getInt(stack, COOL) + 1);
                            if (getInt(stack, COOL) >= Cooldown(stack)) {
                                setInt(stack, COOL, 0);
                                if (chargingSpell.shotsNumber(livingEntityIn, stack) > 0) {
                                    this.increaseShots(stack);
                                }
                                this.MagicResults(stack, worldIn, livingEntityIn, chargingSpell);
                            }
                        }
                    }
                    if (livingEntityIn instanceof Player player) {
                        if (!SEHelper.getSoulsAmount(player, iSpell.soulCost(player, stack)) && !player.isCreative()) {
                            player.stopUsingItem();
                        }
                    }
                }
            } else {
                livingEntityIn.stopUsingItem();
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int useTimeRemaining) {
        if (level instanceof ServerLevel serverLevel) {
            int castTime = this.getUseDuration(stack, livingEntity) - useTimeRemaining;
            ISpell spell = GoetyEventFactory.onStopSpell(livingEntity, stack, this.getSpell(stack), castTime, useTimeRemaining);
            if (spell != null) {
                spell.stopSpell(serverLevel, livingEntity, stack, IWand.getFocus(stack), castTime, WandUtil.getStats(livingEntity, spell));
                if (livingEntity instanceof Player player) {
                    if (spell instanceof IChargingSpell chargeSpell) {
                        if (chargeSpell.shotsNumber(player, stack) > 0) {
                            if (this.ShotsFired(stack) > 0) {
                                float coolPercent = (float) this.ShotsFired(stack) / chargeSpell.shotsNumber(player, stack);
                                this.setShots(stack, 0);
                                if (!spell.hasCustomCooldown(player, stack, IWand.getFocus(stack), Mth.floor(chargeSpell.spellCooldown(player) * coolPercent))) {
                                    SEHelper.addCooldown(player, IWand.getFocus(stack).getItem(), Mth.floor(chargeSpell.spellCooldown(player) * coolPercent));
                                }
                            }
                        } else {
                            if (!spell.hasCustomCooldown(player, stack, IWand.getFocus(stack), Mth.floor(chargeSpell.spellCooldown(player)))) {
                                SEHelper.addCooldown(player, IWand.getFocus(stack).getItem(), Mth.floor(chargeSpell.spellCooldown(player)));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        if (hasState(stack)) {
            return getInt(stack, CASTTIME);
        }
        return this.CastDuration(stack);
    }

    @Override
    @Nonnull
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }

    @Override
    @Nonnull
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        ISpell iSpell = GoetyEventFactory.onCastSpell(entityLiving, this.getSpell(stack));
        if (iSpell != null) {
            if (!(iSpell instanceof IChargingSpell) || this.isNotInstant(iSpell, entityLiving, stack) || this.notTouch(iSpell)) {
                if (!this.cannotCast(entityLiving, stack)) {
                    this.MagicResults(stack, worldIn, entityLiving, iSpell);
                }
            }
        }
        if (hasState(stack)) {
            if (getInt(stack, COOL) > 0) {
                setInt(stack, COOL, 0);
            }
            if (getInt(stack, SHOTS) > 0) {
                setInt(stack, SHOTS, 0);
            }
        }
        return stack;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        ItemStack focus = IWand.getFocus(itemstack);
        if (focus != null && !focus.isEmpty()) {
            if (focus.getItem() instanceof CommandFocus && playerIn.isCrouching()) {
                if (CommandFocus.hasServant(focus)) {
                    CustomData.update(DataComponents.CUSTOM_DATA, focus, tag -> {
                        tag.remove(CommandFocus.TAG_ENTITY);
                        tag.remove(CommandFocus.TAG_ENTITY_CLIENT);
                    });
                    playerIn.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                    if (!worldIn.isClientSide) {
                        ModNetwork.sendTo(playerIn, new SPlayEntitySoundPacket(playerIn.getUUID(), SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                    }
                }
                return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
            } else if (focus.getItem() instanceof OrderFocus && playerIn.isCrouching()) {
                CustomData.update(DataComponents.CUSTOM_DATA, focus, tag -> {
                    tag.remove(OrderFocus.SERVANT_LIST);
                    tag.remove(OrderFocus.SERVANT_CLIENT_LIST);
                });
                playerIn.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                if (!worldIn.isClientSide) {
                    ModNetwork.sendTo(playerIn, new SPlayEntitySoundPacket(playerIn.getUUID(), SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                }
                return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
            } else if (focus.getItem() instanceof CallFocus && playerIn.isCrouching()) {
                if (CallFocus.hasSummon(focus)) {
                    CustomData.update(DataComponents.CUSTOM_DATA, focus, tag -> tag.remove(CallFocus.TAG_ENTITY));
                    playerIn.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                    if (!worldIn.isClientSide) {
                        ModNetwork.sendTo(playerIn, new SPlayEntitySoundPacket(playerIn.getUUID(), SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                    }
                }
                return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
            }
        }
        ISpell spell = this.getSpell(itemstack);
        if (spell != null) {
            if (this.cannotCast(playerIn, itemstack)) {
                return InteractionResultHolder.pass(itemstack);
            } else if (this.isNotInstant(spell, playerIn, itemstack)) {
                if (SEHelper.getSoulsAmount(playerIn, spell.soulCost(playerIn, itemstack)) || playerIn.getAbilities().instabuild) {
                    if (!worldIn.isClientSide) {
                        playerIn.startUsingItem(handIn);
                    }
                }
            } else if (this.notTouch(spell)) {
                playerIn.swing(handIn);
                ISpell iSpell = GoetyEventFactory.onCastSpell(playerIn, spell);
                this.MagicResults(itemstack, worldIn, playerIn, iSpell);
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }

    public void setSpellConditions(@Nullable ISpell spell, ItemStack stack, LivingEntity livingEntity) {
        if (!hasState(stack)) {
            return;
        }
        if (spell != null) {
            int currentShots = getInt(stack, SHOTS);
            int soulCost = spell.soulCost(livingEntity, stack);
            int duration = spell.castDuration(livingEntity, stack);
            int cooldown;
            if (spell instanceof IChargingSpell chargingSpell) {
                cooldown = chargingSpell.Cooldown(livingEntity, stack, currentShots);
            } else {
                cooldown = 0;
            }
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
                tag.putInt(SOULCOST, soulCost);
                tag.putInt(DURATION, duration);
                tag.putInt(COOLDOWN, cooldown);
            });
        } else {
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
                tag.putInt(SOULCOST, 0);
                tag.putInt(DURATION, 0);
                tag.putInt(COOLDOWN, 0);
            });
        }
    }

    public int SoulCost(ItemStack itemStack) {
        return hasState(itemStack) ? getInt(itemStack, SOULCOST) : 0;
    }

    public int CastDuration(ItemStack itemStack) {
        return hasState(itemStack) ? getInt(itemStack, DURATION) : 0;
    }

    public int Cooldown(ItemStack itemStack) {
        return hasState(itemStack) ? getInt(itemStack, COOLDOWN) : 0;
    }

    @Override
    public int ShotsFired(ItemStack itemStack) {
        return hasState(itemStack) ? getInt(itemStack, SHOTS) : 0;
    }

    public void increaseShots(ItemStack itemStack) {
        if (hasState(itemStack)) {
            setInt(itemStack, SHOTS, ShotsFired(itemStack) + 1);
        }
    }

    public void setShots(ItemStack itemStack, int amount) {
        if (hasState(itemStack)) {
            setInt(itemStack, SHOTS, amount);
        }
    }

    @Nullable
    public SoundEvent CastingSound(ItemStack stack, LivingEntity caster) {
        ISpell spell = this.getSpell(stack);
        return spell == null ? null : spell.CastingSound(caster);
    }

    public float castingVolume(ItemStack stack) {
        ISpell spell = this.getSpell(stack);
        return spell == null ? 0.5F : spell.castingVolume();
    }

    public float castingPitch(ItemStack stack) {
        ISpell spell = this.getSpell(stack);
        return spell == null ? 1.0F : spell.castingPitch();
    }

    public boolean canCastTouch(ItemStack stack, Level worldIn, LivingEntity caster) {
        if (!(caster instanceof Player playerEntity)) {
            return false;
        }
        if (worldIn.isClientSide) {
            return false;
        }
        ISpell spell = GoetyEventFactory.onTouchBasedSpell(caster, stack, this.getSpell(stack));
        if (spell == null || this.cannotCast(caster, stack, spell)) {
            return false;
        }
        if (playerEntity.isCreative()) {
            if (!spell.hasCustomCooldown(caster, stack, IWand.getFocus(stack), spell.spellCooldown(playerEntity))) {
                SEHelper.addCooldown(playerEntity, IWand.getFocus(stack).getItem(), spell.spellCooldown(playerEntity));
            }
            return hasState(stack);
        }
        if (SEHelper.getSoulsAmount(playerEntity, SoulUse(caster, stack))) {
            if (hasState(stack)) {
                SEHelper.decreaseSouls(playerEntity, SoulUse(caster, stack));
                if (!spell.hasCustomCooldown(caster, stack, IWand.getFocus(stack), spell.spellCooldown(playerEntity))) {
                    SEHelper.addCooldown(playerEntity, IWand.getFocus(stack).getItem(), spell.spellCooldown(playerEntity));
                }
                SEHelper.sendSEUpdatePacket(playerEntity);
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public void MagicResults(ItemStack stack, Level worldIn, LivingEntity caster) {
        this.MagicResults(stack, worldIn, caster, this.getSpell(stack));
    }

    public void MagicResults(ItemStack stack, Level worldIn, LivingEntity caster, ISpell spell) {
        if (spell != null && caster instanceof Player playerEntity) {
            if (!worldIn.isClientSide) {
                ServerLevel serverWorld = (ServerLevel) worldIn;
                if (playerEntity.isCreative()) {
                    if (hasState(stack)) {
                        spell.SpellResult(serverWorld, caster, stack, WandUtil.getStats(caster, spell));
                        boolean flag;
                        if (spell instanceof IChargingSpell chargingSpell) {
                            flag = chargingSpell.shotsNumber(playerEntity, stack) > 0
                                    && this.ShotsFired(stack) >= chargingSpell.shotsNumber(playerEntity, stack);
                        } else {
                            flag = true;
                        }
                        if (flag) {
                            this.setShots(stack, 0);
                            if (!spell.hasCustomCooldown(caster, stack, IWand.getFocus(stack), spell.spellCooldown(playerEntity))) {
                                SEHelper.addCooldown(playerEntity, IWand.getFocus(stack).getItem(), spell.spellCooldown(playerEntity));
                            }
                        }
                    }
                } else if (SEHelper.getSoulsAmount(playerEntity, SoulUse(caster, stack))) {
                    boolean spent = true;
                    if (spell instanceof IChargingSpell chargingSpell && chargingSpell.everCharge()) {
                        if (hasState(stack)) {
                            int seconds = getInt(stack, SECONDS) + 1;
                            if (seconds != 20) {
                                setInt(stack, SECONDS, seconds);
                                spent = false;
                            } else {
                                setInt(stack, SECONDS, 0);
                            }
                        }
                    }
                    if (spent) {
                        SEHelper.decreaseSouls(playerEntity, SoulUse(caster, stack));
                        SEHelper.sendSEUpdatePacket(playerEntity);
                        int villagerHate = ConfigHelper.getInt(MobsConfig.VillagerHateSpells, 0);
                        if (villagerHate > 0) {
                            for (Villager villager : caster.level().getEntitiesOfClass(Villager.class, caster.getBoundingBox().inflate(16.0D))) {
                                if (villager.hasLineOfSight(caster)) {
                                    villager.getGossips().add(caster.getUUID(), GossipType.MINOR_NEGATIVE, villagerHate);
                                }
                            }
                        }
                    }
                    if (hasState(stack)) {
                        spell.SpellResult(serverWorld, caster, stack, WandUtil.getStats(caster, spell));
                        boolean flag;
                        if (spell instanceof IChargingSpell chargingSpell) {
                            flag = chargingSpell.shotsNumber(playerEntity, stack) > 0
                                    && this.ShotsFired(stack) >= chargingSpell.shotsNumber(playerEntity, stack);
                        } else {
                            flag = true;
                        }
                        if (flag) {
                            this.setShots(stack, 0);
                            if (!spell.hasCustomCooldown(caster, stack, IWand.getFocus(stack), spell.spellCooldown(playerEntity))) {
                                SEHelper.addCooldown(playerEntity, IWand.getFocus(stack).getItem(), spell.spellCooldown(playerEntity));
                            }
                        }
                    }
                } else {
                    worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
                }
            }
            if (worldIn.isClientSide) {
                if (playerEntity.isCreative()) {
                    if (spell instanceof IBreathingSpell breathingSpell) {
                        breathingSpell.showWandBreath(caster, stack, WandUtil.getStats(caster, spell));
                    }
                } else if (SEHelper.getSoulsAmount(playerEntity, SoulUse(caster, stack))) {
                    if (spell instanceof IBreathingSpell breathingSpell) {
                        breathingSpell.showWandBreath(caster, stack, WandUtil.getStats(caster, spell));
                    }
                } else {
                    this.failParticles(worldIn, caster);
                }
            }
        } else {
            this.failParticles(worldIn, caster);
            worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        Player player = Goety.PROXY.getPlayer();
        if (hasState(stack)) {
            int soulUse = getInt(stack, SOULUSE);
            tooltip.add(Component.translatable("info.goety.wand.cost", soulUse));
            ISpell spell = getSpell(stack);
            if (spell != null) {
                if (this.isNotInstant(spell, player, stack) && !(spell instanceof IChargingSpell)) {
                    int castTime = getInt(stack, CASTTIME);
                    tooltip.add(Component.translatable("info.goety.wand.castTime", castTime / 20.0F));
                }
                if (player != null && spell.spellCooldown(player) > 0) {
                    tooltip.add(Component.translatable("info.goety.wand.coolDown", spell.spellCooldown(player) / 20.0F));
                }
            }
        } else {
            tooltip.add(Component.translatable("info.goety.wand.cost", SoulCost(stack)));
        }
        ItemStack focus = IWand.getFocus(stack);
        if (focus != null && !focus.isEmpty()) {
            tooltip.add(Component.translatable("info.goety.wand.focus", focus.getItem().getDescription()));
            if (focus.getItem() instanceof RecallFocus) {
                RecallFocus.addRecallText(focus, tooltip);
            }
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(focus.getItem(), tooltip));
        } else {
            tooltip.add(Component.translatable("info.goety.wand.focus", Component.translatable("info.goety.wand.empty")));
            if (ModKeybindings.wandSlot() != null) {
                tooltip.add(Component.translatable("info.goety.wand.open", ModKeybindings.wandSlot().getTranslatedKeyMessage()).withStyle(ChatFormatting.BLUE));
            }
        }
        if (ModKeybindings.wandCircle() != null) {
            tooltip.add(Component.translatable("info.goety.wand.switch", ModKeybindings.wandCircle().getTranslatedKeyMessage()).withStyle(ChatFormatting.BLUE));
        }
    }

    public void addInformationAfterShift(Item item, List<Component> tooltip) {
        tooltip.add(Component.translatable(item.getDescriptionId() + ".info").withStyle(ChatFormatting.GRAY));
    }

    // Client extensions are registered via RegisterClientExtensionsEvent in ClientSideInit;
    // do not also override initializeClient (deprecated in NeoForge 1.21) or registration runs twice.

    // ------------------------------------------------------------------
    // Phase 5: client-side visuals
    // ------------------------------------------------------------------

    public static class DarkWandClient implements IClientItemExtensions {

        // 1.21+: NeoForge switched ArmPose customisation to the enum-extension system, so the
        // bespoke SPELL/FLIGHT_POSE/HOLD_STAFF poses from the 1.20 reference can no longer be
        // declared inline. Spells already return vanilla pose constants via SpellPoses, which is
        // what getArmPose below picks up — there's no functional regression.

        @Override
        public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof IWand) {
                if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
                    ISpell spell = WandUtil.getSpell(entityLiving);
                    if (spell != null) {
                        return spell.getPose(entityLiving, itemStack, WandUtil.getStats(entityLiving, spell));
                    }
                }
            }
            return HumanoidModel.ArmPose.EMPTY;
        }

        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            if (player.isUsingItem()) {
                applyItemArmTransform(poseStack, arm, equipProcess);
                poseStack.translate((float) i * -0.2785682F, 0.18344387F, 0.15731531F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-13.935F));
                poseStack.mulPose(Axis.YP.rotationDegrees((float) i * 35.3F));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) i * -9.785F));
                float f8 = (float) itemInHand.getUseDuration(player) - ((float) player.getUseItemRemainingTicks() - partialTick + 1.0F);
                float f12 = f8 / 20.0F;
                f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                if (f12 > 1.0F) {
                    f12 = 1.0F;
                }
                if (f12 > 0.1F) {
                    float f15 = Mth.sin((f8 - 0.1F) * 1.3F);
                    float f18 = f12 - 0.1F;
                    float f20 = f15 * f18;
                    poseStack.translate(f20 * 0.0F, f20 * 0.004F, f20 * 0.0F);
                }
                poseStack.translate(f12 * 0.0F, f12 * 0.0F, f12 * 0.04F);
                poseStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                poseStack.mulPose(Axis.YN.rotationDegrees((float) i * 45.0F));
            } else {
                float f5 = -0.4F * Mth.sin(Mth.sqrt(swingProcess) * (float) Math.PI);
                float f6 = 0.2F * Mth.sin(Mth.sqrt(swingProcess) * ((float) Math.PI * 2F));
                float f10 = -0.2F * Mth.sin(swingProcess * (float) Math.PI);
                poseStack.translate((float) i * f5, f6, f10);
                this.applyItemArmTransform(poseStack, arm, equipProcess);
                this.applyItemArmAttackTransform(poseStack, arm, swingProcess);
            }
            return true;
        }

        private void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float equipProcess) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate((float) i * 0.56F, -0.52F + equipProcess * -0.6F, -0.72F);
        }

        private void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm humanoidArm, float swingProcess) {
            int i = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;
            float f = Mth.sin(swingProcess * swingProcess * (float) Math.PI);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) i * (45.0F + f * -20.0F)));
            float f1 = Mth.sin(Mth.sqrt(swingProcess) * (float) Math.PI);
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) i * f1 * -20.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(f1 * -80.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees((float) i * -45.0F));
        }
    }
}
