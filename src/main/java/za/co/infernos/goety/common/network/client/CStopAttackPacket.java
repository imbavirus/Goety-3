package za.co.infernos.goety.common.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.MobUtil;

public record CStopAttackPacket() implements CustomPacketPayload {
    public static final Type<CStopAttackPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "stop_attack"));

    public static final StreamCodec<FriendlyByteBuf, CStopAttackPacket> STREAM_CODEC =
            StreamCodec.unit(new CStopAttackPacket());

    public static void handle(CStopAttackPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer playerEntity && playerEntity.level() instanceof ServerLevel serverLevel) {
                for (Entity entity : serverLevel.getAllEntities()) {
                    if (entity instanceof IOwned owned && entity instanceof Mob mob && owned.getTrueOwner() == playerEntity) {
                        Entity pick = MobUtil.getSingleTarget(serverLevel, playerEntity, 16, 3);
                        if (pick instanceof LivingEntity target
                                && target != mob
                                && target != owned.getTrueOwner()
                                && !MobUtil.areAllies(playerEntity, target)
                                && mob.distanceTo(playerEntity) <= 32) {
                            mob.setTarget(target);
                            if (mob.getLastHurtByMob() != target) {
                                mob.setLastHurtByMob(target);
                            }
                            entity.playSound(ModSounds.ROAR_SPELL.get(), 0.5F, 2.0F);
                            owned.onStopAttack();
                        } else {
                            owned.onCeaseFire(playerEntity);
                            if (mob.getTarget() != null) {
                                mob.setTarget(null);
                                if (mob.getLastHurtByMob() != null) {
                                    mob.setLastHurtByMob(null);
                                }
                                if (playerEntity.getLastHurtMob() != null) {
                                    playerEntity.setLastHurtMob(null);
                                }
                                if (mob.getBrain() != null) {
                                    mob.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
                                    mob.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
                                    mob.getBrain().eraseMemory(MemoryModuleType.HURT_BY);
                                }
                                if (mob instanceof NeutralMob neutralMob) {
                                    neutralMob.setPersistentAngerTarget(null);
                                    neutralMob.stopBeingAngry();
                                }
                                mob.setAggressive(false);
                                entity.playSound(ModSounds.CAST_SPELL.get(), 1.0F, 1.0F);
                                owned.onStopAttack();
                            }
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
