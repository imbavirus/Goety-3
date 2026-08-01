package za.co.infernos.goety.common.capabilities.misc;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.init.ModAttachments;
import za.co.infernos.goety.utils.MiscCapHelper;
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

public record MiscCapUpdatePacket(int entityID, CompoundTag tag) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MiscCapUpdatePacket> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "misc_update"));

    public static final StreamCodec<FriendlyByteBuf, MiscCapUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MiscCapUpdatePacket::entityID,
            ByteBufCodecs.COMPOUND_TAG, MiscCapUpdatePacket::tag,
            MiscCapUpdatePacket::new
    );

    public MiscCapUpdatePacket(LivingEntity living) {
        this(living.getId(), MiscCapHelper.save(living.getData(ModAttachments.MISC)));
    }

    public static void handle(MiscCapUpdatePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = Goety.PROXY.getLevel();
            if (level instanceof ClientLevel clientLevel) {
                Entity entity = clientLevel.getEntity(packet.entityID);
                if (entity instanceof LivingEntity livingEntity) {
                    IMisc misc = livingEntity.getData(ModAttachments.MISC);
                    MiscCapHelper.load(packet.tag, misc);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
