package za.co.infernos.goety.common.items.magic;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class TroopFocus extends Item {
    private static final String TAG_SUMMON_TYPE = "TroopSummonType";

    public TroopFocus() {
        super(new Item.Properties().stacksTo(1));
    }

    public static boolean hasSummonType(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(TAG_SUMMON_TYPE);
    }

    public static void setSummonType(CompoundTag compoundTag, EntityType<?> entityType) {
        if (compoundTag != null && entityType != null) {
            ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            if (key != null) {
                compoundTag.putString(TAG_SUMMON_TYPE, key.toString());
            }
        }
    }

    public static void setSummonType(ItemStack stack, EntityType<?> entityType) {
        if (stack != null && entityType != null) {
            ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            if (key != null) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(TAG_SUMMON_TYPE, key.toString()));
            }
        }
    }

    public static EntityType<?> getSummonType(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains(TAG_SUMMON_TYPE)) {
            return null;
        }
        ResourceLocation loc = ResourceLocation.tryParse(tag.getString(TAG_SUMMON_TYPE));
        return loc == null ? null : BuiltInRegistries.ENTITY_TYPE.get(loc);
    }

    public static void call(Player player, ItemStack stack) {
        // Server-side resolution belongs to the spell logic; this stays a no-op.
    }
}
