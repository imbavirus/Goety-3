package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public class MagicSmokeParticle extends TextureSheetParticle {
    public int colorFrom;
    public int colorTo;

    public MagicSmokeParticle(ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd, int colorFrom, int colorTo, int duration, float size, float gravity) {
        super(clientLevel, x, y, z, xd, yd, zd);
        this.friction = 0.96F;
        this.gravity = gravity;
        this.speedUpWhenYMotionIsBlocked = true;
        this.xd = xd == 0.0D ? (this.random.nextDouble() * 2 - 1) / 10 : xd;
        this.yd = yd == 0.0D ? 0.1D + this.random.nextDouble() / 10 : yd;
        this.zd = zd == 0.0D ? (this.random.nextDouble() * 2 - 1) / 10 : zd;
        this.xd *= 0.5F;
        this.zd *= 0.5F;
        ColorUtil colorUtil = new ColorUtil(colorFrom);
        this.rCol = colorUtil.red();
        this.gCol = colorUtil.green();
        this.bCol = colorUtil.blue();
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
        this.quadSize = size;
        this.lifetime = duration;
        this.hasPhysics = true;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public float getQuadSize(float p_105642_) {
        return this.quadSize * Mth.clamp(((float)this.age + p_105642_) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        ++this.age;
        if (this.age >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= 0.04D * (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }

            this.xd *= (double)this.friction;
            this.yd *= (double)this.friction;
            this.zd *= (double)this.friction;
            if (this.onGround) {
                this.xd *= (double)0.7F;
                this.zd *= (double)0.7F;
            }

            float lerp = (float) this.age / (float) this.lifetime;
            int newColor = FastColor.ARGB32.lerp(lerp, this.colorFrom, this.colorTo);
            ColorUtil colorUtil = new ColorUtil(newColor);
            this.rCol = colorUtil.red();
            this.gCol = colorUtil.green();
            this.bCol = colorUtil.blue();
        }
    }

    @Override
    public int getLightColor(float f) {
        return LightTexture.FULL_BRIGHT;
    }

    public static class Provider implements ParticleProvider<Option> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(Option option, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            MagicSmokeParticle trailParticle = new MagicSmokeParticle(clientLevel, d, e, f, g, h, i, option.getColorFrom(), option.getColorTo(), option.getDuration(), option.getSize(), option.getGravity());
            trailParticle.pickSprite(this.sprite);
            return trailParticle;
        }
    }

    public static class Option implements ParticleOptions {
        public static final com.mojang.serialization.MapCodec<Option> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("colorFrom").forGetter(Option::getColorFrom),
                Codec.INT.fieldOf("colorTo").forGetter(Option::getColorTo),
                ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(Option::getDuration),
                Codec.FLOAT.fieldOf("size").forGetter(Option::getSize),
                Codec.FLOAT.fieldOf("gravity").forGetter(Option::getGravity)
        ).apply(instance, Option::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Option> STREAM_CODEC = StreamCodec.of(
                (buf, value) -> {
                    buf.writeInt(value.getColorFrom());
                    buf.writeInt(value.getColorTo());
                    buf.writeInt(value.getDuration());
                    buf.writeFloat(value.getSize());
                    buf.writeFloat(value.getGravity());
                },
                buf -> new Option(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat())
        );
        public int colorFrom;
        public int colorTo;
        public int duration;
        public float size;
        public float gravity;

        public Option(int colorFrom, int colorTo, int duration, float size, float gravity){
            this.colorFrom = colorFrom;
            this.colorTo = colorTo;
            this.duration = duration;
            this.size = size;
            this.gravity = gravity;
        }

        public Option(int colorFrom, int colorTo, int duration, float size){
            this(colorFrom, colorTo, duration, size, -0.1F);
        }

        public ParticleType<Option> getType() {
            return ModParticleTypes.MAGIC_SMOKE.get();
        }

        public int getColorFrom() {
            return this.colorFrom;
        }

        public int getColorTo() {
            return this.colorTo;
        }

        public int getDuration() {
            return this.duration;
        }

        public float getSize() {
            return this.size;
        }

        public float getGravity() {
            return this.gravity;
        }
    }
}