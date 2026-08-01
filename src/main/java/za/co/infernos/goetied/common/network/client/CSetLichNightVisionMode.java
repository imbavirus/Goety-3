package za.co.infernos.goetied.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.config.MainConfig;
import za.co.infernos.goetied.utils.LichdomHelper;

public record CSetLichNightVisionMode() implements CustomPacketPayload {
    public static final Type<CSetLichNightVisionMode> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "set_lich_night_vision"));

    public static final StreamCodec<FriendlyByteBuf, CSetLichNightVisionMode> STREAM_CODEC =
            StreamCodec.unit(new CSetLichNightVisionMode());

    public static void handle(CSetLichNightVisionMode packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity
                    && LichdomHelper.isLich(playerEntity)
                    && za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MainConfig.LichNightVision, false)) {
                LichdomHelper.setNightVision(playerEntity, !LichdomHelper.nightVision(playerEntity));
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
