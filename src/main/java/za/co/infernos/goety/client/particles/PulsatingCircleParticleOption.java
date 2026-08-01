package za.co.infernos.goety.client.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record PulsatingCircleParticleOption(float size) implements ParticleOptions {
        public static final MapCodec<PulsatingCircleParticleOption> CODEC = RecordCodecBuilder
                        .mapCodec(instance -> instance.group(
                                        Codec.FLOAT.fieldOf("size").forGetter(d -> d.size))
                                        .apply(instance, PulsatingCircleParticleOption::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, PulsatingCircleParticleOption> STREAM_CODEC = StreamCodec
                        .of(
                                        (buf, value) -> buf.writeFloat(value.size),
                                        buf -> new PulsatingCircleParticleOption(buf.readFloat()));

        public ParticleType<PulsatingCircleParticleOption> getType() {
                return ModParticleTypes.MINE_PULSE.get();
        }
}