package za.co.infernos.goetied.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.utils.SEHelper;

/**
 * Based on DoubleJumpPacket from Aether-Redux codes: <a href="https://github.com/Zepalesque/The-Aether-Redux/blob/1.20.1/src/main/java/net/zepalesque/redux/network/packet/DoubleJumpPacket.java">...</a>
 */
public record CMultiJumpPacket() implements CustomPacketPayload {
    public static final Type<CMultiJumpPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "multi_jump"));

    public static final StreamCodec<FriendlyByteBuf, CMultiJumpPacket> STREAM_CODEC =
            StreamCodec.unit(new CMultiJumpPacket());

    public static void handle(CMultiJumpPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity) {
                SEHelper.doubleJump(playerEntity);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
