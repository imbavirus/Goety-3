package za.co.infernos.goety.common.network;

import za.co.infernos.goety.utils.EntityFinder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import za.co.infernos.goety.compat.legacy.network.NetworkDirection;
import za.co.infernos.goety.compat.legacy.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class EntityUpdatePacket {
    private final UUID LivingEntityUUID;
    private CompoundTag tag;

    public EntityUpdatePacket(UUID uuid, CompoundTag tag) {
        this.LivingEntityUUID = uuid;
        this.tag = tag;
    }

    public static void encode(EntityUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.LivingEntityUUID);
        buffer.writeNbt(packet.tag);
    }

    public static EntityUpdatePacket decode(FriendlyByteBuf buffer) {
        return new EntityUpdatePacket(buffer.readUUID(), buffer.readNbt());
    }

    public static void consume(EntityUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (null == NetworkDirection.PLAY_TO_CLIENT) {
                Entity entity = EntityFinder.getEntityByUuiDGlobal(packet.LivingEntityUUID).get();
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.readAdditionalSaveData(packet.tag);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
