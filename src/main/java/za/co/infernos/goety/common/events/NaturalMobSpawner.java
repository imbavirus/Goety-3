package za.co.infernos.goety.common.events;

import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.AABB;

import java.util.Random;

public class NaturalMobSpawner {
    private int nextTick;

    public int tick(ServerLevel pLevel) {
        RandomSource random = pLevel.random;
        --this.nextTick;
        if (this.nextTick > 0) {
            return 0;
        } else {
            // Check every 200 ticks (10 seconds) for spawn opportunities
            this.nextTick = 200;
            
            if (!pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                return 0;
            }
            
            int playerCount = pLevel.players().size();
            if (playerCount < 1) {
                return 0;
            }
            
            // Pick a random player to check around
            ServerPlayer player = pLevel.players().get(random.nextInt(playerCount));
            if (player.isSpectator() || player.isCreative()) {
                return 0;
            }
            
            BlockPos playerPos = player.blockPosition();
            var biome = pLevel.getBiome(playerPos);
            
            // Check for Wraith spawning in Soul Sand Valley, tagged biomes, or Overworld at night
            boolean canSpawnWraith = false;
            if (biome.is(Biomes.SOUL_SAND_VALLEY)) {
                // Always allow in Soul Sand Valley
                canSpawnWraith = true;
            } else if (biome.is(ModTags.Biomes.WRAITH_SPAWN) && !biome.is(ModTags.Biomes.WRAITH_EXCLUDE_SPAWN)) {
                // Tagged biomes
                canSpawnWraith = true;
            } else if (pLevel.dimensionType().natural()) {
                // Overworld - check if it's night (light level check will be done by spawn predicate)
                canSpawnWraith = true;
            }
            
            if (canSpawnWraith) {
                // Check density - don't spawn if there are too many wraiths nearby
                AABB searchBox = new AABB(playerPos).inflate(48.0D);
                int nearbyWraiths = countEntitiesByType(pLevel, ModEntityType.WRAITH.get(), searchBox);
                
                // Limit to max 3-5 wraiths within 48 blocks of player
                int maxNearbyWraiths = 4;
                if (nearbyWraiths >= maxNearbyWraiths) {
                    return 0;
                }
                
                // Use weight more conservatively - divide by 5 to get a reasonable spawn chance
                // Weight of 20 = 4% chance per check (every 10 seconds)
                int wraithWeight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WraithSpawnWeight, 20);
                int spawnChance = Math.max(1, wraithWeight / 5); // Convert weight to percentage (20 -> 4%)
                if (wraithWeight > 0 && random.nextInt(100) < spawnChance) {
                    if (trySpawnWraith(pLevel, player, playerPos)) {
                        return 1;
                    }
                }
            }
            
            // Check for Reaper spawning in Soul Sand Valley or tagged biomes
            boolean canSpawnReaper = false;
            if (biome.is(Biomes.SOUL_SAND_VALLEY)) {
                canSpawnReaper = true;
            } else if (biome.is(ModTags.Biomes.REAPER_SPAWN) && !biome.is(ModTags.Biomes.REAPER_EXCLUDE_SPAWN)) {
                canSpawnReaper = true;
            }
            
            if (canSpawnReaper) {
                // Check density - don't spawn if there are too many reapers nearby
                AABB searchBox = new AABB(playerPos).inflate(48.0D);
                int nearbyReapers = countEntitiesByType(pLevel, ModEntityType.REAPER.get(), searchBox);
                int maxNearbyReapers = 4;
                if (nearbyReapers < maxNearbyReapers) {
                    int reaperWeight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.ReaperSpawnWeight, 20);
                    int spawnChance = Math.max(1, reaperWeight / 5); // Convert weight to percentage (20 -> 4%)
                    if (reaperWeight > 0 && random.nextInt(100) < spawnChance) {
                        if (trySpawnReaper(pLevel, player, playerPos)) {
                            return 1;
                        }
                    }
                }
            }
            
            // Check for other mobs based on biome tags
            if (!biome.is(ModTags.Biomes.COMMON_BLACKLIST)) {
                // Muck Wraith
                if (biome.is(ModTags.Biomes.MUCK_WRAITH_SPAWN) && !biome.is(ModTags.Biomes.MUCK_WRAITH_EXCLUDE_SPAWN)) {
                    AABB searchBox = new AABB(playerPos).inflate(48.0D);
                    int nearbyMuckWraiths = countEntitiesByType(pLevel, ModEntityType.MUCK_WRAITH.get(), searchBox);
                    int maxNearbyMuckWraiths = 4;
                    if (nearbyMuckWraiths < maxNearbyMuckWraiths) {
                        int weight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MuckWraithSpawnWeight, 20);
                        int spawnChance = Math.max(1, weight / 5);
                        if (weight > 0 && random.nextInt(100) < spawnChance) {
                            if (trySpawnMuckWraith(pLevel, player, playerPos)) {
                                return 1;
                            }
                        }
                    }
                }
                
