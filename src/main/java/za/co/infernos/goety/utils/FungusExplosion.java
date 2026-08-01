package za.co.infernos.goety.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class FungusExplosion {
    private final Level level;
    private final @Nullable Entity source;
    private final double x;
    private final double y;
    private final double z;
    private final float radius;
    private final boolean fire;

    public FungusExplosion(Level level, @Nullable Entity source, double x, double y, double z, float radius, boolean fire) {
        this.level = level;
        this.source = source;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.fire = fire;
    }

    public void explode() {
        // no-op in compatibility mode; ExplosionUtil performs world explosion directly.
    }

    public void finalizeExplosion(boolean spawnParticles) {
        // no-op
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
}
