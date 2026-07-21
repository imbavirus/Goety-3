package za.co.infernos.goety.common.network.server;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.utils.SEHelper;

public record SFocusCooldownPacket(Item item, int duration) implements CustomPacketPayload {
    public static final Type<SFocusCooldownPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "focus_cooldown"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SFocusCooldownPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ITEM), SFocusCooldownPacket::item,
            ByteBufCodecs.VAR_INT, SFocusCooldownPacket::duration,
            SFocusCooldownPacket::new
    );

    public static void handle(SFocusCooldownPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = Goety.PROXY.getPlayer();
            if (player != null) {
                if (packet.duration == 0) {
                    SEHelper.getFocusCoolDown(player).removeCooldown(player, player.level(), packet.item);
                } else {
                    SEHelper.addCooldown(player, packet.item, packet.duration);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
