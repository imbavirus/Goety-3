package za.co.infernos.goety.common.entities.ally.undead.skeleton;

import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

import javax.annotation.Nullable;

public class WitherSkeletonServant extends AbstractSkeletonServant {
   public WitherSkeletonServant(EntityType<? extends WitherSkeletonServant> p_34166_, Level p_34167_) {
      super(p_34166_, p_34167_);
      this.setPathfindingMalus(PathType.LAVA, 8.0F);
   }

   public static AttributeSupplier.Builder setCustomAttributes() {
      return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WitherSkeletonServantHealth, 20.0D))
            .add(Attributes.ARMOR, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WitherSkeletonServantArmor, 20.0D))
            .add(Attributes.MOVEMENT_SPEED, 0.25F)
            .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WitherSkeletonServantDamage, 20.0D));
   }

   public void setConfigurableAttributes() {
      MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH),
            za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WitherSkeletonServantHealth, 20.0D));
      MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WitherSkeletonServantArmor, 20.0D));
      MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE),
            za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WitherSkeletonServantDamage, 20.0D));
   }

   @Override
   public double getBaseRangeDamage() {
      return za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WitherSkeletonServantRangeDamage, 20.0D);
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.WITHER_SKELETON_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource p_34195_) {
      return SoundEvents.WITHER_SKELETON_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.WITHER_SKELETON_DEATH;
   }

   protected SoundEvent getStepSound() {
      return SoundEvents.WITHER_SKELETON_STEP;
   }

   protected void populateDefaultEquipmentSlots(RandomSource p_219154_, DifficultyInstance p_219155_) {
      if (this.canSpawnArmor()) {
         super.populateDefaultEquipmentSlots(p_219154_, p_219155_);
      }
      this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
      this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
   }

   protected void populateDefaultEquipmentEnchantments(RandomSource p_219157_, DifficultyInstance p_219158_) {
   }

   protected float getStandingEyeHeight(Pose p_34186_, EntityDimensions p_34187_) {
      return 2.1F;
   }

   public boolean doHurtTarget(Entity p_34169_) {
      if (!super.doHurtTarget(p_34169_)) {
         return false;
      } else {
         if (p_34169_ instanceof LivingEntity) {
            ((LivingEntity) p_34169_).addEffect(new MobEffectInstance(MobEffects.WITHER, 200), this);
         }

         return true;
      }
   }

   protected AbstractArrow getMobArrow(ItemStack p_34189_, float p_34190_) {
      AbstractArrow abstractarrow = super.getMobArrow(p_34189_, p_34190_);
      abstractarrow.igniteForSeconds(100);
      return abstractarrow;
   }

   public boolean canBeAffected(MobEffectInstance p_34192_) {
      return p_34192_.getEffect() != MobEffects.WITHER && super.canBeAffected(p_34192_);
   }

   public EntityType<?> getVariant(@Nullable Player player, Level level, BlockPos blockPos) {
      return ModEntityType.WITHER_SKELETON_SERVANT.get();
   }
}