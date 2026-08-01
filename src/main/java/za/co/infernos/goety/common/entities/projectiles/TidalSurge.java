package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.ModDamageSource;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TidalSurge extends AbstractWave {

    public TidalSurge(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public TidalSurge(Level level, LivingEntity shooter) {
        this(ModEntityType.TIDAL_SURGE.get(), level);
        this.setOwner(shooter);
    }

    public void tick() {
        super.tick();
        if (this.tickCount % 5 == 0){
            this.playSound(SoundEvents.GENERIC_SWIM, 0.15F, 1.0F);
        }
        if (this.level().isClientSide) {
            for(int h = 0; h < this.getWaveScale(); h++){
                for (int i = 0; i <= 8; i++) {
                    float xOffset = (float) i / 4.0F - 0.5F + (random.nextFloat() - 0.5F) * 0.2F;
                    this.spawnCloudParticleAt((0.2F + random.nextFloat() * 0.2F) * this.getWaveScale(), 1.2F, xOffset * 1.2F * this.getWaveScale());
                    this.spawnParticleAt((0.2F + random.nextFloat() * 0.2F) * this.getWaveScale(), -0.2F, xOffset * 1.4F * this.getWaveScale(), ParticleTypes.SPLASH);
                }
            }
        }
    }

    public void spawnCloudParticleAt(float yOffset, float zOffset, float xOffset) {
        Vec3 vec3 = new Vec3(xOffset, yOffset, zOffset).yRot((float) Math.toRadians(-this.getYRot()));
        this.level().addParticle(ModParticleTypes.SPELL_CLOUD.get(), this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z, 1.0F, 1.0F, 1.0F);
    }

    public void attackEntities(float scale) {
        DamageSource source = ModDamageSource.indirectDrench(this, this.getOwner());
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5F * scale, 0.5F, 0.5F * scale))) {
            if (!MobUtil.areAllies(entity, this.getOwner() != null ? this.getOwner() : this)) {
                float damage = scale + za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.TidalBaseDamage, 1.0F);
                if (entity.isSensitiveToWater()){
                    damage += 1.0F;
                }
                damage *= WandUtil.damageMultiply();
                entity.hurt(source, damage);
                this.setSlamming(true);
                entity.knockback(0.1D + 0.5D * scale, (double) Mth.sin(this.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(this.getYRot() * ((float) Math.PI / 180F))));
            }
        }
    }
}
