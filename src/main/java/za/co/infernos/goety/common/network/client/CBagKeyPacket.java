package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.inventory.container.FocusBagContainer;
import za.co.infernos.goety.client.inventory.container.FocusPackContainer;
import za.co.infernos.goety.common.items.handler.FocusBagItemHandler;
import za.co.infernos.goety.common.items.magic.FocusPack;
import za.co.infernos.goety.utils.TotemFinder;

public record CBagKeyPacket() implements CustomPacketPayload {
    public static final Type<CBagKeyPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "bag_key"));

    public static final StreamCodec<FriendlyByteBuf, CBagKeyPacket> STREAM_CODEC =
            StreamCodec.unit(new CBagKeyPacket());

    public static void handle(CBagKeyPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity) {
                ItemStack stack = TotemFinder.findBag(playerEntity);

                if (!stack.isEmpty()) {
                    SimpleMenuProvider provider = new SimpleMenuProvider(
                            (id, inventory, player) -> new FocusBagContainer(id, inventory, FocusBagItemHandler.get(stack), stack),
                            Component.translatable(stack.getDescriptionId()));
                    if (stack.getItem() instanceof FocusPack) {
                        provider = new SimpleMenuProvider(
                                (id, inventory, player) -> new FocusPackContainer(id, inventory, FocusBagItemHandler.get(stack), stack),
                                Component.translatable(stack.getDescriptionId()));
                    }
                    playerEntity.openMenu(provider);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
