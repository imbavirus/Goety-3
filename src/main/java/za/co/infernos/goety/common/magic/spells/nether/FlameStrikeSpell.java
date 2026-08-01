package za.co.infernos.goety.common.magic.spells.nether;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.util.FirePillar;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FlameStrikeSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setDuration(180).setRadius(3.0D);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FlameStrikeCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FlameStrikeDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.RUMBLE.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FlameStrikeCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NETHER;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public void startSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int warmUp = this.castDuration(caster, staff) - 10;
        int duration = spellStat.getDuration() * (WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1);
        int range = spellStat.getRange() + WandUtil.getRangeLevel(caster);
        LivingEntity target = this.getTarget(caster, range);
        if (target != null && !this.isShifting(caster)){
            double radius = spellStat.getRadius();
            if (rightStaff(staff)){
                radius *= 2.0D;
            }
            List<Vec3> vec3s = BlockFinder.buildOuterBlockCircle(target.position(), radius);
            for (Vec3 vec3 : vec3s) {
                FirePillar flames = new FirePillar(worldIn, vec3.x, vec3.y, vec3.z);
                flames.setOwner(caster);
                flames.setDuration(duration);
                flames.setWarmUp(warmUp);
                flames.setExtraDamage(WandUtil.getPotencyLevel(caster));
                MobUtil.moveDownToGround(flames);
                worldIn.addFreshEntity(flames);
            }
        } else {
            Vec3 vector3d = caster.getViewVector(1.0F);
            float f = (float) Mth.atan2(vector3d.z - caster.getZ(), vector3d.x - caster.getX());
            for (int k = 0; k < 8; ++k) {
                float f2 = f + (float) k * (float) Math.PI * 0.25F + 1.0F;
                FirePillar flames = new FirePillar(worldIn, caster.getX() + (double) Mth.cos(f2), caster.getY(), caster.getZ() + (double) Mth.sin(f2));
                flames.setOwner(caster);
                flames.setDuration(duration);
                flames.setWarmUp(warmUp);
                flames.setExtraDamage(WandUtil.getPotencyLevel(caster));
                MobUtil.moveDownToGround(flames);
                worldIn.addFreshEntity(flames);
            }

            for (int k = 0; k < 8; ++k) {
                float f2 = f + (float) k * (float) Math.PI * 0.25F + 3.0F;
                FirePillar flames = new FirePillar(worldIn, caster.getX() + (double) Mth.cos(f2) * 3.0D, caster.getY(), caster.getZ() + (double) Mth.sin(f2) * 3.0D);
                flames.setOwner(caster);
                flames.setDuration(duration);
                flames.setWarmUp(warmUp);
                flames.setExtraDamage(WandUtil.getPotencyLevel(caster));
                MobUtil.moveDownToGround(flames);
                worldIn.addFreshEntity(flames);
            }

            if (rightStaff(staff)) {
                for (int k = 0; k < 8; ++k) {
                    float f2 = f + (float) k * (float) Math.PI * 0.25F + 6.0F;
                    FirePillar flames = new FirePillar(worldIn, caster.getX() + (double) Mth.cos(f2) * 6.0D, caster.getY(), caster.getZ() + (double) Mth.sin(f2) * 6.0D);
                    flames.setOwner(caster);
                    flames.setDuration(duration);
                    flames.setWarmUp(warmUp);
                    flames.setExtraDamage(WandUtil.getPotencyLevel(caster));
                    MobUtil.moveDownToGround(flames);
                    worldIn.addFreshEntity(flames);
                }

                for (int k = 0; k < 16; ++k) {
                    float f2 = f + (float) k * (float) Math.PI * 0.25F + 9.0F;
                    FirePillar flames = new FirePillar(worldIn, caster.getX() + (double) Mth.cos(f2) * 9.0F, caster.getY(), caster.getZ() + (double) Mth.sin(f2) * 9.0F);
                    flames.setOwner(caster);
                    flames.setDuration(duration);
                    flames.setWarmUp(warmUp);
                    flames.setExtraDamage(WandUtil.getPotencyLevel(caster));
                    MobUtil.moveDownToGround(flames);
                    worldIn.addFreshEntity(flames);
                }
            }
        }
    }
}