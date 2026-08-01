package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.util.MagicGround;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.ModDamageSource;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
public class MagicBolt extends SpellHurtingProjectile {
    public static final EntityDataAccessor<Integer> DATA_EXTRA_DURATION = SynchedEntityData.defineId(MagicBolt.class, EntityDataSerializers.INT);

    public MagicBolt(EntityType<? extends MagicBolt> p_i50147_1_, Level p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
    }

    public MagicBolt(Level p_i1794_1_, LivingEntity p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_) {
        super(ModEntityType.MAGIC_BOLT.get(), p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_, p_i1794_1_);
    }

    public MagicBolt(Level p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
        super(ModEntityType.MAGIC_BOLT.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_, p_i1795_1_);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_EXTRA_DURATION, 0);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("ExtraDuration")) {
            this.setExtraDuration(compound.getInt("ExtraDuration"));
        }
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ExtraDuration", this.getExtraDuration());
    }

    public int getExtraDuration() {
        return this.entityData.get(DATA_EXTRA_DURATION);
    }

    public void setExtraDuration(int duration) {
        this.entityData.set(DATA_EXTRA_DURATION, duration);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= 100){
            this.discard();
        }

        if (this.level().isClientSide && this.level().isLoaded(this.blockPosition())) {
            Vec3 vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            this.level().addParticle(ParticleTypes.WITCH, d0 + this.level().random.nextDouble()/2, d1 + 0.5D, d2 + this.level().random.nextDouble()/2, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity target = pResult.getEntity();
        Entity owner = this.getOwner();
        LivingEntity livingentity = owner instanceof LivingEntity ? (LivingEntity)owner : null;
        float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.MagicBoltDamage, 1.0F) * WandUtil.damageMultiply();
        boolean flag;
        if (livingentity != null) {
            if (livingentity instanceof Mob mob && mob.getAttribute(Attributes.ATTACK_DAMAGE) != null){
                damage = (float) mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
            }
            damage += this.getExtraDamage();
            flag = target.hurt(ModDamageSource.magicBolt(this, livingentity), damage);
            if (flag) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    EnchantmentHelper.doPostAttackEffects(serverLevel, livingentity, this.damageSources().mobAttack(livingentity));
                }
            }
        } else {
            flag = target.hurt(ModDamageSource.magicBolt(this, this), 4.0F);
        }

        int duration = 100;
        if (flag){
            if (target instanceof LivingEntity livingTarget){
                if (livingentity != null) {
                    duration *= this.getExtraDuration() + 1;
                }
                livingTarget.addEffect(new MobEffectInstance(GoetyEffects.CURSED, duration));
            }
        }
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        if (!this.level().isClientSide) {
            boolean flag = true;
            if (p_37260_ instanceof EntityHitResult hitResult) {
                if (!this.canHitEntity(hitResult.getEntity())) {
                    flag = false;
                }
            }
            if (flag) {
                this.magicGround(p_37260_);
                this.discard();
            }
        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null){
            if (pEntity == this.getOwner()){
                return false;
            }
            if (this.getOwner() instanceof Mob mob && mob.getTarget() == pEntity){
                return super.canHitEntity(pEntity);
            } else {
                if (MobUtil.areAllies(this.getOwner(), pEntity)){
                    return false;
                }
                if (pEntity instanceof IOwned owned0 && this.getOwner() instanceof IOwned owned1){
                    return !MobUtil.ownerStack(owned0, owned1);
                }
            }
        }
        return super.canHitEntity(pEntity);
    }

    protected void magicGround(HitResult pResult){
        int duration = 100;
        MagicGround magicGround = new MagicGround(this.level(), this.getX(), this.getY(), this.getZ());
        if (pResult instanceof EntityHitResult entityHitResult){
            Entity entity = entityHitResult.getEntity();
            magicGround.setPos(entity.position());
        }
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity living){
            magicGround.setOwner(living);
            duration *= this.getExtraDuration() + 1;
        }
        magicGround.setDuration(duration);
        MobUtil.moveDownToGround(magicGround);
        this.level().addFreshEntity(magicGround);
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    protected boolean shouldBurn() {
        return false;
    }

    protected ParticleOptions getTrailParticle() {
        return ModParticleTypes.MAGIC_BOLT.get();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity p_345759_) {
        return new net.minecraft.network.protocol.game.ClientboundAddEntityPacket(this, p_345759_);
    }
}