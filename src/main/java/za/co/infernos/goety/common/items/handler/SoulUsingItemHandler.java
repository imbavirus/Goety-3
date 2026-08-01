package za.co.infernos.goety.common.items.handler;

import za.co.infernos.goety.api.items.magic.IFocus;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nonnull;

public class SoulUsingItemHandler extends ItemStackHandler {
    private final ItemStack itemStack;
    private int slot;

    public SoulUsingItemHandler(ItemStack itemStack) {
        this.itemStack = itemStack;
        net.minecraft.world.item.component.CustomData customData = itemStack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
        if (customData != null) {
            deserializeNBT(net.minecraft.core.RegistryAccess.EMPTY, customData.copyTag());
        }
    }

    public ItemStack extractItem() {
        return extractItem(slot, 1, false);
    }

    public ItemStack insertItem(ItemStack insert) {
        return insertItem(slot, insert, false);
    }

    public ItemStack getSlot() {
        return getStackInSlot(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return stack.getItem() instanceof IFocus;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    public NonNullList<ItemStack> getContents(){
        return stacks;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = super.serializeNBT(provider);
        nbt.putInt("slot", slot);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        super.deserializeNBT(provider, nbt);
        if (nbt.contains("slot")) {
            slot = nbt.getInt("slot");
        }
        onLoad();
    }

    @Override
    protected void onContentsChanged(int slot) {
        CompoundTag nbt = serializeNBT(net.minecraft.core.RegistryAccess.EMPTY);
        itemStack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(nbt));
    }

    public static SoulUsingItemHandler get(ItemStack stack) {
        return new SoulUsingItemHandler(stack);
    }
}