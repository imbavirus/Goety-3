package za.co.infernos.goety.common.items.block;

import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.entities.ally.illager.RaiderServant;
import za.co.infernos.goety.utils.EntityFinder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OminousIdolBlockItem extends BlockItemBase{
    public static String ILLAGER_LIST = "illagerList";

    public OminousIdolBlockItem() {
        super(ModBlocks.OMINOUS_IDOL.get());
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isClientSide) {
            ListTag listTag = getIllagerList(stack, worldIn);
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (listTag != null && customData != null){
                if (listTag.isEmpty()){
                    CompoundTag tag = customData.copyTag();
                    tag.remove(ILLAGER_LIST);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
            if (!getIllagers(stack, worldIn).isEmpty()) {
                for (RaiderServant illagerServant : getIllagers(stack, worldIn)) {
                    if (illagerServant == null || illagerServant.isDeadOrDying()) {
                        removeIllager(stack, illagerServant, worldIn);
                    }
                }
            } else if (customData != null){
                if (customData.contains(ILLAGER_LIST)){
                    CompoundTag tag = customData.copyTag();
                    tag.remove(ILLAGER_LIST);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        CustomData customData = p_41453_.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.contains(ILLAGER_LIST);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() || player.isCrouching()){
            if (itemstack.getItem() instanceof OminousIdolBlockItem){
                CustomData customData = itemstack.get(DataComponents.CUSTOM_DATA);
                if (customData != null){
                    CompoundTag tag = customData.copyTag();
                    tag.remove(ILLAGER_LIST);
                    itemstack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemstack);
    }

    public static ListTag getIllagerList(ItemStack stack, Level level){
        if (!level.isClientSide) {
            CompoundTag compound = new CompoundTag();
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                compound = customData.copyTag();
            }
            if (compound != null) {
                if (compound.contains(ILLAGER_LIST)) {
                    return compound.getList(ILLAGER_LIST, 8);
                }
            }
        }
        return null;
    }

    public static void removeIllager(ItemStack stack, RaiderServant illagerServant, Level level){
        if (!level.isClientSide) {
            CompoundTag compound = new CompoundTag();
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                compound = customData.copyTag();
            }
            List<String> list = new ArrayList<>();
            if (compound != null) {
                if (compound.contains(ILLAGER_LIST)) {
                    for (int i = 0; i < compound.getList(ILLAGER_LIST, 8).size(); ++i) {
                        list.add(compound.getList(ILLAGER_LIST, 8).getString(i));
                    }
                }

                if (list.contains(illagerServant.getStringUUID())) {
                    ListTag nbttaglist = new ListTag();
                    if (compound.contains(ILLAGER_LIST)) {
                        nbttaglist = compound.getList(ILLAGER_LIST, 8);
                    }

                    nbttaglist.remove(StringTag.valueOf(illagerServant.getStringUUID()));
                    compound.put(ILLAGER_LIST, nbttaglist);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
                }
            }
        }
    }

    public static List<RaiderServant> getIllagers(ItemStack stack, Level level){
        List<RaiderServant> illagerServants = new ArrayList<>();
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (!level.isClientSide && customData != null){
            CompoundTag tag = customData.copyTag();
            ListTag list = tag.getList(ILLAGER_LIST, 8);
            for(int i = 0; i < list.size(); ++i) {
                Entity entity = EntityFinder.getEntityByUuiD(UUID.fromString(list.getString(i)));
                if (entity instanceof RaiderServant servant){
                    illagerServants.add(servant);
                }
            }
        }
        return illagerServants;
    }

    public static void setIllager(ItemStack stack, Player player, RaiderServant illagerServant){
        if (!player.level().isClientSide) {
            CompoundTag compound = new CompoundTag();
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                compound = customData.copyTag();
            }
            List<String> list = new ArrayList<>();
            if (compound != null) {
                if (compound.contains(ILLAGER_LIST)) {
                    for (int i = 0; i < compound.getList(ILLAGER_LIST, 8).size(); ++i) {
                        list.add(compound.getList(ILLAGER_LIST, 8).getString(i));
                    }
                }

                if (!list.contains(illagerServant.getStringUUID())) {
                    ListTag nbttaglist = new ListTag();
                    if (compound.contains(ILLAGER_LIST)) {
                        nbttaglist = compound.getList(ILLAGER_LIST, 8);
                    }

                    nbttaglist.add(StringTag.valueOf(illagerServant.getStringUUID()));
                    compound.put(ILLAGER_LIST, nbttaglist);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
                }
            }
        }
    }

    public static void setUUIDs(ItemStack stack, UUID uuid){
        CompoundTag compound = new CompoundTag();
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            compound = customData.copyTag();
        }
        List<String> list = new ArrayList<>();
        if (compound != null) {
            if (compound.contains(ILLAGER_LIST)) {
                for (int i = 0; i < compound.getList(ILLAGER_LIST, 8).size(); ++i) {
                    list.add(compound.getList(ILLAGER_LIST, 8).getString(i));
                }
            }

            if (!list.contains(uuid.toString())) {
                ListTag nbttaglist = new ListTag();
                if (compound.contains(ILLAGER_LIST)) {
                    nbttaglist = compound.getList(ILLAGER_LIST, 8);
                }

                nbttaglist.add(StringTag.valueOf(uuid.toString()));
                compound.put(ILLAGER_LIST, nbttaglist);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
            }
        }
    }

    public static void clearUUIDs(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            if (customData.contains(ILLAGER_LIST)) {
                CompoundTag tag = customData.copyTag();
                tag.remove(ILLAGER_LIST);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
        }
    }
}
