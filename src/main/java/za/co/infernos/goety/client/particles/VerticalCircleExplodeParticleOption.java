package za.co.infernos.goety.client.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class VerticalCircleExplodeParticleOption implements ParticleOptions {
    public static final MapCodec<VerticalCircleExplodeParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            com.mojang.serialization.Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
            com.mojang.serialization.Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
            com.mojang.serialization.Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
            com.mojang.serialization.Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
            com.mojang.serialization.Codec.INT.fieldOf("speed").forGetter(d -> d.speed)
    ).apply(instance, VerticalCircleExplodeParticleOption::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, VerticalCircleExplodeParticleOption> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeFloat(value.red);
                buf.writeFloat(value.green);
                buf.writeFloat(value.blue);
                buf.writeFloat(value.size);
                buf.writeInt(value.speed);
            },
            buf -> new VerticalCircleExplodeParticleOption(
                    buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readFloat(),
                    buf.readInt()
            )
    );
    private final float red;
    private final float green;
    private final float blue;
    private final float size;
    private final int speed;

    public VerticalCircleExplodeParticleOption(float r, float g, float b) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.size = 10;
        this.speed = 0;
    }

    public VerticalCircleExplodeParticleOption(float r, float g, float b, float size, int speed) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.size = size;
        this.speed = speed;
    }

    public ParticleType<VerticalCircleExplodeParticleOption> getType() {
        return ModParticleTypes.VERTICAL_CIRCLE_EXPLODE.get();
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

    public float getSize(){
        return this.size;
    }

    public int getSpeed(){
        return this.speed;
    }
}