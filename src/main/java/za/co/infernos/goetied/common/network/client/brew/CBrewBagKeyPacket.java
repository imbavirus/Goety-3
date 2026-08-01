package za.co.infernos.goetied.common.network.client.brew;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.inventory.container.BrewBagContainer;
import za.co.infernos.goetied.common.items.handler.BrewBagItemHandler;
import za.co.infernos.goetied.utils.CuriosFinder;

public record CBrewBagKeyPacket() implements CustomPacketPayload {
    public static final Type<CBrewBagKeyPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "brew_bag_key"));

    public static final StreamCodec<FriendlyByteBuf, CBrewBagKeyPacket> STREAM_CODEC =
            StreamCodec.unit(new CBrewBagKeyPacket());

    public static void handle(CBrewBagKeyPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity) {
                ItemStack stack = CuriosFinder.findBrewBag(playerEntity);

                if (!stack.isEmpty()) {
                    SimpleMenuProvider provider = new SimpleMenuProvider(
                            (id, inventory, player) -> new BrewBagContainer(id, inventory, BrewBagItemHandler.get(stack), stack),
                            Component.translatable(stack.getDescriptionId()));
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
