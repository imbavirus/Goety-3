package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;

public class MagicAshSmokeParticle extends BaseAshSmokeParticle {
    public int colorFrom;
    public int colorTo;
    private final SpriteSet sprites;

    public MagicAshSmokeParticle(ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd, int colorFrom, int colorTo, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 0.1F, 0.1F, 0.1F, xd, yd, zd, 1.0F, spriteSet, 0.3F, 8, -0.1F, true);
        this.sprites = spriteSet;
        ColorUtil colorUtil = new ColorUtil(colorFrom);
        this.rCol = colorUtil.red();
        this.gCol = colorUtil.green();
        this.bCol = colorUtil.blue();
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.sprites);
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

            this.xd *= this.friction;
            this.yd *= this.friction;
            this.zd *= this.friction;
            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
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
            MagicAshSmokeParticle trailParticle = new MagicAshSmokeParticle(clientLevel, d, e, f, g, h, i, option.getColorFrom(), option.getColorTo(), this.sprite);
            trailParticle.pickSprite(this.sprite);
            return trailParticle;
        }
    }

    public static class Option implements ParticleOptions {
        public static final com.mojang.serialization.MapCodec<Option> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("colorFrom").forGetter(Option::getColorFrom),
                Codec.INT.fieldOf("colorTo").forGetter(Option::getColorTo)
        ).apply(instance, Option::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Option> STREAM_CODEC = StreamCodec.of(
                (buf, value) -> {
                    buf.writeInt(value.getColorFrom());
                    buf.writeInt(value.getColorTo());
                },
                buf -> new Option(buf.readInt(), buf.readInt())
        );
        public int colorFrom;
        public int colorTo;

        public Option(int colorFrom, int colorTo){
            this.colorFrom = colorFrom;
            this.colorTo = colorTo;
        }

        public Option(int color){
            this.colorFrom = color;
            this.colorTo = color;
        }

        public ParticleType<Option> getType() {
            return ModParticleTypes.MAGIC_ASH_SMOKE.get();
        }

        public int getColorFrom() {
            return this.colorFrom;
        }

        public int getColorTo() {
            return this.colorTo;
        }
    }
}