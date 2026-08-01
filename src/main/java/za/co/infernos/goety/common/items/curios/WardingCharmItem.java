package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.spells.IronHideSpell;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Item;

public class WardingCharmItem extends SingleStackItem{
    private static final String SOULUSE = "Soul Use";

    public boolean SoulDiscount(LivingEntity entityLiving){
        return CuriosFinder.hasCurio(entityLiving, itemStack -> itemStack.getItem() instanceof MagicRobeItem);
    }

    public boolean SoulCostUp(LivingEntity entityLiving){
        return entityLiving.hasEffect(GoetyEffects.SUMMON_DOWN);
    }

    public int SoulCalculation(LivingEntity entityLiving){
        if (SoulCostUp(entityLiving)){
            int amp = Objects.requireNonNull(entityLiving.getEffect(GoetyEffects.SUMMON_DOWN)).getAmplifier() + 2;
            return new IronHideSpell().defaultSoulCost() * amp;
        } else if (SoulDiscount(entityLiving)){
            return new IronHideSpell().defaultSoulCost() / 2;
        } else {
            return new IronHideSpell().defaultSoulCost();
        }
    }

    public int SoulUse(LivingEntity entityLiving, ItemStack stack){
        if (stack.isEnchanted()){
            return (int) (SoulCalculation(entityLiving) * 2 * SEHelper.soulDiscount(entityLiving));
        } else {
            return (int) (SoulCalculation(entityLiving) * SEHelper.soulDiscount(entityLiving));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity livingEntity) {
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(SOULUSE, SoulUse(livingEntity, stack)));
            if (!worldIn.isClientSide) {
                if (livingEntity instanceof Player player) {
                    if (!player.hasEffect(GoetyEffects.SOUL_ARMOR)) {
                        if (SEHelper.getSoulsAmount(player, SoulUse(player, stack))) {
                            List<Mob> mobs = worldIn.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(64, 16, 64));
                            Mob hostile = mobs.stream().filter(mob -> mob.getTarget() == player).findFirst().orElse(null);
                            if (hostile != null || player.hurtTime > 0) {
                                SEHelper.decreaseSouls(player, SoulUse(player, stack));
                                SEHelper.sendSEUpdatePacket(player);
                                int enchantment = 0;
                                int duration = 1;
                                if (stack.isEnchanted()) {
                                    enchantment = stack.getEnchantmentLevel(ModEnchantments.POTENCY);
                                    duration += stack.getEnchantmentLevel(ModEnchantments.DURATION);
                                }
                                player.addEffect(new MobEffectInstance(GoetyEffects.SOUL_ARMOR, MathHelper.minutesToTicks(duration), enchantment, false, false, true));
                                worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.IRON_HIDE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isEnchantable(ItemStack pStack) {
        return pStack.getCount() == 1;
    }

    public int getEnchantmentValue() {
        return 1;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == ModEnchantments.POTENCY.get()
                || enchantment == ModEnchantments.DURATION.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains(SOULUSE)) {
            int SoulUse = tag.getInt(SOULUSE);
            tooltip.add(Component.translatable("info.goety.wand.cost", SoulUse));
        }
    }
}
