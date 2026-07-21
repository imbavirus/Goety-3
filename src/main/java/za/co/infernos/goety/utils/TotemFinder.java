package za.co.infernos.goety.utils;

import za.co.infernos.goety.api.items.magic.IFocus;
import za.co.infernos.goety.api.items.magic.ITotem;
import za.co.infernos.goety.common.items.handler.FocusBagItemHandler;
import za.co.infernos.goety.common.items.magic.FocusBag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TotemFinder {

    private static boolean isFocusBag(ItemStack itemStack) {
        return itemStack.getItem() instanceof FocusBag;
    }

    public static ItemStack findBag(Player playerEntity) {
        ItemStack foundStack = ItemStack.EMPTY;
        for (int i = 0; i < playerEntity.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = playerEntity.getInventory().getItem(i);
            if (!itemStack.isEmpty() && isFocusBag(itemStack)) {
                foundStack = itemStack;
                break;
            }
        }
        if (foundStack.isEmpty()) {
            ItemStack curioStack = CuriosFinder.findCurio(playerEntity, TotemFinder::isFocusBag);
            if (!curioStack.isEmpty()) {
                foundStack = curioStack;
            }
        }
        return foundStack;
    }

    public static ItemStack findFocusInBag(Player player){
        ItemStack foundStack = ItemStack.EMPTY;
        if (!findBag(player).isEmpty()){
            FocusBagItemHandler focusBagItemHandler = FocusBagItemHandler.get(findBag(player));
            for (int i = 1; i < focusBagItemHandler.getSlots(); ++i){
                ItemStack itemStack = focusBagItemHandler.getStackInSlot(i);
                if (itemStack.getItem() instanceof IFocus){
                    foundStack = itemStack;
                }
            }
        }
        return foundStack;
    }

    public static int getFocusBagTotal(Player player){
        int num = 0;
        if (!findBag(player).isEmpty()){
            FocusBagItemHandler focusBagItemHandler = FocusBagItemHandler.get(findBag(player));
            for (int i = 1; i < focusBagItemHandler.getSlots(); ++i){
                ItemStack itemStack = focusBagItemHandler.getStackInSlot(i);
                if (itemStack.getItem() instanceof IFocus){
                    ++num;
                }
            }
        }
        return num;
    }

    public static boolean hasEmptyBagSpace(Player player){
        int total = 10;
        if (!findBag(player).isEmpty()){
            FocusBagItemHandler focusBagItemHandler = FocusBagItemHandler.get(findBag(player));
            total = focusBagItemHandler.getSlots();
        }
        return getFocusBagTotal(player) < total;
    }

    public static boolean hasFocusInBag(Player player){
        return !findFocusInBag(player).isEmpty();
    }

    public static boolean canOpenWandCircle(Player player){
        return hasFocusInBag(player) || WandUtil.hasFocusInInv(player) || !WandUtil.findFocus(player).isEmpty();
    }

    private static boolean isTotem(ItemStack itemStack) {
        return itemStack.getItem() instanceof ITotem;
    }

    public static ItemStack FindTotem(Player playerEntity){
        ItemStack foundStack = ItemStack.EMPTY;
        if (isTotem(playerEntity.getOffhandItem())){
            foundStack = playerEntity.getOffhandItem();
        } else {
            for (int i = 0; i <= 9; i++) {
                ItemStack itemStack = playerEntity.getInventory().getItem(i);
                if (!itemStack.isEmpty() && isTotem(itemStack)) {
                    foundStack = itemStack;
                    break;
                }
            }
        }
        if (foundStack.isEmpty()) {
            ItemStack curioStack = CuriosFinder.findCurio(playerEntity, TotemFinder::isTotem);
            if (!curioStack.isEmpty()) {
                foundStack = curioStack;
            }
        }
        return foundStack;
    }
}