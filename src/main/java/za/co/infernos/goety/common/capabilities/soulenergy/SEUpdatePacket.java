package za.co.infernos.goety.common.capabilities.soulenergy;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.init.ModAttachments;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SEUpdatePacket(UUID playerUUID, CompoundTag tag) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SEUpdatePacket> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "se_update"));

    public static final StreamCodec<FriendlyByteBuf, SEUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString), SEUpdatePacket::playerUUID,
            ByteBufCodecs.COMPOUND_TAG, SEUpdatePacket::tag,
            SEUpdatePacket::new
    );

    public SEUpdatePacket(Player player) {
        this(player.getUUID(), SEHelper.save(new CompoundTag(), player.getData(ModAttachments.SOUL_ENERGY)));
    }

    public static void handle(SEUpdatePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = Goety.PROXY.getPlayer();
            if (player != null) {
                ISoulEnergy soulEnergy = player.getData(ModAttachments.SOUL_ENERGY);
                SEHelper.load(packet.tag, soulEnergy);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}