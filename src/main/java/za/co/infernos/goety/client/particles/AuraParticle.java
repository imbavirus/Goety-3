package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AuraParticle extends TextureSheetParticle {
    private final float rotSpeed;
    public final int ownerId;
    public final Vec3 origin;

    protected AuraParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteSet, int ownerId, float size) {
        super(level, x, y, z);
        this.ownerId = ownerId;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.origin = new Vec3(x, y, z);
        this.hasPhysics = false;
        this.quadSize = size;
        this.lifetime = 20;
        this.setSpriteFromAge(spriteSet);
        this.rotSpeed = ((float)Math.random() - 0.5F) * 0.1F;
        this.roll = (float)Math.random() * ((float)Math.PI * 2F);
    }

    public Vec3 getPosition() {
        Entity owner = this.getEntity();
        return owner != null ? new Vec3(owner.getX(), owner.getY() + (owner.getBbHeight() / 2.0F), owner.getZ()) : this.origin;
    }

    public Entity getEntity() {
        return this.ownerId == -1 ? null : this.level.getEntity(this.ownerId);
    }

    public int getLightColor(final float partialTicks) {
        return 240;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        Vec3 vec3 = this.getPosition();
        this.setPos(vec3.x, vec3.y, vec3.z);
        if (++this.age >= this.lifetime || this.getEntity() == null) {
            this.remove();
        }
        this.oRoll = this.roll;
        this.roll += (float)Math.PI * this.rotSpeed * 2.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<AuraParticle.Option> {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_i50607_1_) {
            this.sprites = p_i50607_1_;
        }

        public Particle createParticle(AuraParticle.Option pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new AuraParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites, pType.getOwnerId(), pType.getSize());
        }
    }

    public static class Option implements ParticleOptions {
        public static final MapCodec<Option> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                com.mojang.serialization.Codec.INT.fieldOf("ownerId").forGetter(d -> d.ownerId),
                com.mojang.serialization.Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
                com.mojang.serialization.Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
                com.mojang.serialization.Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
                com.mojang.serialization.Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue)
        ).apply(instance, Option::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Option> STREAM_CODEC = StreamCodec.of(
                (buf, value) -> {
                    buf.writeInt(value.ownerId);
                    buf.writeFloat(value.size);
                    buf.writeFloat(value.red);
                    buf.writeFloat(value.green);
                    buf.writeFloat(value.blue);
                },
                buf -> new Option(buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat())
        );
        private final int ownerId;
        private final float size;
        private final float red;
        private final float green;
        private final float blue;

        public Option(int ownerId, float size, ColorUtil color) {
            this.ownerId = ownerId;
            this.size = size;
            this.red = color.red;
            this.green = color.green;
            this.blue = color.blue;
        }

        public Option(int ownerId, float size, float red, float green, float blue) {
            this.ownerId = ownerId;
            this.size = size;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        @NotNull
        @Override
        public ParticleType<Option> getType() {
            return ModParticleTypes.AURA.get();
        }

        public int getOwnerId() {
            return this.ownerId;
        }

        public float getSize(){
            return this.size;
        }

        public float getRed() {
            return this.red;
        }

        public float getGreen() {
            return this.green;
        }

        public float getBlue() {
            return this.blue;
        }
    }

}
