package za.co.infernos.goety.common.items;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.ModEntityType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import za.co.infernos.goety.compat.fml.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ServantSpawnEggs {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Goety.MOD_ID);

    public static void init() {
        ServantSpawnEggs.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final DeferredHolder<Item, ServantSpawnEggItem> ZOMBIE_SERVANT_SPAWN_EGG = ITEMS.register("zombie_servant_spawn_egg",
            () -> new ServantSpawnEggItem(ModEntityType.ZOMBIE_SERVANT, 0x192927, 0x737885, egg()));

    public static Item.Properties egg() {
        return new Item.Properties();
    }
}
