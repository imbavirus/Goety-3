package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class WindParticleOption implements ParticleOptions {
    public static final MapCodec<WindParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            com.mojang.serialization.Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
            com.mojang.serialization.Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
            com.mojang.serialization.Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
            com.mojang.serialization.Codec.FLOAT.fieldOf("width").forGetter(d -> d.width),
            com.mojang.serialization.Codec.FLOAT.fieldOf("height").forGetter(d -> d.height),
            com.mojang.serialization.Codec.INT.fieldOf("life").forGetter(d -> d.life),
            com.mojang.serialization.Codec.INT.fieldOf("ownerId").forGetter(d -> d.ownerId)
    ).apply(instance, WindParticleOption::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WindParticleOption> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeFloat(value.red);
                buf.writeFloat(value.green);
                buf.writeFloat(value.blue);
                buf.writeFloat(value.width);
                buf.writeFloat(value.height);
                buf.writeInt(value.life);
                buf.writeInt(value.ownerId);
            },
            buf -> new WindParticleOption(
                    buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readFloat(), buf.readFloat(),
                    buf.readInt(), buf.readInt()
            )
    );
    private final float red;
    private final float green;
    private final float blue;
    private final float width;
    private final float height;
    private final int life;
    private final int ownerId;

    public WindParticleOption(ColorUtil color, float width, float height, int ownerId) {
        this.red = color.red();
        this.green = color.green();
        this.blue = color.blue();
        this.width = width;
        this.height = height;
        this.life = 0;
        this.ownerId = ownerId;
    }

    public WindParticleOption(ColorUtil color, float width, float height, int life, int ownerId) {
        this.red = color.red();
        this.green = color.green();
        this.blue = color.blue();
        this.width = width;
        this.height = height;
        this.life = life;
        this.ownerId = ownerId;
    }

    public WindParticleOption(float red, float green, float blue, float width, float height, int life, int ownerId) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.width = width;
        this.height = height;
        this.life = life;
        this.ownerId = ownerId;
    }

    public WindParticleOption(float red, float green, float blue, float width, float height, int ownerId) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.width = width;
        this.height = height;
        this.life = 0;
        this.ownerId = ownerId;
    }

    public ParticleType<WindParticleOption> getType() {
        return ModParticleTypes.WIND.get();
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

    public int getLife() {
        return this.life;
    }

    public int getOwnerId() {
        return this.ownerId;
    }
}