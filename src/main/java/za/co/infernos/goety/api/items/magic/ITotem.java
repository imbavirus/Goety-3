package za.co.infernos.goety.api.items.magic;

import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.TotemFinder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public interface ITotem {
    String SOULS_AMOUNT = "Souls";
    String MAX_SOUL_AMOUNT = "Max Souls";
    
    // Lazy evaluation to avoid accessing config before it's loaded
    static int getDefaultMaxSouls() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.MaxSouls, 10000);
    }

    int getMaxSouls();

    default void setTagTick(ItemStack stack){
        CustomData.update(DataComponents.CUSTOM_DATA, stack, compound -> {
            if (!compound.contains(SOULS_AMOUNT)) {
                compound.putInt(SOULS_AMOUNT, 0);
            }
            if (!compound.contains(MAX_SOUL_AMOUNT)) {
                compound.putInt(MAX_SOUL_AMOUNT, this.getMaxSouls());
            }

            int max = compound.getInt(MAX_SOUL_AMOUNT);
            int souls = compound.getInt(SOULS_AMOUNT);
            if (souls > max){
                compound.putInt(SOULS_AMOUNT, max);
            }
            if (souls < 0){
                compound.putInt(SOULS_AMOUNT, 0);
            }
        });
    }

    static boolean isFull(ItemStack itemStack) {
        CustomData data = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (data.isEmpty() || !data.contains(MAX_SOUL_AMOUNT)) {
            return false;
        }
        CompoundTag tag = data.copyTag();
        int Soulcount = tag.getInt(SOULS_AMOUNT);
        int MaxSouls = tag.getInt(MAX_SOUL_AMOUNT);
        return Soulcount == MaxSouls;
    }

    static boolean isEmpty(ItemStack itemStack) {
        CustomData data = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (data.isEmpty()) {
            return true;
        }
        int Soulcount = data.copyTag().getInt(SOULS_AMOUNT);
        return Soulcount == 0;
    }

    static boolean UndyingEffect(Player player){
        ItemStack itemStack = TotemFinder.FindTotem(player);
        if (!itemStack.isEmpty()) {
            CustomData data = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            if (!data.isEmpty() && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.TotemUndying, false)) {
                return data.copyTag().getInt(SOULS_AMOUNT) == ITotem.getDefaultMaxSouls();
            }
        }
        return false;
    }

    static int currentSouls(ItemStack itemStack){
        CustomData data = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.isEmpty() ? 0 : data.copyTag().getInt(SOULS_AMOUNT);
    }

    static int maximumSouls(ItemStack itemStack){
        CustomData data = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.isEmpty() ? 0 : data.copyTag().getInt(MAX_SOUL_AMOUNT);
    }

    static void setSoulsamount(ItemStack itemStack, int souls){
        if (!(itemStack.getItem() instanceof ITotem)) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.putInt(SOULS_AMOUNT, souls));
    }

    static void setMaxSoulAmount(ItemStack itemStack, int souls){
        if (!(itemStack.getItem() instanceof ITotem)) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.putInt(MAX_SOUL_AMOUNT, souls));
    }

    static void increaseSouls(ItemStack itemStack, int souls) {
        if (!(itemStack.getItem() instanceof ITotem totem)) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> {
            int max = tag.contains(MAX_SOUL_AMOUNT) ? tag.getInt(MAX_SOUL_AMOUNT) : totem.getMaxSouls();
            tag.putInt(MAX_SOUL_AMOUNT, max);
            int current = tag.getInt(SOULS_AMOUNT);
            if (current < max) {
                tag.putInt(SOULS_AMOUNT, Math.min(current + souls, max));
            }
        });
    }

    static void decreaseSouls(ItemStack itemStack, int souls) {
        if (!(itemStack.getItem() instanceof ITotem)) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> {
            int current = tag.getInt(SOULS_AMOUNT);
            if (current > 0) {
                tag.putInt(SOULS_AMOUNT, Math.max(current - souls, 0));
            }
        });
    }
}