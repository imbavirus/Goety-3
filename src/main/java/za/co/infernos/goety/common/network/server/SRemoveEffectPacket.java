package za.co.infernos.goety.common.network.server;

import za.co.infernos.goety.Goety;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import za.co.infernos.goety.compat.legacy.network.NetworkEvent;

import java.util.function.Supplier;

public class SRemoveEffectPacket {
    private final int mob;
    private final int effect;

    public SRemoveEffectPacket(int mob, int effect){
        this.mob = mob;
        this.effect = effect;
    }

    public static void encode(SRemoveEffectPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.mob);
        buffer.writeInt(packet.effect);
    }

    public static SRemoveEffectPacket decode(FriendlyByteBuf buffer) {
        return new SRemoveEffectPacket(buffer.readInt(), buffer.readInt());
    }

    public static void consume(SRemoveEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Goety.PROXY.getLevel();
            if (level instanceof ClientLevel clientWorld) {
                Entity entity = clientWorld.getEntity(packet.mob);
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.removeEffect(BuiltInRegistries.MOB_EFFECT.getHolder(packet.effect).orElseThrow());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
