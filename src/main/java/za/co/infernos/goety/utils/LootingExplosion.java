package za.co.infernos.goety.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootingExplosion {
    private final Level level;
    private final @Nullable Entity source;
    private final double x;
    private final double y;
    private final double z;
    private final float radius;
    private final boolean fire;
    private final Explosion.BlockInteraction blockInteraction;
    public Mode lootMode;
    private final Map<Player, Vec3> hitPlayers = new HashMap<>();

    public LootingExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fire, Explosion.BlockInteraction blockInteraction, Mode lootMode, List<BlockPos> toBlow) {
        this(level, source, x, y, z, radius, fire, blockInteraction, lootMode);
    }

    public LootingExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fire, Explosion.BlockInteraction blockInteraction, Mode lootMode) {
        this.level = level;
        this.source = source;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.fire = fire;
        this.blockInteraction = blockInteraction;
        this.lootMode = lootMode;
    }

    public void explode() {
        // no-op: explosion is handled by ExplosionUtil in compatibility mode
    }

    public void finalizeExplosion(boolean spawnParticles) {
        // no-op
    }

    public Map<Player, Vec3> getHitPlayers() {
        return this.hitPlayers;
    }

    public Level level() {
        return this.level;
    }

    public @Nullable Entity getSource() {
        return this.source;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getRadius() {
        return this.radius;
    }

    public boolean isFire() {
        return this.fire;
    }

    public Explosion.BlockInteraction getBlockInteraction() {
        return this.blockInteraction;
    }

    public enum Mode {
        REGULAR,
        LOOT
    }
}
