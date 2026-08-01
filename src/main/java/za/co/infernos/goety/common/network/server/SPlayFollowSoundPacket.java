package za.co.infernos.goety.common.network.server;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.audio.LoopSoundPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import za.co.infernos.goety.compat.legacy.network.NetworkDirection;
import za.co.infernos.goety.compat.legacy.network.NetworkEvent;

import java.util.function.Supplier;

public class SPlayFollowSoundPacket {
    private final SoundEvent soundEvent;
    private final int entity;
    private final float volume;
    private final float pitch;
    private final boolean loop;

    public SPlayFollowSoundPacket(Entity entity, SoundEvent soundEvent, float volume, float pitch, boolean loop){
        this.entity = entity.getId();
        this.soundEvent = soundEvent;
        this.volume = volume;
        this.pitch = pitch;
        this.loop = loop;
    }

    public SPlayFollowSoundPacket(int entity, SoundEvent soundEvent, float volume, float pitch, boolean loop){
        this.entity = entity;
        this.soundEvent = soundEvent;
        this.volume = volume;
        this.pitch = pitch;
        this.loop = loop;
    }

    public static void encode(SPlayFollowSoundPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entity);
        buffer.writeResourceLocation(packet.soundEvent.getLocation());
        buffer.writeFloat(packet.volume);
        buffer.writeFloat(packet.pitch);
        buffer.writeBoolean(packet.loop);
    }

    public static SPlayFollowSoundPacket decode(FriendlyByteBuf buffer) {
        return new SPlayFollowSoundPacket(
                buffer.readInt(),
                SoundEvent.createVariableRangeEvent(buffer.readResourceLocation()),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readBoolean());
    }

    public static void consume(SPlayFollowSoundPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (null == NetworkDirection.PLAY_TO_CLIENT) {
                Level level = Goety.PROXY.getLevel();
                if (level instanceof ClientLevel clientWorld) {
                    if (packet.entity >= 0) {
                        Entity entity = clientWorld.getEntity(packet.entity);
                        if (entity != null) {
                            LoopSoundPlayer.playFollowSound(entity, packet.soundEvent, packet.volume, packet.pitch, packet.loop);
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

