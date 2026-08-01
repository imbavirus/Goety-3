/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class SparkleParticleOption implements ParticleOptions {
    public static final MapCodec<SparkleParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            com.mojang.serialization.Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
            com.mojang.serialization.Codec.FLOAT.fieldOf("r").forGetter(d -> d.r),
            com.mojang.serialization.Codec.FLOAT.fieldOf("g").forGetter(d -> d.g),
            com.mojang.serialization.Codec.FLOAT.fieldOf("b").forGetter(d -> d.b),
            com.mojang.serialization.Codec.INT.fieldOf("extraLife").forGetter(d -> d.extraLife)
    ).apply(instance, SparkleParticleOption::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SparkleParticleOption> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeFloat(value.size);
                buf.writeFloat(value.r);
                buf.writeFloat(value.g);
                buf.writeFloat(value.b);
                buf.writeInt(value.extraLife);
            },
            buf -> new SparkleParticleOption(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt())
    );
    public final float size;
    public final float r, g, b;
    public final int extraLife;

    public SparkleParticleOption(float size, ColorUtil colorUtil, int extraLife) {
        this.size = size;
        this.r = colorUtil.red();
        this.g = colorUtil.green();
        this.b = colorUtil.blue();
        this.extraLife = extraLife;
    }

    public SparkleParticleOption(float size, float r, float g, float b, int extraLife) {
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.extraLife = extraLife;
    }

    @NotNull
    @Override
    public ParticleType<SparkleParticleOption> getType() {
        return ModParticleTypes.SPARKLE.get();
    }
}
