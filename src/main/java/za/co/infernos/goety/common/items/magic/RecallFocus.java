package za.co.infernos.goety.common.items.magic;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class RecallFocus extends Item {
    private static final String TAG_DIMENSION = "RecallDimension";
    private static final String TAG_POS = "RecallPos";

    public RecallFocus() {
        super(new Item.Properties().stacksTo(1));
    }

    public static boolean hasRecall(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(TAG_DIMENSION) && tag.contains(TAG_POS);
    }

    public static Optional<ResourceKey<Level>> getDimension(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains(TAG_DIMENSION)) {
            return Optional.empty();
        }
        return ResourceKey.codec(Registries.DIMENSION).parse(NbtOps.INSTANCE, tag.get(TAG_DIMENSION)).result();
    }

    public static BlockPos getRecallBlockPos(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return NbtUtilsCompat.readBlockPos(tag, TAG_POS);
    }

    public static void setRecall(ItemStack stack, ResourceKey<Level> dimension, BlockPos pos) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            ResourceKey.codec(Registries.DIMENSION).encodeStart(NbtOps.INSTANCE, dimension).result().ifPresent(n -> tag.put(TAG_DIMENSION, n));
            tag.put(TAG_POS, net.minecraft.nbt.NbtUtils.writeBlockPos(pos));
        });
    }

    /**
     * Legacy 1.20 entry point. The wand passes a CompoundTag it will then re-attach via
     * {@code CustomData.update}; we mutate the tag directly so the same call site keeps working.
     */
    public static void addRecallTags(ResourceKey<Level> dimension, BlockPos pos, CompoundTag tag) {
        if (tag == null) return;
        ResourceKey.codec(Registries.DIMENSION).encodeStart(NbtOps.INSTANCE, dimension).result().ifPresent(n -> tag.put(TAG_DIMENSION, n));
        tag.put(TAG_POS, net.minecraft.nbt.NbtUtils.writeBlockPos(pos));
    }

    public static void addRecallText(ItemStack stack, List<Component> tooltip) {
        BlockPos blockPos = getRecallBlockPos(stack);
        Optional<ResourceKey<Level>> dim = getDimension(stack);
        if (blockPos != null && dim.isPresent()) {
            ResourceLocation loc = dim.get().location();
            tooltip.add(Component.translatable("info.goety.focus.Position", blockPos.getX(), blockPos.getY(), blockPos.getZ()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("info.goety.focus.PosDim", loc.toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public static void clearRecall(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.remove(TAG_DIMENSION);
            tag.remove(TAG_POS);
        });
    }

    public static boolean isValid(Level level, ItemStack stack) {
        Optional<ResourceKey<Level>> dim = getDimension(stack);
        return hasRecall(stack) && dim.isPresent() && getRecallBlockPos(stack) != null && dim.get() == level.dimension();
    }

    public static void recall(LivingEntity livingEntity, ItemStack stack) {
        BlockPos target = getRecallBlockPos(stack);
        if (target == null) {
            return;
        }
        Optional<ResourceKey<Level>> dim = getDimension(stack);
        if (dim.isEmpty() || dim.get() != livingEntity.level().dimension()) {
            return;
        }
        livingEntity.teleportTo(target.getX() + 0.5D, target.getY() + 0.1D, target.getZ() + 0.5D);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        BlockPos blockPos = getRecallBlockPos(stack);
        Optional<ResourceKey<Level>> dim = getDimension(stack);
        if (blockPos != null && dim.isPresent()) {
            ResourceLocation loc = dim.get().location();
            tooltip.add(Component.translatable("info.goety.focus.Position", blockPos.getX(), blockPos.getY(), blockPos.getZ()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("info.goety.focus.PosDim", loc.toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private static final class NbtUtilsCompat {
        private NbtUtilsCompat() {
        }

        private static BlockPos readBlockPos(CompoundTag tag, String key) {
            if (!tag.contains(key, 10)) {
                return null;
            }
            return net.minecraft.nbt.NbtUtils.readBlockPos(tag, key).orElse(null);
        }
    }
}
