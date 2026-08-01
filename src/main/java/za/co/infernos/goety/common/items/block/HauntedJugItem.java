package za.co.infernos.goety.common.items.block;

import za.co.infernos.goety.common.blocks.ModBlocks;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import za.co.infernos.goety.utils.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class HauntedJugItem extends BlockItemBase {

    public HauntedJugItem() {
        super(ModBlocks.HAUNTED_JUG.get());
    }

    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack other = slot.getItem();
            if (other.is(Items.BUCKET)) {
                if (other.getCount() == 1) {
                    slot.set(new ItemStack(Items.WATER_BUCKET));
                } else {
                    other.shrink(1);
                    player.getInventory().placeItemBackInInventory(new ItemStack(Items.WATER_BUCKET));
                }

                return true;
            } else if (other.is(Items.GLASS_BOTTLE)) {
                if (other.getCount() == 1) {
                    slot.set(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                } else {
                    other.shrink(1);
                    player.getInventory().placeItemBackInInventory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY) {
            return false;
        } else {
            if (other.is(Items.BUCKET)) {
                if (other.getCount() == 1) {
                    access.set(new ItemStack(Items.WATER_BUCKET));
                } else {
                    other.shrink(1);
                    player.getInventory().placeItemBackInInventory(new ItemStack(Items.WATER_BUCKET));
                }

                return true;
            } else if (other.is(Items.GLASS_BOTTLE)) {
                if (other.getCount() == 1) {
                    access.set(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                } else {
                    other.shrink(1);
                    player.getInventory().placeItemBackInInventory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                }

                return true;
            } else {
                return false;
            }
        }
    }
}