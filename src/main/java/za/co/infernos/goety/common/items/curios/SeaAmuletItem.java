package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SPlayPlayerSoundPacket;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.utils.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.List;

public class SeaAmuletItem extends SingleStackItem{
    private static final String CONDUIT_CHARGES = "Conduit Charges";
    // Lazy evaluation to avoid accessing config before it's loaded
    private static int getMaxPower() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.SeaAmuletMax, 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof Player player) {
            boolean flag = true;
            if (!stack.has(DataComponents.CUSTOM_DATA)) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(CONDUIT_CHARGES, 0));
            } else {
                if (this.getConduitChargesAmount(stack) > getMaxPower()){
                    this.setConduitCharges(stack, getMaxPower());
                } else if (this.getConduitChargesAmount(stack) < 0){
                    this.setConduitCharges(stack, 0);
                }
                if (CuriosFinder.hasCurio(player, this)){
                    if (player.isUnderWater()) {
                        BlockEntity blockEntity = BlockFinder.findBlockEntity(BlockEntityType.CONDUIT, worldIn, player.blockPosition(), 8);
                        if (blockEntity instanceof ConduitBlockEntity blockEntity1) {
                            if (blockEntity1.isActive()) {
                                if (this.getConduitChargesAmount(stack) < getMaxPower()) {
                                    if (worldIn instanceof ServerLevel serverLevel) {
                                        ServerParticleUtil.gatheringParticles(ParticleTypes.NAUTILUS, player, serverLevel);
                                    }
                                    this.increaseConduitCharges(stack);
                                }
                                flag = false;
                            }
                        }
                        int duration = MathHelper.secondsToTicks(5);
                        if (player.hasEffect(MobEffects.CONDUIT_POWER)){
                            MobEffectInstance mobEffectInstance = player.getEffect(MobEffects.CONDUIT_POWER);
                            flag = mobEffectInstance != null && mobEffectInstance.getDuration() < duration;
                        }
                        if (flag && this.getConduitChargesAmount(stack) > 0 && MobUtil.validNonLich(player)) {
                            this.decreaseConduitCharges(stack);
                            if (!worldIn.isClientSide){
                                if (player instanceof ServerPlayer serverPlayer){
                                    ModNetwork.sendToClient(serverPlayer, new SPlayPlayerSoundPacket(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1.0F, 1.0F));
                                }
                            }
                            player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, duration * 2, 0, false, false));
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        CustomData.update(DataComponents.CUSTOM_DATA, pStack, tag -> tag.putInt(CONDUIT_CHARGES, 0));
    }

    public void increaseConduitCharges(ItemStack stack){
        setConduitCharges(stack, Math.min(getMaxPower(), getConduitChargesAmount(stack) + za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.SeaAmuletChargeConsume, 0)));
    }

    public void decreaseConduitCharges(ItemStack stack){
        setConduitCharges(stack, Math.max(0, getConduitChargesAmount(stack) - za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.SeaAmuletChargeConsume, 0)));
    }

    public void setConduitCharges(ItemStack stack, int charges){
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(CONDUIT_CHARGES, charges));
    }

    public int getConduitChargesAmount(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.getInt(CONDUIT_CHARGES);
    }

    public int getBarColor(ItemStack stack) {
        float f = Math.max(0.0F, (float) (1.0F - amountColor(stack))/2.0F);
        return Mth.hsvToRgb(f, 1.0F, f);
    }

    public double amountColor(ItemStack stack){
        int i = getConduitChargesAmount(stack);
        return 1.0D - (i / (double) getMaxPower());
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getConduitChargesAmount(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack){
        int power = getConduitChargesAmount(stack);
        return Math.round((power * 13.0F / getMaxPower()));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        int conduit = getConduitChargesAmount(stack);
        tooltip.add(Component.translatable("info.goety.sea_amulet.amount", conduit));
    }
}
