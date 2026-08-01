package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.items.curios.IActivatable;
import za.co.infernos.goety.utils.CuriosFinder;

public record CActivateCurioKeyPacket() implements CustomPacketPayload {
    public static final Type<CActivateCurioKeyPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "activate_curio_key"));

    public static final StreamCodec<FriendlyByteBuf, CActivateCurioKeyPacket> STREAM_CODEC =
            StreamCodec.unit(new CActivateCurioKeyPacket());

    public static void handle(CActivateCurioKeyPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity) {
                ItemStack stack = CuriosFinder.findCurio(playerEntity, itemStack -> itemStack.getItem() instanceof IActivatable);

                if (stack != null && !stack.isEmpty() && stack.getItem() instanceof IActivatable activatable) {
                    activatable.activate(playerEntity.level(), playerEntity, stack);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
