package za.co.infernos.goetied.common.network.server;

import za.co.infernos.goetied.Goetied;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import za.co.infernos.goetied.compat.legacy.network.NetworkEvent;

import java.util.function.Supplier;

public class SSoulExplodePacket {
    private final BlockPos blockPos;
    private final int radius;

    public SSoulExplodePacket(BlockPos blockPos, int radius){
        this.blockPos = blockPos;
        this.radius = radius;
    }

    public static void encode(SSoulExplodePacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.blockPos);
        buffer.writeInt(packet.radius);
    }

    public static SSoulExplodePacket decode(FriendlyByteBuf buffer) {
        return new SSoulExplodePacket(buffer.readBlockPos(), buffer.readInt());
    }

    public static void consume(SSoulExplodePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Goetied.PROXY.soulExplode(packet.blockPos, packet.radius);
        });
        ctx.get().setPacketHandled(true);
    }
}