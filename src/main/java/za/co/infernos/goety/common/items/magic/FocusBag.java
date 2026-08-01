package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.client.inventory.container.FocusBagContainer;
import za.co.infernos.goety.client.inventory.container.FocusBagContainer;
import za.co.infernos.goety.common.items.handler.FocusBagItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FocusBag extends Item implements ICurioItem {
    public FocusBag(){
        super(new Properties()
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1)
        );
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity().isCrouching();
    }

    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide) {
            SimpleMenuProvider provider = new SimpleMenuProvider(
                    (id, inventory, player) -> new FocusBagContainer(id, inventory, FocusBagItemHandler.get(itemstack), itemstack), getName(itemstack));
            playerIn.openMenu(provider);
        }
        return InteractionResultHolder.success(itemstack);
    }

    /**
     * Found Creative Server Bug fix from @mraof's Minestuck Music Player Weapon code.
     */
    private static IItemHandler getItemHandler(ItemStack itemStack) {
        return new FocusBagItemHandler(itemStack, 11);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }
}