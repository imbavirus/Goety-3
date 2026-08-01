package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.ItemHelper;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Item;
import javax.annotation.Nullable;
import java.util.List;

public class PendantOfHungerItem extends SingleStackItem {
    private static final String ROTTEN_FLESH = "Rotten Flesh Count";

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof Player player) {
            if (!stack.has(DataComponents.CUSTOM_DATA)) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(ROTTEN_FLESH, 0));
            } else if (CuriosFinder.hasCurio(player, this)){
                if (getRottenFleshAmount(stack) < za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.PendantOfHungerLimit, 0)) {
                    if (!ItemHelper.findItem(player, Items.ROTTEN_FLESH).isEmpty()) {
                        increaseRottenFlesh(stack);
                        ItemHelper.findItem(player, Items.ROTTEN_FLESH).shrink(1);
                    }
                }
                if (getRottenFleshAmount(stack) > 0) {
                    if (MobUtil.validNonLich(player)) {
                        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0, false, false));
                        if (player.getFoodData().needsFood()) {
                            player.eat(player.level(), new ItemStack(Items.ROTTEN_FLESH));
                            decreaseRottenFlesh(stack);
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        CustomData.update(DataComponents.CUSTOM_DATA, pStack, tag -> tag.putInt(ROTTEN_FLESH, 0));
    }

    public void increaseRottenFlesh(ItemStack stack){
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(ROTTEN_FLESH, getRottenFleshAmount(stack) + 1));
    }

    public void decreaseRottenFlesh(ItemStack stack){
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(ROTTEN_FLESH, getRottenFleshAmount(stack) - 1));
    }

    public int getRottenFleshAmount(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.getInt(ROTTEN_FLESH);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    public int getBarColor(ItemStack stack) {
        float f = Math.max(0.0F, (float) (1.0F - amountColor(stack))/2.0F);
        return Mth.hsvToRgb(1.0F, f, f);
    }

    public double amountColor(ItemStack stack){
        int i = getRottenFleshAmount(stack);
        return 1.0D - (i / (double) za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.PendantOfHungerLimit, 0));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getRottenFleshAmount(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack){
        int power = getRottenFleshAmount(stack);
        return Math.round((power * 13.0F / za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.PendantOfHungerLimit, 0)));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        int rottenFlesh = getRottenFleshAmount(stack);
        tooltip.add(Component.translatable("info.goety.hunger_pendent.amount", rottenFlesh));
    }

}


