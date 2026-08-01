package za.co.infernos.goety.mixin;

import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.illager.WitchServant;
import za.co.infernos.goety.utils.CuriosFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {

    public VillagerMixin(EntityType<? extends AbstractVillager> p_35267_, Level p_35268_) {
        super(p_35267_, p_35268_);
    }

    @Inject(at = @At("HEAD"), method = "thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V", cancellable = true)
    public void goetyThunderHit(ServerLevel serverLevel, LightningBolt lightningBolt, CallbackInfo ci) {
        if (lightningBolt.getCause() != null) {
            if (CuriosFinder.hasUnholySet(lightningBolt.getCause())) {
                ci.cancel();
                WitchServant witch = ModEntityType.WITCH_SERVANT.get().create(serverLevel);
                if (witch != null) {
                    witch.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                    witch.setTrueOwner(lightningBolt.getCause());
                    witch.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(witch.blockPosition()), MobSpawnType.CONVERSION, null);
                    witch.setNoAi(this.isNoAi());
                    if (this.hasCustomName()) {
                        witch.setCustomName(this.getCustomName());
                        witch.setCustomNameVisible(this.isCustomNameVisible());
                    }

                    witch.setPersistenceRequired();
                    EventHooks.onLivingConvert(this, witch);
                    serverLevel.addFreshEntityWithPassengers(witch);
                    this.releaseAllPois();
                    this.discard();
                }
            }
        }
    }

    @Shadow
    private void releaseAllPois() {
    }
}