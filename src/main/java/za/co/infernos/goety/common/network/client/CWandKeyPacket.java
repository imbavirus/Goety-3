package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.items.magic.IWand;
import za.co.infernos.goety.client.inventory.container.SoulItemContainer;
import za.co.infernos.goety.common.items.handler.SoulUsingItemHandler;

public record CWandKeyPacket() implements CustomPacketPayload {
    public static final Type<CWandKeyPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "wand_key"));

    public static final StreamCodec<FriendlyByteBuf, CWandKeyPacket> STREAM_CODEC =
            StreamCodec.unit(new CWandKeyPacket());

    public static void handle(CWandKeyPacket packet, IPayloadContext ctx) {
        Goety.LOGGER.debug("[Goety] CWandKeyPacket received on server");
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity) {
                ItemStack mainHand = playerEntity.getMainHandItem();
                ItemStack offHand = playerEntity.getOffhandItem();

                final ItemStack wandStack;
                final InteractionHand hand;
                if (!mainHand.isEmpty() && mainHand.getItem() instanceof IWand) {
                    wandStack = mainHand;
                    hand = InteractionHand.MAIN_HAND;
                } else if (!offHand.isEmpty() && offHand.getItem() instanceof IWand) {
                    wandStack = offHand;
                    hand = InteractionHand.OFF_HAND;
                } else {
                    return;
                }

                SimpleMenuProvider provider = new SimpleMenuProvider(
                        (id, inventory, player) -> new SoulItemContainer(id, inventory, SoulUsingItemHandler.get(wandStack), wandStack, hand),
                        Component.translatable(wandStack.getDescriptionId()));
                playerEntity.openMenu(provider, buf -> buf.writeBoolean(hand == InteractionHand.MAIN_HAND));
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
