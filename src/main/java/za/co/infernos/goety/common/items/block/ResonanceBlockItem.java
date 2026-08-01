package za.co.infernos.goety.common.items.block;

import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.entities.ally.golem.SquallGolem;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SPlayPlayerSoundPacket;
import za.co.infernos.goety.utils.EntityFinder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.sounds.SoundEvents;
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

public class ResonanceBlockItem extends BlockItemBase{
    public static String GOLEM_LIST = "golemList";

    public ResonanceBlockItem() {
        super(ModBlocks.RESONANCE_CRYSTAL.get());
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isClientSide) {
            ListTag listTag = getGolemList(stack, worldIn);
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (listTag != null && customData != null){
                if (listTag.isEmpty()){
                    CompoundTag tag = customData.copyTag();
                    tag.remove(GOLEM_LIST);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
            if (!getGolems(stack, worldIn).isEmpty()) {
                for (SquallGolem squallGolem : getGolems(stack, worldIn)) {
                    if (squallGolem == null || squallGolem.isDeadOrDying()) {
                        removeGolem(stack, squallGolem, worldIn);
                    }
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        CustomData customData = p_41453_.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.contains(GOLEM_LIST);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() || player.isCrouching()){
            if (itemstack.getItem() instanceof ResonanceBlockItem){
                CustomData customData = itemstack.get(DataComponents.CUSTOM_DATA);
                if (customData != null){
                    CompoundTag tag = customData.copyTag();
                    tag.remove(GOLEM_LIST);
                    itemstack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemstack);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!player.level().isClientSide) {
            if (entity instanceof SquallGolem squallGolem) {
                ListTag listTag = getGolemList(stack, player.level());
                if (listTag == null || listTag.size() < 4) {
                    setGolems(stack, player, squallGolem);
                    ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                }
            }
        }
        return true;
    }

    public static ListTag getGolemList(ItemStack stack, Level level){
        if (!level.isClientSide) {
            CompoundTag compound = new CompoundTag();
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                compound = customData.copyTag();
            }
            if (compound != null) {
                if (compound.contains(GOLEM_LIST)) {
                    return compound.getList(GOLEM_LIST, 8);
                }
            }
        }
        return null;
    }

    public static void removeGolem(ItemStack stack, SquallGolem squallGolem, Level level){
        if (!level.isClientSide) {
            CompoundTag compound = new CompoundTag();
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                compound = customData.copyTag();
            }
            List<String> list = new ArrayList<>();
            if (compound != null) {
                if (compound.contains(GOLEM_LIST)) {
                    for (int i = 0; i < compound.getList(GOLEM_LIST, 8).size(); ++i) {
                        list.add(compound.getList(GOLEM_LIST, 8).getString(i));
                    }
                }

                if (list.contains(squallGolem.getStringUUID())) {
                    ListTag nbttaglist = new ListTag();
                    if (compound.contains(GOLEM_LIST)) {
                        nbttaglist = compound.getList(GOLEM_LIST, 8);
                    }

                    nbttaglist.remove(StringTag.valueOf(squallGolem.getStringUUID()));
                    compound.put(GOLEM_LIST, nbttaglist);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
                }
            }
        }
    }

    public static List<SquallGolem> getGolems(ItemStack stack, Level level){
        List<SquallGolem> squallGolems = new ArrayList<>();
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (!level.isClientSide && customData != null){
            CompoundTag tag = customData.copyTag();
            ListTag list = tag.getList(GOLEM_LIST, 8);
            for(int i = 0; i < list.size(); ++i) {
                Entity entity = EntityFinder.getEntityByUuiD(UUID.fromString(list.getString(i)));
                if (entity instanceof SquallGolem squallGolem){
                    squallGolems.add(squallGolem);
                }
            }
        }
        return squallGolems;
    }

    public static void setGolems(ItemStack stack, Player player, SquallGolem squallGolem){
        if (!player.level().isClientSide) {
            CompoundTag compound = new CompoundTag();
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                compound = customData.copyTag();
            }
            List<String> list = new ArrayList<>();
            if (compound != null) {
                if (compound.contains(GOLEM_LIST)) {
                    for (int i = 0; i < compound.getList(GOLEM_LIST, 8).size(); ++i) {
                        list.add(compound.getList(GOLEM_LIST, 8).getString(i));
                    }
                }

                if (!list.contains(squallGolem.getStringUUID())) {
                    ListTag nbttaglist = new ListTag();
                    if (compound.contains(GOLEM_LIST)) {
                        nbttaglist = compound.getList(GOLEM_LIST, 8);
                    }

                    nbttaglist.add(StringTag.valueOf(squallGolem.getStringUUID()));
                    compound.put(GOLEM_LIST, nbttaglist);
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
            if (compound.contains(GOLEM_LIST)) {
                for (int i = 0; i < compound.getList(GOLEM_LIST, 8).size(); ++i) {
                    list.add(compound.getList(GOLEM_LIST, 8).getString(i));
                }
            }

            if (!list.contains(uuid.toString())) {
                ListTag nbttaglist = new ListTag();
                if (compound.contains(GOLEM_LIST)) {
                    nbttaglist = compound.getList(GOLEM_LIST, 8);
                }

                nbttaglist.add(StringTag.valueOf(uuid.toString()));
                compound.put(GOLEM_LIST, nbttaglist);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
            }
        }
    }
}
