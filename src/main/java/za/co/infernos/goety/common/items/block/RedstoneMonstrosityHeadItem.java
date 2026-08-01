package za.co.infernos.goety.common.items.block;

import za.co.infernos.goety.common.blocks.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class RedstoneMonstrosityHeadItem extends StandingAndWallBlockItem {

    public RedstoneMonstrosityHeadItem(Properties p_43250_) {
        super(ModBlocks.REDSTONE_MONSTROSITY_HEAD_BLOCK.get(), ModBlocks.WALL_REDSTONE_MONSTROSITY_HEAD_BLOCK.get(), p_43250_, Direction.DOWN);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return getOwnerID(p_41453_) != null;
    }

    public static void setOwner(@Nullable LivingEntity entity, ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag entityTag = customData.copyTag();
        if (entity != null) {
            entityTag.putUUID("owner", entity.getUUID());
            entityTag.putString("owner_name", entity.getDisplayName().getString());
        }
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
    }

    public static void setCustomName(String string, ItemStack stack){
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag entityTag = customData.copyTag();
        entityTag.putString("mod_custom_name", string);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
    }

    public static String getCustomName(ItemStack stack){
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null){
            CompoundTag entityTag = customData.copyTag();
            if (entityTag.contains("mod_custom_name")) {
                return entityTag.getString("mod_custom_name");
            }
        }
        return null;
    }

    @Nullable
    public static UUID getOwnerID(ItemStack stack){
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null){
            CompoundTag entityTag = customData.copyTag();
            if (entityTag.contains("owner")) {
                return entityTag.getUUID("owner");
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag entityTag = customData.copyTag();
            if (entityTag.contains("owner_name")) {
                tooltip.add(Component.translatable("tooltip.goety.arcaPlayer").setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))).append(Component.literal("" + entityTag.getString("owner_name")).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY)))));
            }
            if (entityTag.contains("mod_custom_name")){
                tooltip.add(Component.translatable("tooltip.goety.customName").setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))).append(Component.literal("" + entityTag.getString("mod_custom_name")).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY)))));
            }
        }
        super.appendHoverText(stack, context, tooltip, flagIn);
    }
}