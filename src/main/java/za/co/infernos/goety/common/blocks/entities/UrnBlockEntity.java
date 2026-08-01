package za.co.infernos.goety.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UrnBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    public UrnBlockEntity(BlockPos p_155630_, BlockState p_155631_) {
        super(ModBlockEntities.URN.get(), p_155630_, p_155631_);
    }

    protected void saveAdditional(CompoundTag p_187459_, HolderLookup.Provider p_323635_) {
        super.saveAdditional(p_187459_, p_323635_);
        if (!this.trySaveLootTable(p_187459_)) {
            ContainerHelper.saveAllItems(p_187459_, this.items, p_323635_);
        }
    }

    public void loadAdditional(CompoundTag p_155055_, HolderLookup.Provider p_324471_) {
        super.loadAdditional(p_155055_, p_324471_);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(p_155055_)) {
            ContainerHelper.loadAllItems(p_155055_, this.items, p_324471_);
        }
    }

    public int getContainerSize() {
        return 27;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void setItems(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }

    protected Component getDefaultName() {
        return Component.translatable("container.goety.urn");
    }

    @Override
    public boolean canOpen(Player p_59643_) {
        return false;
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
        return null;
    }

    private Component name;

    public void setCustomName(Component p_59639_) {
        this.name = p_59639_;
    }

    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    public Component getDisplayName() {
        return this.getName();
    }

    @javax.annotation.Nullable
    public Component getCustomName() {
        return this.name;
    }
}