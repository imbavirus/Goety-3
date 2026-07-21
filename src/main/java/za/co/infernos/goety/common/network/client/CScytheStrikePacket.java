package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.items.equipment.DeathScytheItem;

public record CScytheStrikePacket() implements CustomPacketPayload {
    public static final Type<CScytheStrikePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "scythe_strike"));

    public static final StreamCodec<FriendlyByteBuf, CScytheStrikePacket> STREAM_CODEC =
            StreamCodec.unit(new CScytheStrikePacket());

    public static void handle(CScytheStrikePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity) {
                DeathScytheItem.strike(playerEntity.level(), playerEntity);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
