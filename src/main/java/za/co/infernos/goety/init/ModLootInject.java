package za.co.infernos.goety.init;

import za.co.infernos.goety.Goety;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.List;

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModLootInject {

    private static final List<String> CHEST_TABLES = List.of("abandoned_mineshaft", "ancient_city", "ancient_city_ice_box", "desert_pyramid", "jungle_temple", "nether_bridge", "pillager_outpost", "simple_dungeon", "stronghold_crossing", "stronghold_library", "woodland_mansion");

    private static final List<String> ENTITY_TABLES = List.of("cave_spider", "frog", "ravager", "spider", "witch", "zoglin");

    @SubscribeEvent
    public static void InjectLootTables(LootTableLoadEvent evt) {
        String name = evt.getName().toString();
        
        // Debug logging for wraith loot table and any goety loot tables
        if (name.startsWith("goety:")) {
            Goety.LOGGER.info("[LOOT DEBUG] LootTableLoadEvent fired for: {}", name);
            Goety.LOGGER.info("[LOOT DEBUG] Loot table is EMPTY: {}", evt.getTable() == net.minecraft.world.level.storage.loot.LootTable.EMPTY);
        }
        
        String chestsPrefix = "minecraft:chests/";
        String entitiesPrefix = "minecraft:entities/";

        if ((name.startsWith(chestsPrefix) && CHEST_TABLES.contains(name.substring(chestsPrefix.length())))
                || (name.startsWith(entitiesPrefix) && ENTITY_TABLES.contains(name.substring(entitiesPrefix.length())))) {
            String file = name.substring("minecraft:".length());
            evt.getTable().addPool(getInjectPool(file));
        }
    }

    private static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool().add(getInjectEntry(entryName)).name("goety_inject_pool").build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
        return LootTableReference.lootTableReference(Goety.location("inject/" + name));
    }
}