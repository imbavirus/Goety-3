package za.co.infernos.goety.client;

import za.co.infernos.goety.client.events.BossBarEvent;
import za.co.infernos.goety.client.events.ClientEvents;
import za.co.infernos.goety.client.particles.LightningEffect;
import za.co.infernos.goety.client.particles.LightningParticleOptions;
import za.co.infernos.goety.client.particles.ShockwaveParticle;
import za.co.infernos.goety.init.ModProxy;
import za.co.infernos.goety.utils.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class ClientProxy implements ModProxy {
    @Nullable
    @Override
    public Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    @Nullable
    @Override
    public Level getLevel() {
        return Minecraft.getInstance().level;
    }

    @Override
    public void addBossBar(UUID id, Mob mob) {
        BossBarEvent.addBossBar(id, mob);
    }

    @Override
    public void removeBossBar(UUID id, Mob mob) {
        BossBarEvent.removeBossBar(id, mob);
    }

    @Override
    public void soulExplode(BlockPos blockPos, int radius) {
        spawnSoulExplosion(Minecraft.getInstance().level, blockPos, radius);
    }

    public void shock(Vec3 vectorStart, Vec3 vectorEnd, ColorUtil colorUtil, int lifespan) {
        if (Minecraft.getInstance().level == null){
            return;
        }
        LightningEffect.INSTANCE.add(Minecraft.getInstance().level, new LightningParticleOptions(LightningParticleOptions.BoltRenderInfo.shock(colorUtil), vectorStart, vectorEnd, lifespan).size(0.04F), ClientEvents.PARTIAL_TICK);
    }

    public void thunderBolt(Vec3 vectorStart, Vec3 vectorEnd, ColorUtil colorUtil, int lifespan) {
        if (Minecraft.getInstance().level == null){
            return;
        }
        LightningEffect.INSTANCE.add(Minecraft.getInstance().level, new LightningParticleOptions(LightningParticleOptions.BoltRenderInfo.thunderBolt(colorUtil), vectorStart, vectorEnd, lifespan).size(0.25F), ClientEvents.PARTIAL_TICK);
    }

    public void lightningBolt(Vec3 vectorStart, Vec3 vectorEnd, ColorUtil colorUtil, int lifespan){
        if (Minecraft.getInstance().level == null){
            return;
        }
        LightningEffect.INSTANCE.add(Minecraft.getInstance().level, new LightningParticleOptions(LightningParticleOptions.BoltRenderInfo.thunderBolt(colorUtil).noise(1.0F, 0.001F), vectorStart, vectorEnd, lifespan).size(0.5F), ClientEvents.PARTIAL_TICK);
    }

    public void spawnSoulExplosion(Level level, BlockPos blockPos, int radius){
        if (!(level instanceof ClientLevel)){
            return;
        }
        Minecraft.getInstance().particleEngine.add(new ShockwaveParticle.Explosion((ClientLevel) level, blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F, 0, 0, 0, radius, Minecraft.getInstance().particleEngine));
    }
}
