package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.magic.cantrips.MagnetCantrip;
import za.co.infernos.goety.utils.LichdomHelper;

public record CMagnetPacket() implements CustomPacketPayload {
    public static final Type<CMagnetPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "magnet"));

    public static final StreamCodec<FriendlyByteBuf, CMagnetPacket> STREAM_CODEC =
            StreamCodec.unit(new CMagnetPacket());

    public static void handle(CMagnetPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity && LichdomHelper.isLich(playerEntity)) {
                new MagnetCantrip().callItems(playerEntity);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
