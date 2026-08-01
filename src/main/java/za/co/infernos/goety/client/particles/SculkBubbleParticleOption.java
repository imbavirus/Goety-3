package za.co.infernos.goety.client.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.phys.Vec3;

public class SculkBubbleParticleOption implements ParticleOptions {
    public static final MapCodec<SculkBubbleParticleOption> CODEC = RecordCodecBuilder.mapCodec((p_235978_) -> {
        return p_235978_.group(PositionSource.CODEC.fieldOf("destination").forGetter((p_235982_) -> {
            return p_235982_.destination;
        }), Codec.INT.fieldOf("arrival_in_ticks").forGetter((p_235980_) -> {
            return p_235980_.arrivalInTicks;
        })).apply(p_235978_, SculkBubbleParticleOption::new);
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, SculkBubbleParticleOption> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                PositionSource.STREAM_CODEC.encode(buf, value.destination);
                buf.writeVarInt(value.arrivalInTicks);
            },
            buf -> new SculkBubbleParticleOption(PositionSource.STREAM_CODEC.decode(buf), buf.readVarInt()));
    private final PositionSource destination;
    private final int arrivalInTicks;

    public SculkBubbleParticleOption(PositionSource p_235975_, int p_235976_) {
        this.destination = p_235975_;
        this.arrivalInTicks = p_235976_;
    }

    public ParticleType<SculkBubbleParticleOption> getType() {
        return ModParticleTypes.SCULK_BUBBLE.get();
    }

    public PositionSource getDestination() {
        return this.destination;
    }

    public int getArrivalInTicks() {
        return this.arrivalInTicks;
    }
}