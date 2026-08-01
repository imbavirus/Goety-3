package za.co.infernos.goety.common.network.client;

import za.co.infernos.goety.common.magic.cantrips.LichKissCantrip;
import za.co.infernos.goety.utils.LichdomHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import za.co.infernos.goety.compat.legacy.network.NetworkEvent;

import java.util.function.Supplier;

public class CLichKissPacket {
    public static void encode(CLichKissPacket packet, FriendlyByteBuf buffer) {
    }

    public static CLichKissPacket decode(FriendlyByteBuf buffer) {
        return new CLichKissPacket();
    }

    public static void consume(CLichKissPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer playerEntity = za.co.infernos.goety.common.network.NetworkContextHelper.getServerPlayer(ctx);

            if (playerEntity != null) {
                if (LichdomHelper.isLich(playerEntity)) {
                    new LichKissCantrip().sendRay(playerEntity);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


