/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package za.co.infernos.goety.client.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class SpirallingParticleOption implements ParticleOptions {
    public static final MapCodec<SpirallingParticleOption> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(
                    Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.r),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.g),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.b),
                    Codec.INT.fieldOf("life").forGetter(d -> d.life)).apply(instance, SpirallingParticleOption::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpirallingParticleOption> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeFloat(value.size);
                buf.writeFloat(value.r);
                buf.writeFloat(value.g);
                buf.writeFloat(value.b);
                buf.writeInt(value.life);
            },
            buf -> new SpirallingParticleOption(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                    buf.readInt()));
    public final float size;
    public final float r, g, b;
    public final int life;

    public SpirallingParticleOption(float size, float r, float g, float b, int life) {
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.life = life;
    }

    @NotNull
    @Override
    public ParticleType<SpirallingParticleOption> getType() {
        return ModParticleTypes.SPIRALLING.get();
    }
}
