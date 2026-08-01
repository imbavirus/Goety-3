package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.api.entities.ISpellEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;

public abstract class WaterHurtingProjectile extends AbstractHurtingProjectile implements ISpellEntity {
    private boolean started;
    private boolean generic;

    public void setGeneric(boolean p_36826_) {
        this.generic = p_36826_;
    }

    public boolean isGeneric() {
        return this.generic;
    }

    protected WaterHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }



    public WaterHurtingProjectile(EntityType<? extends WaterHurtingProjectile> p_36826_, double p_36827_, double p_36828_, double p_36829_, double p_36830_, double p_36831_, double p_36832_, Level p_36833_) {
        super((EntityType<? extends AbstractHurtingProjectile>) p_36826_, p_36833_);
        this.setPos(p_36827_, p_36828_, p_36829_);
        this.setDeltaMovement(p_36830_, p_36831_, p_36832_);
        this.setGeneric(true);
    }

    public boolean isAffectedByWater() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount == 1) {
            if (this.isGeneric()) {
                this.playSound(SoundEvents.GENERIC_SPLASH, 0.5F, 1.0F);
            }
            // Original hasBeenShot logic, adapted for first tick
            this.setDeltaMovement(this.getDeltaMovement().add(this.getDeltaMovement().normalize().scale(0.5D)));
        }

        Entity entity = this.getOwner();
        if (this.level().isClientSide
                || (entity == null || !entity.isRemoved()) && this.level().isLoaded(this.blockPosition())) {
            if (!this.started) {
                this.setDeltaMovement(this.getDeltaMovement().add(this.getDeltaMovement().normalize().scale(0.5D)));
                this.started = true;
            }
            // if (!this.leftOwner) {
            //     this.leftOwner = this.checkLeftOwner();
            // }
            // baseTick() is called by super.tick() in 1.20.1+
            if (this.shouldBurn()) {
                this.igniteForSeconds(1);
            }

            this.hitDetection();

            this.checkInsideBlocks();
            this.travel();
            this.trailParticle();
        } else {
            this.discard();
        }
    }

    /**
     * Stole these methods from @Iron:<a href=
     * "https://github.com/iron431/irons-spells-n-spellbooks/blob/1.20.1/src/main/java/io/redspace/ironsspellbooks/entity/spells/AbstractMagicProjectile.java">...</a>
     * From here
     */
    public void shoot(Vec3 rotation) {
        this.setDeltaMovement(rotation.scale(this.getInertia()));
    }

    public void hitDetection() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS
                && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }
    }

    public void travel() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        ProjectileUtil.rotateTowardsMovement(this, 0.2F);
        float f = this.getInertia();
        if (this.isInWater()) {
            if (this.isAffectedByWater()) {
                f = 0.8F;
            }
        }

        // this.setDeltaMovement(vec3.add(this.accelerationX, this.accelerationY, this.accelerationZ).scale(f));
        this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, this.getDefaultGravity(), 0.0D));
        this.setPos(d0, d1, d2);
    }

    /**
     * To here
     */

    public void trailParticle() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * f1, d1 - vec3.y * f1, d2 - vec3.z * f1,
                        vec3.x, vec3.y, vec3.z);
            }
        }
        this.level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public double getDefaultGravity() {
        return 0.0D;
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getOwner();
        if (entity != null) {
            for (Entity entity1 : this.level().getEntities(this,
                    this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D),
                    (p_37272_) -> !p_37272_.isSpectator() && p_37272_.isPickable())) {
                if (entity1.getRootVehicle() == entity.getRootVehicle()) {
                    return false;
                }
            }
        }

        return true;
    }
}