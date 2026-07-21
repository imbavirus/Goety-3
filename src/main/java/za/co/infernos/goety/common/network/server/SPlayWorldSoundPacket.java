package za.co.infernos.goety.common.network.server;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;

public record SPlayWorldSoundPacket(BlockPos blockPos, ResourceLocation soundId, float volume, float pitch)
        implements CustomPacketPayload {
    public static final Type<SPlayWorldSoundPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "play_world_sound"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SPlayWorldSoundPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SPlayWorldSoundPacket::blockPos,
            ResourceLocation.STREAM_CODEC, SPlayWorldSoundPacket::soundId,
            ByteBufCodecs.FLOAT, SPlayWorldSoundPacket::volume,
            ByteBufCodecs.FLOAT, SPlayWorldSoundPacket::pitch,
            SPlayWorldSoundPacket::new
    );

    public SPlayWorldSoundPacket(BlockPos blockPos, SoundEvent soundEvent, float volume, float pitch) {
        this(blockPos, soundEvent.getLocation(), volume, pitch);
    }

    public static void handle(SPlayWorldSoundPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = Goety.PROXY.getLevel();
            if (level instanceof ClientLevel clientWorld) {
                SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(packet.soundId);
                if (soundEvent == null) {
                    soundEvent = SoundEvent.createVariableRangeEvent(packet.soundId);
                }
                clientWorld.playLocalSound(packet.blockPos, soundEvent, SoundSource.NEUTRAL, packet.volume, packet.pitch, false);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
