package za.co.infernos.goety.common.items.handler;

import za.co.infernos.goety.api.items.magic.IFocus;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import net.minecraft.core.HolderLookup;

public class FocusBagItemHandler extends ItemStackHandler {
    private final ItemStack itemStack;
    private final int size;
    private int slot;

    public FocusBagItemHandler(ItemStack itemStack, int size) {
        super(size);
        this.size = size;
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
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof IFocus;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.size;
    }

    public NonNullList<ItemStack> getContents() {
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
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            if (nbt.contains("slot")) {
                slot = nbt.getInt("slot");
                stacks.set(slot, ItemStack.parse(provider, itemTags).orElse(ItemStack.EMPTY));
            }
        }
        onLoad();

    }

    @Override
    protected void onContentsChanged(int slot) {
        CompoundTag nbt = serializeNBT(net.minecraft.core.RegistryAccess.EMPTY);
        itemStack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(nbt));
    }

    public static FocusBagItemHandler get(ItemStack stack) {
        return new FocusBagItemHandler(stack, 11); // Assuming size 11 from FocusBag.java usage
    }
}