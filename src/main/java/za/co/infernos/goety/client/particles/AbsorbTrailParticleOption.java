package za.co.infernos.goety.client.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record AbsorbTrailParticleOption(Vec3 target, int color, int duration) implements ParticleOptions {
    public static final MapCodec<AbsorbTrailParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Vec3.CODEC.fieldOf("target").forGetter(AbsorbTrailParticleOption::target),
            com.mojang.serialization.Codec.INT.fieldOf("color").forGetter(AbsorbTrailParticleOption::color),
            com.mojang.serialization.Codec.INT.fieldOf("duration").forGetter(AbsorbTrailParticleOption::duration)
    ).apply(instance, AbsorbTrailParticleOption::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AbsorbTrailParticleOption> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeDouble(value.target().x);
                buf.writeDouble(value.target().y);
                buf.writeDouble(value.target().z);
                buf.writeInt(value.color());
                buf.writeInt(value.duration());
            },
            buf -> new AbsorbTrailParticleOption(
                    new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                    buf.readInt(),
                    buf.readInt()
            )
    );

    public ParticleType<AbsorbTrailParticleOption> getType() {
        return ModParticleTypes.ABSORB_TRAIL.get();
    }
}