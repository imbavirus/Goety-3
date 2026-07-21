package za.co.infernos.goety.common.items.magic;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import za.co.infernos.goety.common.magic.spells.void_spells.CallSpell;

import java.util.List;

public class CallFocus extends MagicFocus {
    public static final String TAG_ENTITY = "Summoned";

    public CallFocus() {
        super(new CallSpell());
    }

    public static void call(ServerPlayer player, ItemStack stack) {
        // Server-side resolution requires a world lookup; consumed by spell logic elsewhere.
    }

    public static boolean hasSummon(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(TAG_ENTITY);
    }

    public static void setSummon(CompoundTag compoundTag, LivingEntity livingEntity) {
        if (compoundTag != null && livingEntity != null) {
            compoundTag.putUUID(TAG_ENTITY, livingEntity.getUUID());
        }
    }

    public static void setSummon(ItemStack stack, LivingEntity livingEntity) {
        if (stack != null && livingEntity != null) {
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putUUID(TAG_ENTITY, livingEntity.getUUID()));
        }
    }

    public static LivingEntity getSummon(ItemStack stack) {
        return null;
    }

    public static LivingEntity getSummon(CompoundTag compoundTag) {
        return null;
    }

    public static LivingEntity getSummonClient(Level level, ItemStack stack) {
        if (!hasSummon(stack)) {
            return null;
        }
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (level instanceof ServerLevel sl) {
            Entity entity = sl.getEntity(tag.getUUID(TAG_ENTITY));
            return entity instanceof LivingEntity le ? le : null;
        }
        return null;
    }

    public static void addCallText(ItemStack stack, List<Component> tooltip) {
        if (hasSummon(stack)) {
            tooltip.add(Component.translatable("info.goety.focus.bound").withStyle(ChatFormatting.GRAY));
        }
    }
}
