package za.co.infernos.goety.common.items.block;

import za.co.infernos.goety.common.blocks.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class RedstoneGolemSkullItem extends StandingAndWallBlockItem {
    
    public RedstoneGolemSkullItem(Properties p_43250_) {
        super(ModBlocks.REDSTONE_GOLEM_SKULL_BLOCK.get(), ModBlocks.WALL_REDSTONE_GOLEM_SKULL_BLOCK.get(), p_43250_, Direction.DOWN);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return getOwnerID(p_41453_) != null;
    }

    public static void setOwner(@Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag tag = data.copyTag();
            tag.putUUID("owner", entity.getUUID());
            tag.putString("owner_name", entity.getDisplayName().getString());
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

    public static void setCustomName(String string, ItemStack stack){
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = data.copyTag();
        tag.putString("mod_custom_name", string);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static String getCustomName(ItemStack stack){
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null){
            CompoundTag tag = data.copyTag();
            if (tag.contains("mod_custom_name")) {
                return tag.getString("mod_custom_name");
            }
        }
        return null;
    }

    @Nullable
    public static UUID getOwnerID(ItemStack stack){
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null){
            CompoundTag tag = data.copyTag();
            if (tag.contains("owner")) {
                return tag.getUUID("owner");
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            CompoundTag tag = data.copyTag();
            if (tag.contains("owner_name")) {
                tooltip.add(Component.translatable("tooltip.goety.arcaPlayer").setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))).append(Component.literal("" + tag.getString("owner_name")).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY)))));
            }
            if (tag.contains("mod_custom_name")){
                tooltip.add(Component.translatable("tooltip.goety.customName").setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))).append(Component.literal("" + tag.getString("mod_custom_name")).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY)))));
            }
        }
        super.appendHoverText(stack, context, tooltip, flagIn);
    }
}