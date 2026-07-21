package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;

public record CTargetPlayerPacket(int aggressor) implements CustomPacketPayload {
    public static final Type<CTargetPlayerPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "target_player"));

    public static final StreamCodec<FriendlyByteBuf, CTargetPlayerPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CTargetPlayerPacket::aggressor,
            CTargetPlayerPacket::new
    );

    public CTargetPlayerPacket(Mob aggressor) {
        this(aggressor.getId());
    }

    public static void handle(CTargetPlayerPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer player) {
                Entity entity = player.level().getEntity(packet.aggressor);
                if (entity instanceof Mob mob) {
                    mob.setTarget(player);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
