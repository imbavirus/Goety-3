package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.neutral.IRavager;

public record CRavagerRoarPacket() implements CustomPacketPayload {
    public static final Type<CRavagerRoarPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "ravager_roar"));

    public static final StreamCodec<FriendlyByteBuf, CRavagerRoarPacket> STREAM_CODEC =
            StreamCodec.unit(new CRavagerRoarPacket());

    public static void handle(CRavagerRoarPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity
                    && playerEntity.getVehicle() instanceof IRavager ravager) {
                ravager.forceRoar();
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
