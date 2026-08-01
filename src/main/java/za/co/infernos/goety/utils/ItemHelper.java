package za.co.infernos.goety.utils;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.ModTiers;
import za.co.infernos.goety.common.items.equipment.PhilosophersMaceItem;
import za.co.infernos.goety.config.ItemConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.List;
import java.util.function.Predicate;

public class ItemHelper {

    public static <T extends LivingEntity> void hurtAndRemove(ItemStack stack, int pAmount, T pEntity) {
        if (!pEntity.level().isClientSide && (!(pEntity instanceof Player) || !((Player)pEntity).getAbilities().instabuild)) {
            if (stack.isDamageableItem()) {
                stack.hurtAndBreak(pAmount, pEntity, EquipmentSlot.MAINHAND);
            }
        }
    }

    public static <T extends LivingEntity> void hurtAndBreak(ItemStack itemStack, int pAmount, T pEntity) {
        itemStack.hurtAndBreak(pAmount, pEntity, EquipmentSlot.MAINHAND);
    }

    public static void hurtNoEntity(ItemStack itemStack, int pAmount, Level level){
        if (!level.isClientSide) {
            if (itemStack.isDamageableItem()) {
                itemStack.setDamageValue(Math.min(itemStack.getMaxDamage(), itemStack.getDamageValue() + pAmount));
            }
        }
    }

    public static ItemEntity itemEntityDrop(LivingEntity livingEntity, ItemStack itemStack){
        return new ItemEntity(livingEntity.level(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), itemStack);
    }

    public static void addItemEntity(Level level, BlockPos blockPos, ItemStack itemStack){
        double d0 = (double) (level.random.nextFloat() * 0.5F) + 0.25D;
        double d1 = (double) (level.random.nextFloat() * 0.5F) + 0.25D;
        double d2 = (double) (level.random.nextFloat() * 0.5F) + 0.25D;
        ItemEntity itementity = new ItemEntity(level, (double) blockPos.getX() + d0, (double) blockPos.getY() + d1, (double) blockPos.getZ() + d2, itemStack);
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
    }

    public static void addAndConsumeItem(Player player, InteractionHand hand, ItemStack toAdd) {
        addAndConsumeItem(player, hand, toAdd, true);
    }

