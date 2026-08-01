package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class FoggyCloudParticleOption implements ParticleOptions {
    public static final MapCodec<FoggyCloudParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            com.mojang.serialization.Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
            com.mojang.serialization.Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
            com.mojang.serialization.Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
            com.mojang.serialization.Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
            com.mojang.serialization.Codec.INT.fieldOf("speed").forGetter(d -> d.speed),
            com.mojang.serialization.Codec.BOOL.fieldOf("gravity").forGetter(d -> d.gravity)
    ).apply(instance, FoggyCloudParticleOption::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FoggyCloudParticleOption> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeFloat(value.red);
                buf.writeFloat(value.green);
                buf.writeFloat(value.blue);
                buf.writeFloat(value.size);
                buf.writeInt(value.speed);
                buf.writeBoolean(value.gravity);
            },
            buf -> new FoggyCloudParticleOption(
                    buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readFloat(),
                    buf.readInt(),
                    buf.readBoolean()
            )
    );
    private final float red;
    private final float green;
    private final float blue;
    private final float size;
    private final int speed;
    private final boolean gravity;

    public FoggyCloudParticleOption(ColorUtil colorUtil, float size, int speed) {
        this(colorUtil, size, speed, true);
    }

    public FoggyCloudParticleOption(ColorUtil colorUtil, float size, int speed, boolean gravity) {
        this.red = colorUtil.red;
        this.green = colorUtil.green;
        this.blue = colorUtil.blue;
        this.size = size;
        this.speed = speed;
        this.gravity = gravity;
    }

    public FoggyCloudParticleOption(float r, float g, float b, float size, int speed) {
        this(r, g, b, size, speed, true);
    }

    public FoggyCloudParticleOption(float r, float g, float b, float size, int speed, boolean gravity) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.size = size;
        this.speed = speed;
        this.gravity = gravity;
    }

    public ParticleType<FoggyCloudParticleOption> getType() {
        return ModParticleTypes.FOG_CLOUD.get();
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

    public boolean hasGravity(){
        return this.gravity;
    }
}