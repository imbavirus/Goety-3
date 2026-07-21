package za.co.infernos.goety.common.network.client.focus;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.items.handler.SoulUsingItemHandler;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.WandUtil;

public record CAddFocusToInventoryPacket() implements CustomPacketPayload {
    public static final Type<CAddFocusToInventoryPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "add_focus_to_inventory"));

    public static final StreamCodec<FriendlyByteBuf, CAddFocusToInventoryPacket> STREAM_CODEC =
            StreamCodec.unit(new CAddFocusToInventoryPacket());

    public static void handle(CAddFocusToInventoryPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer player) {
                ItemStack stack = WandUtil.findFocus(player);
                if (stack.getCount() <= 0) {
                    return;
                }

                ItemStack wand = WandUtil.findWand(player);

                SoulUsingItemHandler wandHandler = SoulUsingItemHandler.get(wand);

                ItemStack wandFocus = wandHandler.getSlot();

                for (int i = 0; i < player.getInventory().items.size(); ++i) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if (itemStack.isEmpty()) {
                        player.getInventory().setItem(i, wandFocus);
                        wandHandler.extractItem();
                        break;
                    }
                }
                player.connection.send(new ClientboundSoundPacket(
                        BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ModSounds.FOCUS_PICK.value()),
                        SoundSource.PLAYERS,
                        player.position().x, player.position().y, player.position().z,
                        1.0F, 1.0F,
                        player.level().getRandom().nextLong()));
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
