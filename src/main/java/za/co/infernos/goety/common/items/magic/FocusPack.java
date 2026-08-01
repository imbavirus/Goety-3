package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.client.inventory.container.FocusPackContainer;
import za.co.infernos.goety.client.inventory.container.FocusBagContainer;
import za.co.infernos.goety.common.items.handler.FocusBagItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FocusPack extends FocusBag {

    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide) {
            SimpleMenuProvider provider = new SimpleMenuProvider(
                    (id, inventory, player) -> new FocusPackContainer(id, inventory, FocusBagItemHandler.get(itemstack), itemstack), getName(itemstack));
            playerIn.openMenu(provider);
        }
        return InteractionResultHolder.success(itemstack);
    }
}