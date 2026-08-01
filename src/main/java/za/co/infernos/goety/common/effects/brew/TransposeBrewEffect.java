package za.co.infernos.goety.common.effects.brew;

import za.co.infernos.goety.config.BrewConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

public class TransposeBrewEffect extends BrewEffect{
    public TransposeBrewEffect() {
        super("transpose", za.co.infernos.goety.utils.ConfigHelper.getInt(BrewConfig.TransposeCost, 0), MobEffectCategory.BENEFICIAL, 0x2b0178);
    }

    @Override
    public void applyBlockEffect(Level pLevel, BlockPos pPos, LivingEntity pSource, int pAmplifier, int pAreaOfEffect) {
        for (BlockPos blockPos : this.getSpherePos(pPos, pAreaOfEffect + 4)){
            for (Entity entity : pLevel.getEntitiesOfClass(Entity.class, new AABB(blockPos))){
                if (entity instanceof ItemEntity || entity instanceof LivingEntity) {
                    if (entity instanceof LivingEntity livingEntity) {
                        net.neoforged.neoforge.event.entity.EntityTeleportEvent.EnderEntity event = new net.neoforged.neoforge.event.entity.EntityTeleportEvent.EnderEntity(livingEntity, blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(event);
                        if (event.isCanceled()) {
                            break;
                        }
                    }
                    entity.teleportTo(pPos.getX(), pPos.getY() + 1, pPos.getZ());
                    pLevel.gameEvent(GameEvent.TELEPORT, entity.position(), GameEvent.Context.of(entity));
                    if (!entity.isSilent()) {
                        pLevel.playSound((Player) null, entity.xo, entity.yo, entity.zo, SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1.0F, 1.0F);
                        entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                    pLevel.broadcastEntityEvent(entity, (byte) 46);
                }
            }
        }
    }
}