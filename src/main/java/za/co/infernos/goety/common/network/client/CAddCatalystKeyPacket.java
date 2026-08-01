package za.co.infernos.goety.common.network.client;

import za.co.infernos.goety.common.inventory.ModSaveInventory;
import za.co.infernos.goety.common.inventory.WitchRobeInventory;
import za.co.infernos.goety.common.items.curios.WitchRobeItem;
import za.co.infernos.goety.utils.CuriosFinder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import za.co.infernos.goety.compat.legacy.network.NetworkEvent;

import java.util.function.Supplier;

public class CAddCatalystKeyPacket {
    public static void encode(CAddCatalystKeyPacket packet, FriendlyByteBuf buffer) {
    }

    public static CAddCatalystKeyPacket decode(FriendlyByteBuf buffer) {
        return new CAddCatalystKeyPacket();
    }

    public static void consume(CAddCatalystKeyPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer playerEntity = za.co.infernos.goety.common.network.NetworkContextHelper.getServerPlayer(ctx);

            if (playerEntity != null) {
                ItemStack stack = CuriosFinder.findCurio(playerEntity, itemStack -> itemStack.getItem() instanceof WitchRobeItem);
                ItemStack mainHandItem = playerEntity.getMainHandItem();
                ItemStack offhandItem = playerEntity.getOffhandItem();

                if (!stack.isEmpty()){
                    int inventoryId = WitchRobeItem.getOrCreateInventoryId(stack);
                    if (inventoryId < 0) {
                        return;
                    }
                    WitchRobeInventory inventory = ModSaveInventory.getInstance().getWitchRobeInventory(inventoryId, playerEntity);
                    if (!mainHandItem.isEmpty()){
                        inventory.addBottlesOrCatalyst(mainHandItem);
                    } else if (!offhandItem.isEmpty()){
                        inventory.addBottlesOrCatalyst(offhandItem);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


