package za.co.infernos.goety.common.magic.spells;

import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.client.particles.ReverseShockwaveParticleOption;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.Doppelganger;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SSetPlayerOwnerPacket;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class IllusionSpell extends Spell {
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.IllusionCost, 0);
    }

    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.IllusionDuration, 0);
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.IllusionCoolDown, 0);
    }

    @Override
    public ColorUtil particleColors(LivingEntity caster) {
        return new ColorUtil(0.3F, 0.3F, 0.8F);
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        for (Entity entity : worldIn.getAllEntities()) {
            if (entity instanceof Doppelganger doppelganger) {
                if (doppelganger.getTrueOwner() == caster && doppelganger.tickCount > 10) {
                    doppelganger.die(caster.damageSources().starve());
                }
            }
        }
        int i0 = 4;
        if (staff.is(ModItems.NAMELESS_STAFF.get())){
            i0 = 8;
        }
        boolean undead = CuriosFinder.hasNamelessSet(caster) && staff.is(ModItems.NAMELESS_STAFF.get());
        ParticleOptions particleOptions = ParticleTypes.POOF;
        if (undead){
            particleOptions = ModParticleTypes.LICH.get();
        }
        for (int i1 = 0; i1 < i0; ++i1) {
            Doppelganger summonedentity = new Doppelganger(ModEntityType.DOPPELGANGER.get(), worldIn);
            summonedentity.setTrueOwner(caster);
            if (caster instanceof Player player) {
                ModNetwork.sendTo(player, new SSetPlayerOwnerPacket(summonedentity));
            }
            Vec3 vec3 = BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn).getCenter();
            summonedentity.setPos(vec3);
            summonedentity.setUndeadClone(undead);
            summonedentity.setLimitedLife(undead ? MathHelper.secondsToTicks(2.875F) : 1200);
            summonedentity.setPersistenceRequired();
            summonedentity.setUpgraded(CuriosFinder.hasIllusionRobe(caster));
            summonedentity.finalizeSpawn(worldIn, caster.level().getCurrentDifficultyAt(BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn)), MobSpawnType.MOB_SUMMONED, null);
            LivingEntity target = this.getTarget(caster);
            if (target != null) {
                double d2 = target.getX() - summonedentity.getX();
                double d1 = target.getZ() - summonedentity.getZ();
                summonedentity.setYRot(-((float)Mth.atan2(d2, d1)) * (180F / (float)Math.PI));
                if (undead){
                    float f = (float) Mth.atan2(d1, d2);
                    float f2 = f + (float) i1 * (float) Math.PI * 0.25F + 4.0F;
                    vec3 = new Vec3(target.getX() + (double) Mth.cos(f2) * 4.0D, target.getY(), target.getZ() + (double) Mth.sin(f2) * 4.0D);
                    summonedentity.setPos(vec3);
                }
            }
            MobUtil.moveDownToGround(summonedentity);
            worldIn.addFreshEntity(summonedentity);
            for (int i = 0; i < caster.level().random.nextInt(10) + 10; ++i) {
                ServerParticleUtil.smokeParticles(particleOptions, summonedentity.getX(), summonedentity.getY(), summonedentity.getZ(), worldIn);
            }
            if (undead){
                worldIn.sendParticles(new ReverseShockwaveParticleOption(new ColorUtil(0x36e416), 2.0F, 0.5F, 1), summonedentity.getX(), summonedentity.getY() + 0.5F, summonedentity.getZ(), 0, 0, 0, 0, 0.5F);
            }
        }
        if (CuriosFinder.hasIllusionRobe(caster)){
            caster.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200));
            LivingEntity target = this.getTarget(caster);
            if (target != null) {
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400));
            }
        }
        SoundEvent soundEvent = SoundEvents.ILLUSIONER_MIRROR_MOVE;
        if (undead){
            soundEvent = ModSounds.LICH_TELEPORT_IN.get();
        }
        this.playSound(worldIn, caster, soundEvent);
        for (int i = 0; i < caster.level().random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(particleOptions, caster.getX(), caster.getEyeY(), caster.getZ(), 0, 0.0F, 0.0F, 0.0F, 0);
        }
    }

}

