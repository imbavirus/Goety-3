package za.co.infernos.goety.common.network.server;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.utils.EntityFinder;

import java.util.Optional;
import java.util.UUID;

public record SPlayEntitySoundPacket(UUID entity, ResourceLocation soundId, float volume, float pitch) implements CustomPacketPayload {
    public static final Type<SPlayEntitySoundPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "play_entity_sound"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SPlayEntitySoundPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString), SPlayEntitySoundPacket::entity,
            ResourceLocation.STREAM_CODEC, SPlayEntitySoundPacket::soundId,
            ByteBufCodecs.FLOAT, SPlayEntitySoundPacket::volume,
            ByteBufCodecs.FLOAT, SPlayEntitySoundPacket::pitch,
            SPlayEntitySoundPacket::new
    );

    public SPlayEntitySoundPacket(UUID uuid, SoundEvent soundEvent, float volume, float pitch) {
        this(uuid, soundEvent.getLocation(), volume, pitch);
    }

    public static void handle(SPlayEntitySoundPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = Goety.PROXY.getLevel();
            if (level instanceof ClientLevel clientLevel) {
                Optional<? extends Entity> optionalEntity = EntityFinder.getEntityByUuiDGlobal(packet.entity);
                if (optionalEntity.isPresent()) {
                    Entity entity = optionalEntity.get();
                    SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(packet.soundId);
                    if (soundEvent == null) {
                        soundEvent = SoundEvent.createVariableRangeEvent(packet.soundId);
                    }
                    clientLevel.playLocalSound(entity.blockPosition(), soundEvent, entity.getSoundSource(), packet.volume, packet.pitch, false);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
