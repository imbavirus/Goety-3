package za.co.infernos.goety.utils;

import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SFungusExplosionPacket;
import za.co.infernos.goety.common.network.server.SLootingExplosionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ExplosionUtil {

    public static LootingExplosion lootExplode(Level world, @Nullable Entity pExploder, double pX, double pY, double pZ, float pSize, boolean pCausesFire, Explosion.BlockInteraction pMode, LootingExplosion.Mode pLootMode) {
        LootingExplosion explosion = new LootingExplosion(world, pExploder, pX, pY, pZ, pSize, pCausesFire, pMode, pLootMode);
        world.explode(pExploder, pX, pY, pZ, pSize, pCausesFire, Level.ExplosionInteraction.BLOCK);
        if (world instanceof ServerLevel serverLevel) {
            for (ServerPlayer serverplayer : serverLevel.getPlayers((p_147157_) -> {
                return p_147157_.distanceToSqr(pX, pY, pZ) < 4096.0D;
            })) {
                ModNetwork.sendTo(serverplayer, new SLootingExplosionPacket(pX, pY, pZ, pSize, explosion.getHitPlayers().get(serverplayer)));
            }
        }
        return explosion;
    }

    public static FungusExplosion fungusExplode(Level world, @Nullable Entity pExploder, double pX, double pY, double pZ, float pSize, boolean pCausesFire) {
        FungusExplosion explosion = new FungusExplosion(world, pExploder, pX, pY, pZ, pSize, pCausesFire);
        world.explode(pExploder, pX, pY, pZ, pSize, pCausesFire, Level.ExplosionInteraction.BLOCK);
        if (world instanceof ServerLevel serverLevel) {
            for (ServerPlayer serverplayer : serverLevel.getPlayers((p_147157_) -> {
                return p_147157_.distanceToSqr(pX, pY, pZ) < 4096.0D;
            })) {
                ModNetwork.sendTo(serverplayer, new SFungusExplosionPacket(pX, pY, pZ, pSize, null));
            }
        }
        return explosion;
    }
}