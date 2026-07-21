package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.ModDamageSource;
import za.co.infernos.goety.utils.SEHelper;

public record CDismissServantsPacket() implements CustomPacketPayload {
    public static final Type<CDismissServantsPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "dismiss_servants"));

    public static final StreamCodec<FriendlyByteBuf, CDismissServantsPacket> STREAM_CODEC =
            StreamCodec.unit(new CDismissServantsPacket());

    public static void handle(CDismissServantsPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity
                    && playerEntity.level() instanceof ServerLevel serverLevel) {
                for (Entity entity : serverLevel.getAllEntities()) {
                    if (entity instanceof IOwned owned && owned instanceof LivingEntity livingEntity && owned.getTrueOwner() == playerEntity) {
                        if (owned.isLimitedLife()
                                && !SEHelper.getGroundedEntities(playerEntity).contains(livingEntity)
                                && !SEHelper.getGroundedEntityTypes(playerEntity).contains(entity.getType())) {
                            entity.hurt(ModDamageSource.getDamageSource(serverLevel, ModDamageSource.DISMISSED), Float.MAX_VALUE);
                            entity.playSound(ModSounds.ROAR_SPELL.get(), 0.5F, 2.0F);
                        }
                    }
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
