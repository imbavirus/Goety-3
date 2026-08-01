package za.co.infernos.goety.common.magic.spells.geomancy;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.EarthFist;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EarthFistSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(8.0D);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.EarthFistCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.EarthFistDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SUMMON.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.EarthFistCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.GEOMANCY;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    public List<LivingEntity> getLivingEntities(ServerLevel worldIn, LivingEntity caster, SpellStat spellStat){
        int i = (int) caster.getX();
        int j = (int) caster.getY();
        int k = (int) caster.getZ();
        double radius = spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)){
            radius *= (WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.0D) + 1.0D;
        }
        return worldIn.getEntitiesOfClass(LivingEntity.class, (new AABB(i, j, k, i, j - 4, k)).inflate(radius), livingEntity -> !MobUtil.areAllies(livingEntity, caster));
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int range = spellStat.getRange();
        int potency = spellStat.getPotency();
        if (WandUtil.enchantedFocus(caster)) {
            range += WandUtil.getRangeLevel(caster);
            potency += WandUtil.getPotencyLevel(caster);
        }
        HitResult rayTraceResult = this.rayTrace(worldIn, caster, range, 3);
        LivingEntity target = this.getTarget(caster, range);
        Vec3 vec3 = null;
        if (target != null){
            vec3 = target.position();
        } else if (rayTraceResult instanceof BlockHitResult result){
            vec3 = result.getLocation();
        }
        if (this.rightStaff(staff) && !this.getLivingEntities(worldIn, caster, spellStat).isEmpty()) {
            for (LivingEntity entity : this.getLivingEntities(worldIn, caster, spellStat)) {
                float f = (float) Mth.atan2(entity.getZ() - caster.getZ(), entity.getX() - caster.getX());
                EarthFist earthFist = new EarthFist(worldIn, entity.position(), caster);
                earthFist.setExtraDamage(potency);
                earthFist.setWarmupDelayTicks(10);
                earthFist.setYRot(f * (180.0F / (float)Math.PI));
                worldIn.addFreshEntity(earthFist);
                worldIn.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.RUMBLE.get(), this.getSoundSource(), 1.0F, 1.0F);
            }
        } else if (vec3 != null) {
            float f = (float) Mth.atan2(vec3.z - caster.getZ(), vec3.x - caster.getX());
            EarthFist earthFist = new EarthFist(worldIn, vec3, caster);
            earthFist.setExtraDamage(potency);
            earthFist.setWarmupDelayTicks(10);
            earthFist.setYRot(f * (180.0F / (float)Math.PI));
            worldIn.addFreshEntity(earthFist);
            worldIn.playSound(null, vec3.x, vec3.y, vec3.z, ModSounds.RUMBLE.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
    }
}