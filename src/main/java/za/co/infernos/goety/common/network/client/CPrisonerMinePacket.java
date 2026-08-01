package za.co.infernos.goety.common.network.client;

import za.co.infernos.goety.common.entities.ally.illager.Prisoner;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.utils.ItemHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import za.co.infernos.goety.compat.legacy.network.NetworkEvent;

import java.util.function.Supplier;

public class CPrisonerMinePacket {
    public int mob;

    public CPrisonerMinePacket(int id) {
        this.mob = id;
    }

    public static void encode(CPrisonerMinePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.mob);
    }

    public static CPrisonerMinePacket decode(FriendlyByteBuf buffer) {
        return new CPrisonerMinePacket(buffer.readInt());
    }

    public static void consume(CPrisonerMinePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer playerEntity = za.co.infernos.goety.common.network.NetworkContextHelper.getServerPlayer(ctx);

            if (playerEntity != null) {
                Entity entity = playerEntity.level().getEntity(packet.mob);
                if (entity instanceof Prisoner prisoner) {
                    ItemHelper.hurtAndBreak(prisoner.getMainHandItem(), za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.PrisonerMiningDurability, 0), prisoner);
                    prisoner.mineTimes += 1;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


