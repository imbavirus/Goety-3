package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.ModDamageSource;
import za.co.infernos.goety.utils.ServerParticleUtil;
import za.co.infernos.goety.utils.WandUtil;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class IceSpear extends IceSpike {
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;
    @Nullable
    private List<Entity> piercedAndKilledEntities;

    public IceSpear(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    public IceSpear(double p_36712_, double p_36713_, double p_36714_, Level p_36715_) {
        super(p_36712_, p_36713_, p_36714_, p_36715_);
    }

    public IceSpear(LivingEntity p_36718_, Level p_36719_) {
        super(p_36718_, p_36719_);
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return ModEntityType.ICE_SPEAR.get();
    }

    @Override
    public byte getPierceLevel() {
        if (this.getOwner() instanceof LivingEntity livingEntity){
            return (byte) (WandUtil.getPotencyLevel(livingEntity) + 1);
        }
        return super.getPierceLevel();
    }

    private void resetPiercedEntities() {
        if (this.piercedAndKilledEntities != null) {
            this.piercedAndKilledEntities.clear();
        }

        if (this.piercingIgnoreEntityIds != null) {
            this.piercingIgnoreEntityIds.clear();
        }
    }

    protected void onHitEntity(EntityHitResult p_37626_) {
        if (!this.level().isClientSide) {
            float baseDamage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.IceSpikeDamage, 1.0F) * WandUtil.damageMultiply();
            Entity entity = p_37626_.getEntity();
            if (this.getPierceLevel() > 0) {
                if (this.piercingIgnoreEntityIds == null) {
                    this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
                }

                if (this.piercedAndKilledEntities == null) {
                    this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
                }

                if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                    this.discard();
                    return;
                }

                this.piercingIgnoreEntityIds.add(entity.getId());
            }
            Entity entity1 = this.getOwner();
            boolean flag;
            baseDamage += this.getExtraDamage();
            if (entity1 instanceof LivingEntity livingentity) {
                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }
                flag = entity.hurt(ModDamageSource.iceSpike(this, livingentity), baseDamage);
                if (flag) {
                    if (entity.isAlive()) {
                        // Enchant damage effects commented out in 1.21
                    }
                }
            } else {
                flag = entity.hurt(ModDamageSource.iceSpike(this, this), baseDamage);
            }

            if (flag && entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(GoetyEffects.FREEZING, MathHelper.secondsToTicks(3 + livingEntity.getRandom().nextInt(2))));
                this.playSound(ModSounds.ICE_SPIKE_HIT.get(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                if (livingEntity.level() instanceof ServerLevel serverLevel){
                    ServerParticleUtil.addParticlesAroundSelf(serverLevel, new BlockParticleOption(ParticleTypes.BLOCK, Blocks.PACKED_ICE.defaultBlockState()), livingEntity);
                }
                if (this.getPierceLevel() <= 0) {
                    this.discard();
                }
            }

        }
    }

    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        this.resetPiercedEntities();
    }

    protected boolean canHitEntity(Entity p_36743_) {
        return super.canHitEntity(p_36743_) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(p_36743_.getId()));
    }
}
