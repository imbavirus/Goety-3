package za.co.infernos.goety.common.capabilities.witchbarter;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.init.ModAttachments;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WBUpdatePacket(int witchId, CompoundTag tag) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WBUpdatePacket> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "wb_update"));

    public static final StreamCodec<FriendlyByteBuf, WBUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, WBUpdatePacket::witchId,
            ByteBufCodecs.COMPOUND_TAG, WBUpdatePacket::tag,
            WBUpdatePacket::new
    );

    public WBUpdatePacket(LivingEntity livingEntity) {
        this(livingEntity.getId(), WitchBarterProvider.save(new CompoundTag(), livingEntity.getData(ModAttachments.WITCH_BARTER)));
    }

    public static void handle(WBUpdatePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = Goety.PROXY.getLevel();
            if (level instanceof ClientLevel clientLevel) {
                Entity entity = clientLevel.getEntity(packet.witchId);
                if (entity instanceof LivingEntity livingEntity) {
                    IWitchBarter barter = livingEntity.getData(ModAttachments.WITCH_BARTER);
                    WitchBarterProvider.load(packet.tag, barter);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
