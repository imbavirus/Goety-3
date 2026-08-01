package za.co.infernos.goety.common.world;

import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.init.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.common.world.ModifiableStructureInfo;

public class ModLevelRegistry {

    public static void addBiomeSpawns(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (!biome.is(ModTags.Biomes.COMMON_BLACKLIST) && !biome.is(biomeResourceKey -> biomeResourceKey.registry().getNamespace().contains("alexscaves"))){
            // Use default values from MobsConfig (20 for weights, 1 for counts)
            if (biome.is(ModTags.Biomes.REAPER_SPAWN) && !biome.is(ModTags.Biomes.REAPER_EXCLUDE_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.ReaperSpawnWeight, 20) > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.REAPER.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.ReaperSpawnWeight, 20), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.ReaperSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.ReaperSpawnMaxCount, 1)));
            }
            if (biome.is(ModTags.Biomes.WRAITH_SPAWN) && !biome.is(ModTags.Biomes.WRAITH_EXCLUDE_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WraithSpawnWeight, 20) > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.WRAITH.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WraithSpawnWeight, 20), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WraithSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WraithSpawnMaxCount, 1)));
            }
            if (biome.is(ModTags.Biomes.MUCK_WRAITH_SPAWN) && !biome.is(ModTags.Biomes.MUCK_WRAITH_EXCLUDE_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MuckWraithSpawnWeight, 20) > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.MUCK_WRAITH.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MuckWraithSpawnWeight, 20), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MuckWraithSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MuckWraithSpawnMaxCount, 1)));
            }
            if (biome.is(ModTags.Biomes.WEB_SPIDER_SPAWN) && !biome.is(ModTags.Biomes.WEB_SPIDER_EXCLUDE_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WebSpiderSpawnWeight, 20) > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.WEB_SPIDER.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WebSpiderSpawnWeight, 20), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WebSpiderSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WebSpiderSpawnMaxCount, 1)));
            }
            if (biome.is(ModTags.Biomes.ICY_SPIDER_SPAWN) && !biome.is(ModTags.Biomes.ICY_SPIDER_EXCLUDE_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.IcySpiderSpawnWeight, 20) > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.ICY_SPIDER.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.IcySpiderSpawnWeight, 20), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.IcySpiderSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.IcySpiderSpawnMaxCount, 1)));
            }
            if (biome.is(ModTags.Biomes.NECROMANCER_SPAWN) && !biome.is(ModTags.Biomes.NECROMANCER_EXCLUDE_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.NecromancerSpawnWeight, 20) > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.NECROMANCER.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.NecromancerSpawnWeight, 20), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.NecromancerSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.NecromancerSpawnMaxCount, 1)));
            }
            if (biome.is(ModTags.Biomes.WARLOCK_SPAWN) && !biome.is(ModTags.Biomes.WARLOCK_EXCLUDE_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WarlockSpawnWeight, 20) > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.WARLOCK.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WarlockSpawnWeight, 20), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WarlockSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WarlockSpawnMaxCount, 1)));
            }
            if (biome.is(ModTags.Biomes.HERETIC_SPAWN) && !biome.is(ModTags.Biomes.HERETIC_EXCLUDE_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.HereticSpawnWeight, 20) > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.HERETIC.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.HereticSpawnWeight, 20), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.HereticSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.HereticSpawnMaxCount, 1)));
            }
            if (biome.is(ModTags.Biomes.MAVERICK_SPAWN) && !biome.is(ModTags.Biomes.MAVERICK_EXCLUDE_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MaverickSpawnWeight, 20) > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.MAVERICK.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MaverickSpawnWeight, 20), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MaverickSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MaverickSpawnMaxCount, 1)));
            }
        }
        // Hardcoded Soul Sand Valley spawns (always applies, regardless of tags)
        if (biome.is(Biomes.SOUL_SAND_VALLEY)){
            int reaperWeight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.ReaperSpawnWeight, 20);
            int wraithWeight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WraithSpawnWeight, 20);
            
            za.co.infernos.goety.Goety.LOGGER.info("ModLevelRegistry: Processing Soul Sand Valley biome - Wraith weight: {}, Reaper weight: {}", wraithWeight, reaperWeight);
            
            // Always add wraiths to Soul Sand Valley with default weight of 20 if config isn't loaded
            if (wraithWeight > 0) {
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.WRAITH.get(), wraithWeight, za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WraithSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WraithSpawnMaxCount, 1)));
                builder.getMobSpawnSettings().addMobCharge(ModEntityType.WRAITH.get(), 0.7D, 0.15D);
                za.co.infernos.goety.Goety.LOGGER.info("ModLevelRegistry: Added Wraith spawn to Soul Sand Valley with weight {}", wraithWeight);
            }
            if (reaperWeight > 0) {
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ModEntityType.REAPER.get(), reaperWeight, za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.ReaperSpawnMinCount, 1), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.ReaperSpawnMaxCount, 1)));
                builder.getMobSpawnSettings().addMobCharge(ModEntityType.REAPER.get(), 0.7D, 0.15D);
            }
        }
    }

    public static boolean startName(ResourceKey<Biome> biomeResourceKey, String string){
        return biomeResourceKey.registry().getNamespace().startsWith(string);
    }

    public static boolean containsName(ResourceKey<Biome> biomeResourceKey, String string){
        return biomeResourceKey.registry().getNamespace().contains(string);
    }

    public static void addStructureSpawns(Holder<Structure> structure, ModifiableStructureInfo.StructureInfo.Builder builder) {
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.NecromancerSpawnStructure, false) && structure.is(ModTags.Structures.NECROMANCER_SPAWN) && za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.NecromancerSpawnWeight, 0) > 0) {
            builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.MONSTER).addSpawn(new MobSpawnSettings.SpawnerData(ModEntityType.NECROMANCER.get(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.NecromancerSpawnWeight, 0), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.NecromancerSpawnMinCount, 0), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.NecromancerSpawnMaxCount, 0)));
        }
    }
}