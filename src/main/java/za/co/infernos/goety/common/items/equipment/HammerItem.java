package za.co.infernos.goety.common.items.equipment;

import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.ServerParticleUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

public class HammerItem extends TieredItem {
    // Lazy evaluation to avoid accessing config before it's loaded
    private static float getInitialDamage(Tier itemTier) {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getFloat(ItemConfig.HammerBaseDamage, 1.0F) + itemTier.getAttackDamageBonus();
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 2.0F + itemTier.getAttackDamageBonus();
        }
    }
    
    private static int getHammerDurability() {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.HammerDurability, 0);
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 500;
        }
    }
    
    private static double getHammerAttackSpeed() {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getDouble(ItemConfig.HammerAttackSpeed, 20.0D);
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 1.0D;
        }
    }
    private final Multimap<Attribute, AttributeModifier> hammerAttributes;
    protected final float speed;
    private final float initialDamage;

    public HammerItem(Tier itemTier) {
        // Lazy evaluation to avoid accessing config before it's loaded
        super(itemTier, new Properties().rarity(Rarity.UNCOMMON).durability(getHammerDurability()));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.initialDamage = getInitialDamage(itemTier);
        double attackSpeed = 4.0D - getHammerAttackSpeed();
        this.speed = itemTier.getSpeed() - 2.0F;
        builder.put(Attributes.ATTACK_DAMAGE.value(), new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,
                this.initialDamage - 1.0D, AttributeModifier.Operation.ADD_VALUE));
        builder.put(Attributes.ATTACK_SPEED.value(), new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,
                -attackSpeed, AttributeModifier.Operation.ADD_VALUE));
        this.hammerAttributes = builder.build();
    }

    public HammerItem() {
        this(Tiers.IRON);
    }

    // Lazy evaluation - returns default damage for IRON tier if called statically
    public static float getInitialDamage() {
        return getInitialDamage(Tiers.IRON);
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, EquipmentSlot.MAINHAND);
        if (pAttacker instanceof Player player) {
            float f2 = player.getAttackStrengthScale(0.5F);
            if (f2 > 0.9F) {
                this.attackMobs(pTarget, player, pStack);
                this.smash(pStack, pTarget, player);
            }
        }
        return true;
    }

    public void smash(ItemStack pStack, LivingEntity pTarget, Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.HAMMER_SWING.get(),
                player.getSoundSource(), 1.0F, 1.0F);
        if (pTarget.onGround()) {
            player.level().playSound(null, pTarget.getX(), pTarget.getY(), pTarget.getZ(), ModSounds.DIRT_DEBRIS.get(),
                    player.getSoundSource(), 1.0F, 1.0F);
        }
        if (player.level() instanceof ServerLevel serverLevel) {
            BlockPos blockPos = BlockPos.containing(pTarget.getX(), pTarget.getY() - 1.0F, pTarget.getZ());
            BlockParticleOption option = new BlockParticleOption(ParticleTypes.BLOCK,
                    serverLevel.getBlockState(blockPos));
            float area = 1.75F;
            area += pStack.getEnchantmentLevel(ModEnchantments.RADIUS);
            for (int i = 0; i < 8; ++i) {
                ServerParticleUtil.circularParticles(serverLevel, option, pTarget.getX(), pTarget.getY() + 0.25D,
                        pTarget.getZ(), area);
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        Level level = p_41427_.getLevel();
        BlockPos blockpos = p_41427_.getClickedPos();
        Player player = p_41427_.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        if (player != null) {
            ItemStack itemStack = p_41427_.getItemInHand();
            if (blockstate.is(Tags.Blocks.STORAGE_BLOCKS_IRON)) {
                itemStack.hurtAndBreak(5, player, EquipmentSlot.MAINHAND);
                level.setBlockAndUpdate(blockpos, Blocks.DAMAGED_ANVIL.defaultBlockState());
                level.scheduleTick(blockpos, Blocks.DAMAGED_ANVIL, 2);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(p_41427_);
    }

    public boolean getMineBlocks(Level pLevel, BlockState pState, BlockPos pPos) {
        return pState.is(BlockTags.MINEABLE_WITH_PICKAXE)
                && pState.getDestroySpeed(pLevel, pPos) > -1.0F;
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(BlockTags.MINEABLE_WITH_PICKAXE) ? this.speed : 1.0F;
    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos,
            LivingEntity pEntityLiving) {
        if (pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(this.getMineBlocks(pLevel, pState, pPos) ? 1 : 2, pEntityLiving, EquipmentSlot.MAINHAND);
        }
        if (this.getMineBlocks(pLevel, pState, pPos)) {
            pLevel.playSound((Player) null, pPos.getX(), pPos.getY(), pPos.getZ(), ModSounds.DIRT_DEBRIS.get(),
                    pEntityLiving.getSoundSource(), 1.0F, 1.0F);
            for (BlockPos blockPos : BlockFinder.multiBlockBreak(pEntityLiving, pPos, 1, 1, 1)) {
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

    public void attackMobs(LivingEntity pTarget, Player pPlayer, ItemStack pStack) {
        float f = (float) pPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = 0.0F;
        int j = 0;
        double area = 1.75D;
        area += pStack.getEnchantmentLevel(ModEnchantments.RADIUS);
        for (LivingEntity livingentity : pPlayer.level().getEntitiesOfClass(LivingEntity.class,
                pTarget.getBoundingBox().inflate(area, 0.25D, area))) {
            if (livingentity != pPlayer && livingentity != pTarget && !MobUtil.areAllies(pPlayer, livingentity)
                    && (!(livingentity instanceof ArmorStand) || !((ArmorStand) livingentity).isMarker())
                    && livingentity != pPlayer.getVehicle()) {
                livingentity.knockback(0.4F, (double) Mth.sin(pPlayer.getYRot() * ((float) Math.PI / 180F)),
                        (double) (-Mth.cos(pPlayer.getYRot() * ((float) Math.PI / 180F))));
                if (livingentity.hurt(pPlayer.damageSources().playerAttack(pPlayer), f + f1)) {
                    if (j > 0) {
                        livingentity.igniteForSeconds(j * 4);
                    }
                }
            }
        }

        pPlayer.level().playSound((Player) null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                ModSounds.HAMMER_IMPACT.get(), pPlayer.getSoundSource(), 1.0F, 1.0F);
    }

    public boolean isCorrectToolForDrops(BlockState pBlock) {
        return pBlock.is(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // TODO(1.21): Enchantments are data-driven; re-implement custom allowlist if
        // needed.
        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return this.hammerAttributes;
    }
}
