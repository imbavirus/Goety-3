package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.utils.ModDamageSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class RazorWind extends SlashProjectile {
    public RazorWind(EntityType<? extends SlashProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public RazorWind(Level levelIn, LivingEntity shooter) {
        super(ModEntityType.RAZOR_WIND.get(), levelIn, shooter);
    }

    public ParticleOptions getParticle(){
        return ParticleTypes.CLOUD;
    }

    public void damageEntity(Entity entity) {
        DamageSource damageSource = entity.damageSources().magic();
        if (this.getOwner() instanceof LivingEntity livingEntity) {
            damageSource = ModDamageSource.windBlast(this, livingEntity);
        }
        entity.hurt(damageSource, this.getDamage());
    }
}