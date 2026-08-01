package za.co.infernos.goety.common.items.equipment;

import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.ModTiers;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.SEHelper;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DarkScytheItem extends TieredItem {
    // Lazy evaluation to avoid accessing config before it's loaded
    private static float getInitialDamage(Tier itemTier) {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getFloat(ItemConfig.ScytheBaseDamage, 1.0F) + itemTier.getAttackDamageBonus();
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 2.0F + itemTier.getAttackDamageBonus();
        }
    }
    
    private static double getScytheAttackSpeed() {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getDouble(ItemConfig.ScytheAttackSpeed, 20.0D);
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 1.0D;
        }
    }
    private final Multimap<Attribute, AttributeModifier> scytheAttributes;
    private final float initialDamage;

    public DarkScytheItem(Tier itemTier) {
        super(itemTier, new Properties().rarity(Rarity.UNCOMMON).durability(itemTier.getUses()));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.initialDamage = getInitialDamage(itemTier);
        double attackSpeed = 4.0D - getScytheAttackSpeed();
        builder.put(Attributes.ATTACK_DAMAGE.value(), new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, this.initialDamage - 1.0D,
                AttributeModifier.Operation.ADD_VALUE));
        builder.put(Attributes.ATTACK_SPEED.value(),
                new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -attackSpeed, AttributeModifier.Operation.ADD_VALUE));
        this.scytheAttributes = builder.build();
    }

    public DarkScytheItem() {
        this(ModTiers.SPECIAL);
    }

    // Lazy evaluation - returns default damage for SPECIAL tier if called statically
    public static float getInitialDamage() {
        return getInitialDamage(ModTiers.SPECIAL);
    }

    public boolean getMineBlocks(Level pLevel, BlockState pState, BlockPos pPos) {
        if (pState.getDestroySpeed(pLevel, pPos) <= -1.0F) {
            return false;
        }
        return pState.is(BlockTags.MINEABLE_WITH_HOE) || BlockFinder.isScytheBreak(pState);
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(BlockTags.MINEABLE_WITH_HOE) ? 8.0F : 1.0F;
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, EquipmentSlot.MAINHAND);
        if (pAttacker instanceof Player player) {
            this.attackMobs(pStack, pTarget, player);
        }
        return true;
    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos,
            LivingEntity pEntityLiving) {
        if (pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(this.getMineBlocks(pLevel, pState, pPos) ? 1 : 2, pEntityLiving, EquipmentSlot.MAINHAND);
        }
        if (this.getMineBlocks(pLevel, pState, pPos)) {
            pLevel.playSound((Player) null, pPos.getX(), pPos.getY(), pPos.getZ(), ModSounds.SCYTHE_HIT.get(),
                    pEntityLiving.getSoundSource(), 1.0F, 1.0F);
            for (BlockPos blockPos : BlockFinder.multiBlockBreak(pEntityLiving, pPos, 2, 2, 2)) {
                BlockState blockstate = pLevel.getBlockState(blockPos);
                if (this.getMineBlocks(pLevel, blockstate, blockPos)) {
                    if (BlockFinder.breakBlock(pLevel, blockPos, pStack, pEntityLiving)) {
                        if (blockstate.getDestroySpeed(pLevel, blockPos) != 0) {
                            pStack.hurtAndBreak(1, pEntityLiving, EquipmentSlot.MAINHAND);
                        }
                    }
                }
            }
        }

        return true;
    }

    public void attackMobs(ItemStack pStack, LivingEntity pTarget, Player pPlayer) {
        int enchantment = pStack.getEnchantmentLevel(ModEnchantments.SOUL_EATER);
        int soulEater = Mth.clamp(enchantment + 1, 1, 10);
        SEHelper.increaseSouls(pPlayer, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.DarkScytheSouls, 1) * soulEater);

        float f = (float) pPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = 0.0F;
        float f2 = pPlayer.getAttackStrengthScale(0.5F);
        f = f * (0.2F + f2 * f2 * 0.8F);
        f1 = f1 * f2;
        f = f + f1;

        if (f > 0.5F || f1 > 0.5F) {
            float f3 = 1.0F + f;
            int j = 0;
            double area = 1.0D;
            if (f2 > 0.9F) {
                area = 2.0D;
            }
            for (LivingEntity livingentity : pPlayer.level().getEntitiesOfClass(LivingEntity.class,
                    pTarget.getBoundingBox().inflate(area, 0.25D, area))) {
                if (livingentity != pPlayer && livingentity != pTarget && !pPlayer.isAlliedTo(livingentity)
                        && (!(livingentity instanceof ArmorStand) || !((ArmorStand) livingentity).isMarker())
                        && pPlayer.distanceToSqr(livingentity) < 16.0D && livingentity != pPlayer.getVehicle()) {
                    livingentity.knockback(0.4F, (double) Mth.sin(pPlayer.getYRot() * ((float) Math.PI / 180F)),
                            (double) (-Mth.cos(pPlayer.getYRot() * ((float) Math.PI / 180F))));
                    if (livingentity.hurt(livingentity.damageSources().playerAttack(pPlayer), f3)) {
                        if (j > 0) {
                            livingentity.igniteForSeconds(j * 4);
                        }
                        pStack.hurtAndBreak(1, pPlayer, EquipmentSlot.MAINHAND);
                        if (livingentity instanceof IOwned) {
                            if (((IOwned) livingentity).getTrueOwner() != pPlayer) {
                                SEHelper.increaseSouls(pPlayer, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.DarkScytheSouls, 1) * soulEater);
                            }
                        } else {
                            SEHelper.increaseSouls(pPlayer, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.DarkScytheSouls, 1) * soulEater);
                        }
                    }
                }
            }
        }

        pPlayer.level().playSound((Player) null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                ModSounds.SCYTHE_SWING.get(), pPlayer.getSoundSource(), 1.0F, 1.0F);
        pPlayer.sweepAttack();
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    public boolean isCorrectToolForDrops(BlockState pBlock) {
        return pBlock.is(BlockTags.MINEABLE_WITH_HOE);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // TODO(1.21): Enchantments are data-driven; re-implement custom allowlist if
        // needed.
        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return this.scytheAttributes;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.getItem() == ModItems.PALE_STEEL_INGOT.get() || super.isValidRepairItem(pToRepair, pRepair);
    }
}