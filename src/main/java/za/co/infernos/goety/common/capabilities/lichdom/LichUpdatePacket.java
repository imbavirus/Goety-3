package za.co.infernos.goety.common.capabilities.lichdom;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.init.ModAttachments;
import za.co.infernos.goety.utils.LichdomHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record LichUpdatePacket(UUID playerUUID, CompoundTag tag) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LichUpdatePacket> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "lich_update"));

    public static final StreamCodec<FriendlyByteBuf, LichUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString), LichUpdatePacket::playerUUID,
            ByteBufCodecs.COMPOUND_TAG, LichUpdatePacket::tag,
            LichUpdatePacket::new
    );

    public LichUpdatePacket(Player player) {
        this(player.getUUID(), LichdomHelper.save(player.getData(ModAttachments.LICHDOM)));
    }

    public static void handle(LichUpdatePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = Goety.PROXY.getPlayer();
            if (player != null) {
                ILichdom lichdom = player.getData(ModAttachments.LICHDOM);
                LichdomHelper.load(packet.tag, lichdom);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}