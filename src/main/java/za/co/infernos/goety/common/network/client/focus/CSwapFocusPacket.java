package za.co.infernos.goety.common.network.client.focus;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.items.handler.FocusBagItemHandler;
import za.co.infernos.goety.common.items.handler.SoulUsingItemHandler;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.TotemFinder;
import za.co.infernos.goety.utils.WandUtil;

public record CSwapFocusPacket(int swapWith) implements CustomPacketPayload {
    public static final Type<CSwapFocusPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "swap_focus"));

    public static final StreamCodec<FriendlyByteBuf, CSwapFocusPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, CSwapFocusPacket::swapWith,
                    CSwapFocusPacket::new);

    public static void handle(CSwapFocusPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer player) {
                swapFocus(packet.swapWith, player);
            }
        });
    }

    public static void swapFocus(int swapSlot, Player player) {
        ItemStack stack = TotemFinder.findBag(player);
        if (stack.getCount() <= 0) {
            return;
        }

        ItemStack wand = WandUtil.findWand(player);

        FocusBagItemHandler bagHandler = FocusBagItemHandler.get(stack);
        SoulUsingItemHandler wandHandler = SoulUsingItemHandler.get(wand);

        ItemStack wandFocus = wandHandler.getSlot();

        ItemStack bagFocus = bagHandler.getStackInSlot(swapSlot);
        bagHandler.setStackInSlot(swapSlot, wandFocus);
        wandHandler.extractItem();
        wandHandler.insertItem(bagFocus);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSoundPacket(
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ModSounds.FOCUS_PICK.value()),
                    SoundSource.PLAYERS,
                    serverPlayer.position().x, serverPlayer.position().y, serverPlayer.position().z,
                    1.0F, 1.0F,
                    serverPlayer.level().getRandom().nextLong()));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
