package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class CorruptedBeam extends AbstractBeam {

    public CorruptedBeam(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        // super.defineSynchedData(builder);
    }

    public CorruptedBeam(EntityType<?> p_i48580_1_, Level p_i48580_2_, LivingEntity owner) {
        super(p_i48580_1_, p_i48580_2_);
        this.setOwner(owner);
    }

    public void damageEntities(Set<LivingEntity> entities){
        for (LivingEntity entity : entities) {
            entity.invulnerableTime = 0;
            Vec3 deltaMovement = entity.getDeltaMovement();
            float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.CorruptedBeamDamage, 1.0F) * WandUtil.damageMultiply();
            damage += this.extraDamage;
            entity.hurt(entity.damageSources().indirectMagic(owner, owner), damage);
            entity.setDeltaMovement(deltaMovement);
        }
    }
}