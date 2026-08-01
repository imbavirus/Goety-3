package za.co.infernos.goety.common.items.equipment;

import za.co.infernos.goety.api.items.ISoulRepair;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.init.ModTags;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PhilosophersMaceItem extends Item implements ISoulRepair {
    private final Multimap<Attribute, AttributeModifier> maceAttributes;

    public PhilosophersMaceItem() {
        super(new Properties().rarity(Rarity.UNCOMMON).durability(getPhilosophersMaceDurability())
                .fireResistant());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE.value(), new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,
                getPhilosophersMaceDamage() - 1.0D, AttributeModifier.Operation.ADD_VALUE));
        builder.put(Attributes.ATTACK_SPEED.value(),
                new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (double) -2.4F, AttributeModifier.Operation.ADD_VALUE));
        this.maceAttributes = builder.build();
    }

    public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos,
            LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(1, pEntityLiving, EquipmentSlot.MAINHAND);
        }

        return true;
    }

    public boolean isCorrectToolForDrops(BlockState pBlock) {
        return pBlock.is(BlockTags.MINEABLE_WITH_PICKAXE) || pBlock.is(BlockTags.MINEABLE_WITH_AXE)
                || pBlock.is(BlockTags.MINEABLE_WITH_HOE) || pBlock.is(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    public float getDestroySpeed(ItemStack p_41004_, BlockState p_41005_) {
        float blockHard = p_41005_.getBlock().defaultDestroyTime();
        if (p_41005_.is(ModTags.Blocks.PHILOSOPHERS_MACE_HARD)) {
            return 1.0F;
        } else if (this.isCorrectToolForDrops(p_41004_, p_41005_) && blockHard >= 1.0F) {
            return 8.0F * blockHard;
        } else {
            return 8.0F;
        }
    }

    public int getEnchantmentValue(ItemStack stack) {
        return getPhilosophersMaceEnchantability();
    }
    
    private static int getPhilosophersMaceDurability() {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.PhilosophersMaceDurability, 0);
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 2031; // Default durability
        }
    }
    
    private static double getPhilosophersMaceDamage() {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getDouble(ItemConfig.PhilosophersMaceDamage, 20.0D);
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 4.0D; // Default damage
        }
    }
    
    private static int getPhilosophersMaceEnchantability() {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.PhilosophersMaceEnchantability, 0);
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 15; // Default enchantability
        }
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // TODO(1.21): Enchantments are data-driven; re-implement custom allowlist if
        // needed.
        return true;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot,
            ItemStack itemStack) {
        return this.maceAttributes;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.getItem() == ModItems.DARK_ALLOY_INGOT.get();
    }

}
