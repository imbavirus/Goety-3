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
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class WindBlowParticle extends WindTrailParticle {
    public final int width;
    public final float height;

    public WindBlowParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd, float red, float green, float blue, int width, float height, int life) {
        super(world, x, y, z, 0, 0, 0, red, green, blue);
        this.gravity = 0.0F;
        this.xd *= (double)0.1F;
        this.yd *= (double)0.1F;
        this.zd *= (double)0.1F;
        this.xd += xd;
        this.yd += yd;
        this.zd += zd;
        if (life <= 0){
            this.lifetime = 20 + this.random.nextInt(20);
        } else {
            this.lifetime = life;
        }
        this.width = width;
        this.height = height;
    }

    public void tick() {
        super.tick();
        this.trailA = 1.0F - (float) this.age / (float) this.lifetime;
    }

    public float getTrailHeight() {
        return this.height;
    }

    public int sampleSize() {
        return this.width;
    }

    public int getLightColor(float pPartialTick) {
        return LightTexture.FULL_BLOCK;
    }

    public static class Provider implements ParticleProvider<Option> {

        public Provider(SpriteSet p_172490_) {
        }

        public Particle createParticle(Option typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WindBlowParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getWidth(), typeIn.getHeight(), typeIn.getLife());
        }
    }

    public static class Option implements ParticleOptions {
        public static final com.mojang.serialization.MapCodec<Option> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
                Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
                Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
                Codec.INT.fieldOf("width").forGetter(d -> d.width),
                Codec.FLOAT.fieldOf("height").forGetter(d -> d.height),
                Codec.INT.fieldOf("life").forGetter(d -> d.life)
        ).apply(instance, Option::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Option> STREAM_CODEC = StreamCodec.of(
                (buf, value) -> {
                    buf.writeFloat(value.red);
                    buf.writeFloat(value.green);
                    buf.writeFloat(value.blue);
                    buf.writeInt(value.width);
                    buf.writeFloat(value.height);
                    buf.writeInt(value.life);
                },
                buf -> new Option(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readFloat(), buf.readInt())
        );
        private final float red;
        private final float green;
        private final float blue;
        private final int width;
        private final float height;
        private final int life;

        public Option(ColorUtil color, int width, float height) {
            this.red = color.red();
            this.green = color.green();
            this.blue = color.blue();
            this.width = width;
            this.height = height;
            this.life = 0;
        }

        public Option(ColorUtil color, int width, float height, int life) {
            this.red = color.red();
            this.green = color.green();
            this.blue = color.blue();
            this.width = width;
            this.height = height;
            this.life = life;
        }

        public Option(float red, float green, float blue, int width, float height, int life) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.width = width;
            this.height = height;
            this.life = life;
        }

        public Option(float red, float green, float blue, int width, float height) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.width = width;
            this.height = height;
            this.life = 0;
        }

        public ParticleType<Option> getType() {
            return ModParticleTypes.WIND_BLOW.get();
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

        public int getWidth() {
            return this.width;
        }

        public float getHeight() {
            return this.height;
        }

        public int getLife() {
            return this.life;
        }
    }
}