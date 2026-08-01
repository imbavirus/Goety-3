package za.co.infernos.goety.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class WaystoneItem extends Item {
    public static final String TAG_OWNER = "WaystoneOwner";

    public WaystoneItem() {
        super(new Properties().stacksTo(1));
    }

    public static boolean hasBlock(ItemStack stack) {
        return getPosition(stack) != null;
    }

    public static GlobalPos getPosition(ItemStack stack) {
        return null;
    }

    public static GlobalPos getPosition(CompoundTag tag) {
        return null;
    }

    public static BlockPos getBlockPos(ItemStack stack) {
        GlobalPos pos = getPosition(stack);
        return pos != null ? pos.pos() : null;
    }

    public static BlockEntity getBlockEntity(ItemStack stack, Level level) {
        BlockPos pos = getBlockPos(stack);
        return pos != null ? level.getBlockEntity(pos) : null;
    }

    public static boolean isSameDimension(LivingEntity livingEntity, ItemStack stack) {
        return true;
    }

    public static boolean isSameDimension(BlockEntity blockEntity, ItemStack stack) {
        return true;
    }

    public static boolean isInRange(Vec3 origin, ItemStack stack, int increase) {
        return true;
    }

    public static boolean canAffect(LivingEntity livingEntity, ItemStack stack, Vec3 origin, int increase) {
        return hasBlock(stack) && isSameDimension(livingEntity, stack) && isInRange(origin, stack, increase);
    }

    public static Direction getDirection(ItemStack stack) {
        return Direction.UP;
    }
}
