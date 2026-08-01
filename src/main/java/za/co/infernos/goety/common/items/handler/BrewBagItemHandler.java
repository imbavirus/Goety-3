package za.co.infernos.goety.common.items.handler;

import za.co.infernos.goety.common.items.brew.ThrowableBrewItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BrewBagItemHandler extends ItemStackHandler {
    private final ItemStack itemStack;
    private int slot;

    public BrewBagItemHandler(ItemStack itemStack) {
        super(11);
        this.itemStack = itemStack;
        net.minecraft.world.item.component.CustomData customData = itemStack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
        if (customData != null) {
            deserializeNBT(net.minecraft.core.RegistryAccess.EMPTY, customData.copyTag());
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        CompoundTag nbt = serializeNBT(net.minecraft.core.RegistryAccess.EMPTY);
        itemStack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(nbt));
    }

    public static BrewBagItemHandler get(ItemStack stack) {
        return new BrewBagItemHandler(stack);
    }
}