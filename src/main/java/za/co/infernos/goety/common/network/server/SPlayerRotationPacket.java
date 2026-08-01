package za.co.infernos.goety.common.network.server;

import za.co.infernos.goety.Goety;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import za.co.infernos.goety.compat.legacy.network.NetworkDirection;
import za.co.infernos.goety.compat.legacy.network.NetworkEvent;

import java.util.function.Supplier;

public class SPlayerRotationPacket {
    private final float yRot;
    private final float xRot;

    public SPlayerRotationPacket(float yRot, float xRot){
        this.yRot = yRot;
        this.xRot = xRot;
    }

    public static void encode(SPlayerRotationPacket packet, FriendlyByteBuf buffer) {
        buffer.writeFloat(packet.yRot);
        buffer.writeFloat(packet.xRot);
    }

    public static SPlayerRotationPacket decode(FriendlyByteBuf buffer) {
        return new SPlayerRotationPacket(
                buffer.readFloat(),
                buffer.readFloat());
    }

    public static void consume(SPlayerRotationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (null == NetworkDirection.PLAY_TO_CLIENT) {
                Player player = Goety.PROXY.getPlayer();
                if (player != null){
                    player.setYRot(packet.yRot);
                    player.setXRot(packet.xRot);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