                // Web Spider
                if (biome.is(ModTags.Biomes.WEB_SPIDER_SPAWN) && !biome.is(ModTags.Biomes.WEB_SPIDER_EXCLUDE_SPAWN)) {
                    AABB searchBox = new AABB(playerPos).inflate(48.0D);
                    int nearbyWebSpiders = countEntitiesByType(pLevel, ModEntityType.WEB_SPIDER.get(), searchBox);
                    int maxNearbyWebSpiders = 4;
                    if (nearbyWebSpiders < maxNearbyWebSpiders) {
                        int weight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WebSpiderSpawnWeight, 20);
                        int spawnChance = Math.max(1, weight / 5);
                        if (weight > 0 && random.nextInt(100) < spawnChance) {
                            if (trySpawnWebSpider(pLevel, player, playerPos)) {
                                return 1;
                            }
                        }
                    }
                }
                
                // Icy Spider
                if (biome.is(ModTags.Biomes.ICY_SPIDER_SPAWN) && !biome.is(ModTags.Biomes.ICY_SPIDER_EXCLUDE_SPAWN)) {
                    AABB searchBox = new AABB(playerPos).inflate(48.0D);
                    int nearbyIcySpiders = countEntitiesByType(pLevel, ModEntityType.ICY_SPIDER.get(), searchBox);
                    int maxNearbyIcySpiders = 4;
                    if (nearbyIcySpiders < maxNearbyIcySpiders) {
                        int weight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.IcySpiderSpawnWeight, 20);
                        int spawnChance = Math.max(1, weight / 5);
                        if (weight > 0 && random.nextInt(100) < spawnChance) {
                            if (trySpawnIcySpider(pLevel, player, playerPos)) {
                                return 1;
                            }
                        }
                    }
                }
                
                // Necromancer
                if (biome.is(ModTags.Biomes.NECROMANCER_SPAWN) && !biome.is(ModTags.Biomes.NECROMANCER_EXCLUDE_SPAWN)) {
                    AABB searchBox = new AABB(playerPos).inflate(48.0D);
                    int nearbyNecromancers = countEntitiesByType(pLevel, ModEntityType.NECROMANCER.get(), searchBox);
                    int maxNearbyNecromancers = 4;
                    if (nearbyNecromancers < maxNearbyNecromancers) {
                        int weight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.NecromancerSpawnWeight, 20);
                        int spawnChance = Math.max(1, weight / 5);
                        if (weight > 0 && random.nextInt(100) < spawnChance) {
                            if (trySpawnNecromancer(pLevel, player, playerPos)) {
                                return 1;
                            }
                        }
                    }
                }
                
                // Warlock
                if (biome.is(ModTags.Biomes.WARLOCK_SPAWN) && !biome.is(ModTags.Biomes.WARLOCK_EXCLUDE_SPAWN)) {
                    AABB searchBox = new AABB(playerPos).inflate(48.0D);
                    int nearbyWarlocks = countEntitiesByType(pLevel, ModEntityType.WARLOCK.get(), searchBox);
                    int maxNearbyWarlocks = 4;
                    if (nearbyWarlocks < maxNearbyWarlocks) {
                        int weight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WarlockSpawnWeight, 20);
                        int spawnChance = Math.max(1, weight / 5);
                        if (weight > 0 && random.nextInt(100) < spawnChance) {
                            if (trySpawnWarlock(pLevel, player, playerPos)) {
                                return 1;
                            }
                        }
                    }
                }
                
                // Heretic
                if (biome.is(ModTags.Biomes.HERETIC_SPAWN) && !biome.is(ModTags.Biomes.HERETIC_EXCLUDE_SPAWN)) {
                    AABB searchBox = new AABB(playerPos).inflate(48.0D);
                    int nearbyHeretics = countEntitiesByType(pLevel, ModEntityType.HERETIC.get(), searchBox);
                    int maxNearbyHeretics = 4;
                    if (nearbyHeretics < maxNearbyHeretics) {
                        int weight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.HereticSpawnWeight, 20);
                        int spawnChance = Math.max(1, weight / 5);
                        if (weight > 0 && random.nextInt(100) < spawnChance) {
                            if (trySpawnHeretic(pLevel, player, playerPos)) {
                                return 1;
                            }
                        }
                    }
                }
                
                // Maverick
                if (biome.is(ModTags.Biomes.MAVERICK_SPAWN) && !biome.is(ModTags.Biomes.MAVERICK_EXCLUDE_SPAWN)) {
                    AABB searchBox = new AABB(playerPos).inflate(48.0D);
                    int nearbyMavericks = countEntitiesByType(pLevel, ModEntityType.MAVERICK.get(), searchBox);
                    int maxNearbyMavericks = 4;
                    if (nearbyMavericks < maxNearbyMavericks) {
                        int weight = za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.MaverickSpawnWeight, 20);
                        int spawnChance = Math.max(1, weight / 5);
                        if (weight > 0 && random.nextInt(100) < spawnChance) {
                            if (trySpawnMaverick(pLevel, player, playerPos)) {
                                return 1;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }
    
    private boolean trySpawnWraith(ServerLevel serverLevel, ServerPlayer player, BlockPos playerPos) {
        Mob mob = ModEntityType.WRAITH.get().create(serverLevel);
        if (mob == null) return false;
        return trySpawnMob(serverLevel, player, mob, playerPos, 24.0D);
    }
    
    private boolean trySpawnReaper(ServerLevel serverLevel, ServerPlayer player, BlockPos playerPos) {
        Mob mob = ModEntityType.REAPER.get().create(serverLevel);
        if (mob == null) return false;
        return trySpawnMob(serverLevel, player, mob, playerPos, 24.0D);
    }
    
    private boolean trySpawnMuckWraith(ServerLevel serverLevel, ServerPlayer player, BlockPos playerPos) {
        Mob mob = ModEntityType.MUCK_WRAITH.get().create(serverLevel);
        if (mob == null) return false;
        return trySpawnMob(serverLevel, player, mob, playerPos, 24.0D);
    }
    
    private boolean trySpawnWebSpider(ServerLevel serverLevel, ServerPlayer player, BlockPos playerPos) {
        Mob mob = ModEntityType.WEB_SPIDER.get().create(serverLevel);
        if (mob == null) return false;
        return trySpawnMob(serverLevel, player, mob, playerPos, 24.0D);
    }
    
    private boolean trySpawnIcySpider(ServerLevel serverLevel, ServerPlayer player, BlockPos playerPos) {
        Mob mob = ModEntityType.ICY_SPIDER.get().create(serverLevel);
        if (mob == null) return false;
        return trySpawnMob(serverLevel, player, mob, playerPos, 24.0D);
    }
    
    private boolean trySpawnNecromancer(ServerLevel serverLevel, ServerPlayer player, BlockPos playerPos) {
        Mob mob = ModEntityType.NECROMANCER.get().create(serverLevel);
        if (mob == null) return false;
        return trySpawnMob(serverLevel, player, mob, playerPos, 24.0D);
    }
    
    private boolean trySpawnWarlock(ServerLevel serverLevel, ServerPlayer player, BlockPos playerPos) {
        Mob mob = ModEntityType.WARLOCK.get().create(serverLevel);
        if (mob == null) return false;
        return trySpawnMob(serverLevel, player, mob, playerPos, 24.0D);
    }
    
    private boolean trySpawnHeretic(ServerLevel serverLevel, ServerPlayer player, BlockPos playerPos) {
        Mob mob = ModEntityType.HERETIC.get().create(serverLevel);
        if (mob == null) return false;
        return trySpawnMob(serverLevel, player, mob, playerPos, 24.0D);
    }
    
    private boolean trySpawnMaverick(ServerLevel serverLevel, ServerPlayer player, BlockPos playerPos) {
        Mob mob = ModEntityType.MAVERICK.get().create(serverLevel);
        if (mob == null) return false;
        return trySpawnMob(serverLevel, player, mob, playerPos, 24.0D);
    }
    
    private boolean trySpawnMob(ServerLevel serverLevel, ServerPlayer player, Mob mob, BlockPos playerPos, double radius) {
        Random rand = new Random();
        // Try to find a valid spawn position near the player
        for (int i = 0; i < 16; ++i) {
            double angle = rand.nextDouble() * Math.PI * 2.0;
            double distance = 8.0 + rand.nextDouble() * (radius - 8.0);
            double x = playerPos.getX() + Math.cos(angle) * distance;
            double z = playerPos.getZ() + Math.sin(angle) * distance;
            double y = playerPos.getY() + (rand.nextDouble() - 0.5) * 8.0;
            
            BlockPos spawnPos = BlockPos.containing(x, y, z);
            
            // Check if position is valid for spawning
            if (!serverLevel.isLoaded(spawnPos) || !serverLevel.getWorldBorder().isWithinBounds(spawnPos)) {
                continue;
            }
            
            // Check spawn predicate using SpawnPlacements first
            if (!net.minecraft.world.entity.SpawnPlacements.checkSpawnRules(mob.getType(), serverLevel, MobSpawnType.NATURAL, spawnPos, serverLevel.random)) {
                continue;
            }
            
            // Set position before checking obstruction
            mob.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
            
            // Check collision and obstruction
            if (!serverLevel.noCollision(mob) || !mob.checkSpawnObstruction(serverLevel)) {
                continue;
            }
            
            // Finalize spawn and add to world
            mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(spawnPos), MobSpawnType.NATURAL, null);
            
            if (serverLevel.addFreshEntity(mob)) {
                za.co.infernos.goety.Goety.LOGGER.debug("NaturalMobSpawner: Spawned {} at {}", mob.getType().getDescription().getString(), spawnPos);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Count entities of a specific type within a bounding box.
     * Uses EntityType instead of class to avoid class loading issues.
     * Optimized to stop counting once we exceed the limit.
     */
    private int countEntitiesByType(ServerLevel level, EntityType<?> entityType, AABB searchBox) {
        int count = 0;
        // Early exit optimization - stop counting once we exceed typical limits
        for (Entity entity : level.getEntitiesOfClass(Entity.class, searchBox)) {
            if (entity.isAlive() && entity.getType() == entityType) {
                count++;
                // Early exit if we've exceeded the typical max (4)
                if (count >= 4) {
                    break;
                }
            }
        }
        return count;
    }
}
