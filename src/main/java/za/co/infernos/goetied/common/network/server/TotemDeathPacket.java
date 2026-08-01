package za.co.infernos.goetied.common.network.server;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.utils.TotemFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import za.co.infernos.goetied.compat.legacy.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TotemDeathPacket {
    private final UUID LivingEntityUUID;

    public TotemDeathPacket(UUID uuid) {
        this.LivingEntityUUID = uuid;
    }

    public static void encode(TotemDeathPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.LivingEntityUUID);
    }

    public static TotemDeathPacket decode(FriendlyByteBuf buffer) {
        return new TotemDeathPacket(buffer.readUUID());
    }

    public static void consume(TotemDeathPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            Player playerEntity = Goetied.PROXY.getPlayer();

            if (playerEntity != null) {
                Minecraft.getInstance().particleEngine.createTrackingEmitter(playerEntity, ParticleTypes.TOTEM_OF_UNDYING, 30);
                playerEntity.level().playLocalSound(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.TOTEM_USE, playerEntity.getSoundSource(), 1.0F, 1.0F, false);
                Minecraft.getInstance().gameRenderer.displayItemActivation(TotemFinder.FindTotem(playerEntity));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}