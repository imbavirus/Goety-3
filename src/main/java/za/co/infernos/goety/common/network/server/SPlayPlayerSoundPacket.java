package za.co.infernos.goety.common.network.server;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;

public record SPlayPlayerSoundPacket(ResourceLocation soundId, float volume, float pitch) implements CustomPacketPayload {
    public static final Type<SPlayPlayerSoundPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "play_player_sound"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SPlayPlayerSoundPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SPlayPlayerSoundPacket::soundId,
            ByteBufCodecs.FLOAT, SPlayPlayerSoundPacket::volume,
            ByteBufCodecs.FLOAT, SPlayPlayerSoundPacket::pitch,
            SPlayPlayerSoundPacket::new
    );

    public SPlayPlayerSoundPacket(SoundEvent soundEvent, float volume, float pitch) {
        this(soundEvent.getLocation(), volume, pitch);
    }

    public static void handle(SPlayPlayerSoundPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = Goety.PROXY.getPlayer();
            if (player != null) {
                SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(packet.soundId);
                if (soundEvent == null) {
                    soundEvent = SoundEvent.createVariableRangeEvent(packet.soundId);
                }
                player.playSound(soundEvent, packet.volume, packet.pitch);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
