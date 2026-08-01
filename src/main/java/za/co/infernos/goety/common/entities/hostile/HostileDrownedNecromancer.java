package za.co.infernos.goety.common.entities.hostile;

import za.co.infernos.goety.common.entities.neutral.AbstractNecromancer;
import za.co.infernos.goety.common.entities.neutral.DrownedNecromancer;
import za.co.infernos.goety.common.network.ModServerBossInfo;
import za.co.infernos.goety.config.MainConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class HostileDrownedNecromancer extends DrownedNecromancer implements Enemy {
    private final ModServerBossInfo bossInfo;

    public HostileDrownedNecromancer(EntityType<? extends AbstractNecromancer> type, Level level) {
        super(type, level);
        this.bossInfo = new ModServerBossInfo(this, BossEvent.BossBarColor.BLUE, false, false);
        this.setHostile(true);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.SpecialBossBar, false)) {
            this.bossInfo.addPlayer(pPlayer);
        }
    }

    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        this.bossInfo.removePlayer(pPlayer);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 5 == 0) {
            this.bossInfo.update();
        }
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() != null){
            if (pSource.getEntity() instanceof LivingEntity livingEntity){
                if (!(livingEntity instanceof Drowned) && !livingEntity.isAlliedTo(this)){
                    for (Drowned drowned : this.level().getEntitiesOfClass(Drowned.class, this.getBoundingBox().inflate(10))){
                        if (drowned.getTarget() != livingEntity) {
                            if (drowned.canAttack(livingEntity)) {
                                drowned.setTarget(livingEntity);
                            }
                        }
                    }
                }
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (super.isAlliedTo(pEntity)) {
            return true;
        } else if (pEntity instanceof Drowned) {
            return this.getTeam() == null && pEntity.getTeam() == null;
        } else {
            return false;
        }
    }
}
