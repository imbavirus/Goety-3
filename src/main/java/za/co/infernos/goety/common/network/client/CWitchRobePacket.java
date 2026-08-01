package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.inventory.ModSaveInventory;
import za.co.infernos.goety.common.inventory.WitchRobeInventory;
import za.co.infernos.goety.common.items.curios.WitchRobeItem;
import za.co.infernos.goety.utils.CuriosFinder;

public record CWitchRobePacket() implements CustomPacketPayload {
    public static final Type<CWitchRobePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "witch_robe_key"));

    public static final StreamCodec<FriendlyByteBuf, CWitchRobePacket> STREAM_CODEC =
            StreamCodec.unit(new CWitchRobePacket());

    public static void handle(CWitchRobePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity) {
                ItemStack stack = CuriosFinder.findCurio(playerEntity, itemStack -> itemStack.getItem() instanceof WitchRobeItem);

                if (stack != null && !stack.isEmpty()) {
                    int inventoryId = WitchRobeItem.getOrCreateInventoryId(stack);
                    if (inventoryId < 0) {
                        return;
                    }
                    WitchRobeInventory inventory = ModSaveInventory.getInstance().getWitchRobeInventory(inventoryId, playerEntity);
                    playerEntity.openMenu(inventory);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
