package za.co.infernos.goety.api.blocks;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public interface IEnchantedBlock {
    Object2IntMap<Enchantment> getEnchantments();

    default void loadEnchants(CompoundTag p_222787_){
        getEnchantments().clear();
        // TODO (1.21+): enchantments are data-driven and stored as item components, not legacy NBT lists.
    }

    default void saveEnchants(CompoundTag p_222789_, Item item) {
        // TODO (1.21+): write enchantments using the new component-based enchantment format (if still needed).
    }

}
