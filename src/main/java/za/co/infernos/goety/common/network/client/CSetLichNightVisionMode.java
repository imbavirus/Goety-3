package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.LichdomHelper;

public record CSetLichNightVisionMode() implements CustomPacketPayload {
    public static final Type<CSetLichNightVisionMode> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "set_lich_night_vision"));

    public static final StreamCodec<FriendlyByteBuf, CSetLichNightVisionMode> STREAM_CODEC =
            StreamCodec.unit(new CSetLichNightVisionMode());

    public static void handle(CSetLichNightVisionMode packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity
                    && LichdomHelper.isLich(playerEntity)
                    && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichNightVision, false)) {
                LichdomHelper.setNightVision(playerEntity, !LichdomHelper.nightVision(playerEntity));
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
