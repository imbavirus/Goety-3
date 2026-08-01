package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.boss.Apostle;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.LichdomHelper;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
public class DeathArrow extends Arrow {

    public DeathArrow(EntityType<? extends Arrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    public DeathArrow(Level p_36866_, LivingEntity p_36867_) {
        super(p_36866_, p_36867_, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ARROW), null);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntityType.DEATH_ARROW.get();
    }

    protected void doPostHurtEffects(LivingEntity livingEntity) {
        super.doPostHurtEffects(livingEntity);
        livingEntity.invulnerableTime = 0;

        if (this.getOwner() instanceof Apostle apostle) {
            if (apostle.isInNether()) {
                float voidDamage = livingEntity.getMaxHealth() * 0.05F;

                if (livingEntity.getHealth() > voidDamage + 1.0F) {
                    livingEntity.heal(-voidDamage);
                }
            }
            if (!apostle.isSmited()) {
                if (MobUtil.healthIsHalved(apostle)) {
                    apostle.heal(4.0F);
                } else {
                    apostle.heal(1.0F);
                }
            }
        } else if (this.getOwner() instanceof LivingEntity livingOwner){
            if (CuriosFinder.hasUnholySet(livingOwner)){
                if (livingOwner.level().dimension() == Level.NETHER) {
                    float voidDamage = livingEntity.getMaxHealth() * 0.05F;

                    if (livingEntity.getHealth() > voidDamage + 1.0F) {
                        livingEntity.heal(-voidDamage);
                    }
                }

                if (livingOwner instanceof Player player && LichdomHelper.isLich(player)) {
                    if (LichdomHelper.smited(player) <= 0) {
                        if (MobUtil.healthIsHalved(player)) {
                            player.heal(4.0F);
                        } else {
                            player.heal(1.0F);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 200 && !this.inGround){
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        float f = (float)this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double)f * this.getBaseDamage(), 0.0D, 2.147483647E9D));
        if (this.isCritArrow()) {
            long j = (long)this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }
        Entity entity = result.getEntity();
        if (entity instanceof WitherBoss witherBoss){
            Entity entity1 = this.getOwner();
            if (entity1 instanceof Apostle apostle) {
                if (apostle.getTarget() == witherBoss || witherBoss.getTarget() == apostle){
                    witherBoss.hurt(this.damageSources().indirectMagic(this, apostle), i);
                }
            } else if (entity1 instanceof LivingEntity livingEntity && CuriosFinder.hasUnholySet(livingEntity)){
                witherBoss.hurt(this.damageSources().indirectMagic(this, livingEntity), i);
            }
        }
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            if (!this.inGround) {
                for (int p = 0; p < 32; ++p) {
                    double d0 = (double) this.getX() + this.level().random.nextDouble();
                    double d1 = (double) this.getY() + this.level().random.nextDouble();
                    double d2 = (double) this.getZ() + this.level().random.nextDouble();
                    serverLevel.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.45, 0.45, 0.45, 0.5F);
                }
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

    // getAddEntityPacket is no longer needed in 1.21 - handled automatically
    // @Override
    // public Packet<ClientGamePacketListener> getAddEntityPacket() {
    //     return new net.minecraft.network.protocol.game.ClientboundAddEntityPacket(this);
    // }
}