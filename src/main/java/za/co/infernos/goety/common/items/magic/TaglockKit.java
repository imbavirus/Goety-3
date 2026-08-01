package za.co.infernos.goety.common.items.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.Vec3;

public class TaglockKit extends Item {
    private static final String TAG_ENTITY = "TaglockEntity";
    private static final String TAG_PLAYER_NAME = "TaglockPlayerName";
    private static final String TAG_DIMENSION = "TaglockDimension";
    private static final String TAG_POS = "TaglockPos";

    public TaglockKit() {
        super(new Item.Properties().stacksTo(1));
    }

    public static boolean hasEntity(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(TAG_ENTITY) || tag.contains(TAG_PLAYER_NAME);
    }

    public static LivingEntity getEntity(ItemStack stack) {
        return null;
    }

    public static void setEntity(ItemStack stack, LivingEntity entity) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            if (entity.getUUID() != null) {
                tag.putUUID(TAG_ENTITY, entity.getUUID());
            }
            tag.putString(TAG_PLAYER_NAME, entity.getName().getString());
            tag.putString(TAG_DIMENSION, entity.level().dimension().location().toString());
            tag.put(TAG_POS, NbtUtils.writeBlockPos(entity.blockPosition()));
        });
    }

    public static void removeEntity(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.remove(TAG_ENTITY);
            tag.remove(TAG_PLAYER_NAME);
            tag.remove(TAG_DIMENSION);
            tag.remove(TAG_POS);
        });
    }

    public static boolean isSameDimension(LivingEntity livingEntity, ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains(TAG_DIMENSION)) {
            return false;
        }
        return tag.getString(TAG_DIMENSION).equals(livingEntity.level().dimension().location().toString());
    }

    public static boolean isInRange(Vec3 origin, ItemStack stack, int increase) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains(TAG_POS, 10)) {
            return false;
        }
        BlockPos pos = NbtUtils.readBlockPos(tag, TAG_POS).orElse(null);
        if (pos == null) {
            return false;
        }
        double maxRange = 64.0D + Math.max(0, increase) * 16.0D;
        return origin.distanceToSqr(Vec3.atCenterOf(pos)) <= (maxRange * maxRange);
    }

    public static boolean canAffect(LivingEntity livingEntity, ItemStack stack, Vec3 origin, int increase) {
        return stack.getItem() instanceof TaglockKit
                && hasEntity(stack)
                && isSameDimension(livingEntity, stack)
                && isInRange(origin, stack, increase);
    }
}
