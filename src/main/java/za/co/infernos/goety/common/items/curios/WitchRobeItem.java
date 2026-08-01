package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.common.inventory.ModSaveInventory;
import za.co.infernos.goety.common.inventory.WitchRobeInventory;
import za.co.infernos.goety.utils.CuriosFinder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class WitchRobeItem extends SingleStackItem {
    public static String INVENTORY = "WITCH_ROBE_BREW";

    public static int getOrCreateInventoryId(ItemStack stack) {
        ModSaveInventory saveInventory = ModSaveInventory.getInstance();
        if (saveInventory == null) {
            return -1;
        }

        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains(INVENTORY)) {
            int inventoryId = saveInventory.addAndCreateWitchRobe();
            CustomData.update(DataComponents.CUSTOM_DATA, stack, customTag -> customTag.putInt(INVENTORY, inventoryId));
            return inventoryId;
        }
        return tag.getInt(INVENTORY);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity livingEntity) {
            if (ModSaveInventory.getInstance() != null) {
                int inventoryId = getOrCreateInventoryId(stack);
                if (inventoryId < 0) {
                    return;
                }
                WitchRobeInventory inventory = ModSaveInventory.getInstance().getWitchRobeInventory(inventoryId, livingEntity);

                if (!worldIn.isClientSide) {
                    if (CuriosFinder.hasWitchHat(livingEntity)) {
                        inventory.setIncreaseSpeed(1);
                    } else {
                        inventory.setIncreaseSpeed(0);
                    }

                    inventory.tick();
                }
            }
        }
    }
}