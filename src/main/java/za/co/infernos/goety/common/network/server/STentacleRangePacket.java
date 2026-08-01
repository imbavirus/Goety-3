package za.co.infernos.goety.common.network.server;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.neutral.GulfTentacle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import za.co.infernos.goety.compat.legacy.network.NetworkEvent;

import java.util.function.Supplier;

public class STentacleRangePacket {
    private final int mob;
    private final float range;

    public STentacleRangePacket(int mob, float range){
        this.mob = mob;
        this.range = range;
    }

    public static void encode(STentacleRangePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.mob);
        buffer.writeFloat(packet.range);
    }

    public static STentacleRangePacket decode(FriendlyByteBuf buffer) {
        return new STentacleRangePacket(buffer.readInt(), buffer.readFloat());
    }

    public static void consume(STentacleRangePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Goety.PROXY.getLevel();
            if (level instanceof ClientLevel clientWorld) {
                Entity entity = clientWorld.getEntity(packet.mob);
                if (entity instanceof GulfTentacle tentacle) {
                    tentacle.setRange(packet.range);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