    public static void addAndConsumeItem(Player player, InteractionHand hand, ItemStack toAdd, boolean addToInventory) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getCount() == 1) {
            if (!player.isCreative()) {
                player.setItemInHand(hand, toAdd);
            } else if (addToInventory){
                if (!player.getInventory().add(toAdd)) {
                    player.drop(toAdd, false, true);
                }
            }
        } else {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            if (!player.getInventory().add(toAdd)) {
                player.drop(toAdd, false, true);
            }
        }
    }

    public static boolean hasItem(Player player, Item item){
        return !findItem(player, item).isEmpty();
    }

    public static ItemStack findItem(Player playerEntity, Item item){
        ItemStack foundStack = ItemStack.EMPTY;
        for (int i = 0; i < playerEntity.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = playerEntity.getInventory().getItem(i);
            if (!itemStack.isEmpty() && itemStack.getItem() == item) {
                foundStack = itemStack;
                break;
            }
        }
        return foundStack;
    }

    public static ItemStack findItem(Player playerEntity, Predicate<ItemStack> item){
        ItemStack foundStack = ItemStack.EMPTY;
        for (int i = 0; i < playerEntity.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = playerEntity.getInventory().getItem(i);
            if (!itemStack.isEmpty() && item.test(itemStack)) {
                foundStack = itemStack;
                break;
            }
        }
        return foundStack;
    }

    public static boolean findHelmet(Player player, Item item){
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() == item;
    }

    public static boolean armorSet(LivingEntity living, ArmorMaterial material){
        int i = 0;
        ItemStack helmetStack = living.getItemBySlot(EquipmentSlot.HEAD);
        if (helmetStack.getItem() instanceof ArmorItem helmet && helmet.getMaterial().value() == material) {
            ResourceLocation leather = ResourceLocation.withDefaultNamespace("leather");
            boolean isLeather = material.equals(BuiltInRegistries.ARMOR_MATERIAL.get(leather));
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (equipmentSlot.isArmor()) {
                    ItemStack stack = living.getItemBySlot(equipmentSlot);
                    if (stack.getItem() instanceof ArmorItem armorItem) {
                        if (armorItem.getMaterial().value() == material) {
                            if (isLeather && BuiltInRegistries.ITEM.getKey(armorItem).getNamespace().equals("minecraft")) {
                                continue;
                            }
                            ++i;
                        }
                    }
                }
            }
        }
        return i >= 4;
    }



    public static boolean isFullEquipped(LivingEntity living){
        int i = 0;
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()){
            if (equipmentSlot.isArmor()){
                if (!living.getItemBySlot(equipmentSlot).isEmpty()){
                    ++i;
                }
            }
        }
        return i >= 4;
    }

    public static boolean isFullArmored(LivingEntity living){
        int i = 0;
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()){
            if (equipmentSlot.isArmor()){
                if (living.getItemBySlot(equipmentSlot).getItem() instanceof ArmorItem){
                    ++i;
                }
            }
        }
        return i >= 4;
    }

    public static boolean noArmor(LivingEntity living){
        int i = 0;
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()){
            if (equipmentSlot.isArmor()){
                if (living.getItemBySlot(equipmentSlot).isEmpty()){
                    ++i;
                }
            }
        }
        return i >= 4;
    }

    public static void repairTick(ItemStack stack, Entity entityIn, boolean isSelected){
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(ItemConfig.SoulRepair, false)) {
            if (entityIn instanceof Player player) {
                if (!(player.swinging && isSelected)) {
                    if (stack.isDamaged()) {
                        if (SEHelper.getSoulsContainer(player)){
                            int i = 1;
                            if (SEHelper.getSoulsAmount(player, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.ItemsRepairAmount, 0) * i)){
                                if (player.tickCount % 20 == 0) {
                                    stack.setDamageValue(stack.getDamageValue() - 1);
                                    SEHelper.decreaseSouls(player, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.ItemsRepairAmount, 0) * i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Based on fluid codes from @Vazkii.
     */
    public static boolean isValidFluidContainerToDrain(ItemStack stack, Fluid fluid) {
        return false;
    }

    public static ItemStack drain(Fluid fluid, ItemStack stack) {
        return stack;
    }

    public static boolean isValidFluidContainerToFill(ItemStack stack, Fluid fluid) {
        return false;
    }

    public static ItemStack fill(Fluid fluid, ItemStack stack) {
        return stack;
    }

    public static void setItemEffect(ItemStack stack, LivingEntity victim){
        if (stack.getItem() instanceof TieredItem weapon){
            if (weapon.getTier() == ModTiers.DARK) {
                victim.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.WANE.get()), 60));
            }
            if (weapon == ModItems.FELL_BLADE.get() && victim.getRandom().nextBoolean()) {
                victim.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.BUSTED.get()), MathHelper.secondsToTicks(5)));
            }
            if (weapon == ModItems.FROZEN_BLADE.get()) {
                victim.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.FREEZING.get()), MathHelper.secondsToTicks(2)));
            }
        } else if (stack.getItem() instanceof PhilosophersMaceItem){
            int i2 = 0;
            victim.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.GOLD_TOUCHED.get()), 300, i2));
        }
    }

    //Stolen from @Vazkii: https://github.com/VazkiiMods/Botania/blob/1.20.x/Xplat/src/main/java/vazkii/botania/client/gui/TooltipHandler.java
    public static Component getShiftInfoTooltip() {
        Component shift = Component.literal("SHIFT").withStyle(ChatFormatting.AQUA);
        return Component.translatable("info.goety.item_info", shift).withStyle(ChatFormatting.GRAY);
    }

    public static void addOnShift(List<Component> tooltip, Runnable lambda) {
        if (Screen.hasShiftDown()) {
            lambda.run();
        } else {
            tooltip.add(getShiftInfoTooltip());
        }
    }

    public static boolean sameBanner(ItemStack banner1, ItemStack banner2){
        return banner1.getItem() instanceof BannerItem
                && banner2.getItem() instanceof BannerItem
                && banner1.getItem() == banner2.getItem();
    }

    public static int repairPlayerItems(Player p_147093_, int experience) {
        return experience;
    }

    private static int durabilityToXp(int p_20794_) {
        return p_20794_ / 2;
    }

    public static InteractionResultHolder<ItemStack> getVoidBottle(Player player, Level world, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.isEmpty() || !stack.is(Items.GLASS_BOTTLE) || world.dimension().location().toString().contains("aether")) {
            return InteractionResultHolder.pass(stack);
        }

        BlockHitResult result = getPlayerPOVHitResult(world, player, ClipContext.Fluid.ANY);

        if (result.getLocation().y <= world.getMinBuildHeight()) {
            if (!world.isClientSide) {
                ItemStack enderAir = new ItemStack(ModItems.VOID_BOTTLE.get());
                player.getInventory().placeItemBackInInventory(enderAir);
                stack.shrink(1);
                world.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.5F, 1.0F);
                world.gameEvent(player, GameEvent.FLUID_PICKUP, player.position());
            }

            return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
        }

        return InteractionResultHolder.pass(stack);
    }

    protected static BlockHitResult getPlayerPOVHitResult(Level p_41436_, Player p_41437_, ClipContext.Fluid p_41438_) {
        float f = p_41437_.getXRot();
        float f1 = p_41437_.getYRot();
        Vec3 vec3 = p_41437_.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = 5.0D;
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return p_41436_.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, p_41438_, p_41437_));
    }
}
