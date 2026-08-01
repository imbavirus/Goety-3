package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;

public class WindShockwaveParticle extends WindParticle{
    public float increase;

    public WindShockwaveParticle(ClientLevel world, double x, double y, double z, float red, float green, float blue, float width, float height, float increase, float startRot, int life, int ownerId) {
        super(world, x, y, z, red, green, blue, width, height, life, ownerId);
        this.increase = increase;
        this.initYRot = startRot * 360.0F;
        this.rotateAge = (10.0F + startRot * 10.0F);
    }

    @Override
    public Vec3 getOrbitPosition() {
        Vec3 position = this.getPosition();
        Vec3 vec3 = new Vec3(0.0D, this.height, this.width + (this.age * this.increase)).yRot((float)Math.toRadians(this.initYRot + this.rotateAge * (float)this.age));
        return position.add(vec3);
    }

    @Override
    public float getTrailHeight() {
        return 1.0F;
    }

    public static class Provider implements ParticleProvider<Option> {

        public Provider(SpriteSet p_172490_) {
        }

        public Particle createParticle(Option typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WindShockwaveParticle(worldIn, x, y, z, typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getWidth(), typeIn.getHeight(), typeIn.getIncrease(), typeIn.getStartYRot(), typeIn.getLife(), typeIn.getOwnerId());
        }
    }

    public static class Option implements ParticleOptions {
        public static final com.mojang.serialization.MapCodec<Option> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
                Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
                Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
                Codec.FLOAT.fieldOf("width").forGetter(d -> d.width),
                Codec.FLOAT.fieldOf("height").forGetter(d -> d.height),
                Codec.FLOAT.fieldOf("increase").forGetter(d -> d.increase),
                Codec.FLOAT.fieldOf("startYRot").forGetter(d -> d.startYRot),
                Codec.INT.fieldOf("life").forGetter(d -> d.life),
                Codec.INT.fieldOf("ownerId").forGetter(d -> d.ownerId)
        ).apply(instance, Option::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Option> STREAM_CODEC = StreamCodec.of(
                (buf, value) -> {
                    buf.writeFloat(value.red);
                    buf.writeFloat(value.green);
                    buf.writeFloat(value.blue);
                    buf.writeFloat(value.width);
                    buf.writeFloat(value.height);
                    buf.writeFloat(value.increase);
                    buf.writeFloat(value.startYRot);
                    buf.writeInt(value.life);
                    buf.writeInt(value.ownerId);
                },
                buf -> new Option(
                        buf.readFloat(), buf.readFloat(), buf.readFloat(),
                        buf.readFloat(), buf.readFloat(),
                        buf.readFloat(), buf.readFloat(),
                        buf.readInt(), buf.readInt()
                )
        );
        private final float red;
        private final float green;
        private final float blue;
        private final float width;
        private final float height;
        private final float increase;
        private final float startYRot;
        private final int life;
        private final int ownerId;

        public Option(ColorUtil color, float width, float height, float increase, float startYRot, int ownerId) {
            this.red = color.red();
            this.green = color.green();
            this.blue = color.blue();
            this.width = width;
            this.height = height;
            this.increase = increase;
            this.startYRot = startYRot;
            this.life = 0;
            this.ownerId = ownerId;
        }

        public Option(ColorUtil color, float width, float height, float increase, float startYRot, int life, int ownerId) {
            this.red = color.red();
            this.green = color.green();
            this.blue = color.blue();
            this.width = width;
            this.height = height;
            this.increase = increase;
            this.startYRot = startYRot;
            this.life = life;
            this.ownerId = ownerId;
        }

        public Option(float red, float green, float blue, float width, float height, float increase, float startYRot, int ownerId) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.width = width;
            this.height = height;
            this.increase = increase;
            this.startYRot = startYRot;
            this.life = 0;
            this.ownerId = ownerId;
        }

        public Option(float red, float green, float blue, float width, float height, float increase, float startYRot, int life, int ownerId) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.width = width;
            this.height = height;
            this.increase = increase;
            this.startYRot = startYRot;
            this.life = life;
            this.ownerId = ownerId;
        }

        public ParticleType<Option> getType() {
            return ModParticleTypes.WIND_SHOCKWAVE.get();
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

        public float getWidth() {
            return this.width;
        }

        public float getHeight() {
            return this.height;
        }

        public float getIncrease() {
            return this.increase;
        }

        public float getStartYRot() {
            return this.startYRot;
        }

        public int getLife() {
            return this.life;
        }

        public int getOwnerId() {
            return this.ownerId;
        }
    }
}