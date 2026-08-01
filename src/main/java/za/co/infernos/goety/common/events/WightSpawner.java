package za.co.infernos.goety.common.events;

import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.hostile.Wight;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

import java.util.Random;

public class WightSpawner {
    private int nextTick;

    public int tick(ServerLevel pLevel) {
        if (!za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.WightSpawn, false)) {
            return 0;
        } else {
            RandomSource random = pLevel.random;
            --this.nextTick;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick += za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WightSpawnFreq, 0);
                if (random.nextInt(za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WightSpawnChance, 0)) != 0) {
                    return 0;
                } else {
                    int j = pLevel.players().size();
                    if (j < 1) {
                        return 0;
                    } else if (!pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                        return 0;
                    } else {
                        ServerPlayer pPlayer = pLevel.players().get(random.nextInt(j));
                        float rawPercent = (float) SEHelper.getSoulAmountInt(pPlayer) / za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.MaxArcaSouls, 0);
                        int sePercent = (int) (rawPercent * 100);
                        if (pPlayer.isSpectator() || pPlayer.isCreative()) {
                            return 0;
                        } else if (!pLevel.getBiome(pPlayer.blockPosition()).is(ModTags.Biomes.WIGHT_SPAWN)) {
                            return 0;
                        } else if (!pLevel.getEntitiesOfClass(LivingEntity.class,
                                pPlayer.getBoundingBox().inflate(64.0D),
                                entity -> entity.getType().is(Tags.EntityTypes.BOSSES)
                                    || entity.getType().is(ModTags.EntityTypes.MINI_BOSSES)).isEmpty()) {
                            return 0;
                        } else if (sePercent >= 9 && sePercent < 20) {
                            summonWight(pLevel, pPlayer, sePercent);
                            this.nextTick += za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.WightSpawnFreq, 0);
                            return 1;
                        } else if (sePercent >= 20 && sePercent < 90) {
                            summonWight(pLevel, pPlayer, sePercent);
                            return 1;
                        } else if (sePercent >= 90){
                            int extra = random.nextBoolean() ? 1 : 0;
                            for (int i = 0; i < extra + 1; ++i) {
                                summonWight(pLevel, pPlayer, sePercent);
                            }
                            return 1;
                        }
                    }
                }
            }
        }
        return 0;
    }

    public static boolean summonWight(ServerLevel serverLevel, Player player, int sePercent){
        Wight wight = new Wight(ModEntityType.WIGHT.get(), serverLevel);
        Random rand = new Random();
        for (int i = 0; i < 16; ++i) {
            Vec3 vec3 = BlockFinder.getRandomSpawnBehindDirection(serverLevel, rand, player.position(), player.getLookAngle());
            BlockPos blockPos = BlockPos.containing(vec3);
            if (BlockFinder.canSeeBlock(player, blockPos) || i == 15) {
                if (serverLevel.isLoaded(blockPos)){
                    wight.setPos(vec3);
                    wight.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(wight.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
                    wight.upgradePower(sePercent);
                    return serverLevel.addFreshEntity(wight);
                }
            }
        }
        return false;
    }

    public void forceSpawn(ServerLevel pLevel, ServerPlayer pPlayer, CommandSourceStack pSource){
        float rawPercent = (float) SEHelper.getSoulAmountInt(pPlayer) / za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.MaxArcaSouls, 0);
        int sePercent = (int) (rawPercent * 100);
        if (summonWight(pLevel, pPlayer, sePercent)){
            pSource.sendSuccess(() -> Component.translatable("commands.goety.misc.wight.success", pPlayer.getDisplayName()), true);
        } else {
            pSource.sendFailure(Component.translatable("commands.goety.misc.wight.failure"));
        }
    }
}