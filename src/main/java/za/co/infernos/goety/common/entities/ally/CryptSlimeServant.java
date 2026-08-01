package za.co.infernos.goety.common.entities.ally;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.entities.neutral.Owned;
import za.co.infernos.goety.utils.ModLootTables;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class CryptSlimeServant extends SlimeServant {
    public CryptSlimeServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    protected ParticleOptions getParticleType() {
        return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.BONE));
    }

    // Loot table override commented out - incompatible return type in 1.21
    // @Override
    // protected ResourceLocation getDefaultLootTable() {
    //     return EntityType.SLIME.getDefaultLootTable();
    // }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel serverLevel, DamageSource pSource, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(serverLevel, pSource, pRecentlyHit);
        if (serverLevel.getServer() != null) {
            LootTable loottable = serverLevel.getServer().reloadableRegistries()
                    .getLootTable(ModLootTables.CRYPT_SLIME);
            LootParams.Builder lootparams$builder = (new LootParams.Builder(serverLevel))
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, pSource)
                    .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, pSource.getEntity())
                    .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, pSource.getDirectEntity());
            if (this.lastHurtByPlayerTime > 0 && this.lastHurtByPlayer != null) {
                lootparams$builder = lootparams$builder
                        .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer)
                        .withLuck(this.lastHurtByPlayer.getLuck());
            }

            LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
            loottable.getRandomItems(lootparams, this.getLootTableSeed(), this::spawnAtLocation);
        }
    }

    public boolean causeFallDamage(float p_149717_, float p_149718_, DamageSource p_149719_) {
        return false;
    }

    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    protected void dealDamage(LivingEntity livingEntity) {
        if (this.isAlive()) {
            int i = this.getSize();
            if (this.distanceToSqr(livingEntity) < 0.6D * (double) i * 0.6D * (double) i
                    && this.hasLineOfSight(livingEntity)
                    && livingEntity.hurt(this.getServantAttack(), this.getAttackDamage())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F,
                        (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                // doEnchantDamageEffects is no longer available in 1.21
                // this.doEnchantDamageEffects(this, livingEntity);
                livingEntity.addEffect(new MobEffectInstance(GoetyEffects.SAPPED, 60, this.getSize()));
            }
        }
    }
}