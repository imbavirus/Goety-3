package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.utils.LichdomHelper;

public record CSetLichMode() implements CustomPacketPayload {
    public static final Type<CSetLichMode> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "set_lich_mode"));

    public static final StreamCodec<FriendlyByteBuf, CSetLichMode> STREAM_CODEC =
            StreamCodec.unit(new CSetLichMode());

    public static void handle(CSetLichMode packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity && LichdomHelper.isLich(playerEntity)) {
                LichdomHelper.setLichMode(playerEntity, !LichdomHelper.isInLichMode(playerEntity));
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
