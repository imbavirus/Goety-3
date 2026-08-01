package za.co.infernos.goety.common.items;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public class ModSpawnEggItem extends SpawnEggItem {

    public ModSpawnEggItem(final Supplier<? extends EntityType<? extends Mob>> entityTypeSupplier, int primaryColorIn, int secondaryColorIn, Item.Properties builder) {
        super(entityTypeSupplier.get(), primaryColorIn, secondaryColorIn, builder);
    }
}