package za.co.infernos.goety.common.magic.spells.nether;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.ModWitherSkull;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class WitherSkullSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WitherSkullCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WitherSkullDuration, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.WITHER_SHOOT;
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WitherSkullCoolDown, 0);
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
        list.add(ModEnchantments.BURNING.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        Vec3 vector3d = caster.getViewVector( 1.0F);
        float extraBlast = (float) (spellStat.getRadius() + WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.0F);
        ModWitherSkull witherSkull = new ModWitherSkull(
                caster.getX() + vector3d.x / 2,
                caster.getEyeY() - 0.2,
                caster.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z, worldIn);
        witherSkull.setOwner(caster);
        if (isShifting(caster)){
            witherSkull.setDangerous(true);
        }
        witherSkull.setExtraDamage(spellStat.getPotency() + WandUtil.getPotencyLevel(caster));
        witherSkull.setFiery(spellStat.getBurning() + WandUtil.getLevels(ModEnchantments.BURNING.get(), caster));
        witherSkull.setExplosionPower(witherSkull.getExplosionPower() + extraBlast);
        worldIn.addFreshEntity(witherSkull);
        if (rightStaff(staff)) {
            for (int i = 0; i < 2; ++i) {
                ModWitherSkull witherSkull1 = new ModWitherSkull(
                        caster.getX() + vector3d.x / 2 + worldIn.random.nextGaussian(),
                        caster.getEyeY() - 0.2,
                        caster.getZ() + vector3d.z / 2 + worldIn.random.nextGaussian(),
                        vector3d.x,
                        vector3d.y,
                        vector3d.z, worldIn);
                witherSkull1.setOwner(caster);
                if (isShifting(caster)) {
                    witherSkull1.setDangerous(true);
                }
                witherSkull1.setExtraDamage(spellStat.getPotency() + WandUtil.getPotencyLevel(caster));
                witherSkull1.setFiery(spellStat.getBurning() + WandUtil.getLevels(ModEnchantments.BURNING.get(), caster));
                witherSkull1.setExplosionPower(witherSkull.getExplosionPower() + extraBlast);
                worldIn.addFreshEntity(witherSkull1);
            }
        }
        this.playSound(worldIn, caster, 2.0F, (caster.getRandom().nextFloat() - caster.getRandom().nextFloat()) * 0.2F + 1.0F);
    }
}