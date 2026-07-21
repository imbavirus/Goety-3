package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.entities.IAutoRideable;

public record CAutoRideablePacket() implements CustomPacketPayload {
    public static final Type<CAutoRideablePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "auto_rideable"));

    public static final StreamCodec<FriendlyByteBuf, CAutoRideablePacket> STREAM_CODEC =
            StreamCodec.unit(new CAutoRideablePacket());

    public static void handle(CAutoRideablePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity
                    && playerEntity.getVehicle() instanceof IAutoRideable rideable) {
                rideable.setAutonomous(!rideable.isAutonomous());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
