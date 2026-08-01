package za.co.infernos.goety.common.magic.spells.nether;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.HellBlast;
import za.co.infernos.goety.common.entities.projectiles.Lavaball;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.ColorUtil;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class LavaballSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LavaballCost, 0);
    }

    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LavaballDuration, 0);
    }

    @Override
    public SoundEvent CastingSound(LivingEntity caster) {
        if (CuriosFinder.hasUnholySet(caster)){
            return ModSounds.APOSTLE_PREPARE_SPELL.get();
        }
        return SoundEvents.EVOKER_PREPARE_ATTACK;
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LavaballCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NETHER;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.BURNING.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public ColorUtil particleColors(LivingEntity caster) {
        return new ColorUtil(1.0F, 0.0F, 0.0F);
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int burning = spellStat.getBurning();
        float radius = (float) spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getPotencyLevel(caster);
            burning += WandUtil.getLevels(ModEnchantments.BURNING.get(), caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.0F;
        }
        Vec3 vector3d = caster.getViewVector( 1.0F);
        AbstractHurtingProjectile fireballEntity = new Lavaball(worldIn,
                caster.getX() + vector3d.x / 2,
                caster.getEyeY() - 0.2,
                caster.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        if (CuriosFinder.hasUnholySet(caster)){
            fireballEntity = new HellBlast(caster.getX() + vector3d.x / 2,
                    caster.getEyeY() - 0.2,
                    caster.getZ() + vector3d.z / 2,
                    vector3d.x,
                    vector3d.y,
                    vector3d.z,
                    worldIn);
        }
        fireballEntity.setOwner(caster);
        if (fireballEntity instanceof Lavaball lavaball) {
            if (rightStaff(staff)) {
                lavaball.setUpgraded(true);
            }
            if (isShifting(caster)) {
                lavaball.setDangerous(false);
            }
            lavaball.setExtraDamage(potency);
            lavaball.setFiery(burning);
            lavaball.setExplosionPower(lavaball.getExplosionPower() + radius);
        } else if (fireballEntity instanceof HellBlast hellBlast){
            hellBlast.setExtraDamage(potency);
            hellBlast.setRadius(hellBlast.getRadius() + radius);
            hellBlast.setFiery(burning);
        }
        worldIn.addFreshEntity(fireballEntity);
        if (rightStaff(staff)) {
            for (int i = 0; i < 2; ++i) {
                AbstractHurtingProjectile fireballEntity1 = new Lavaball(worldIn,
                        caster.getX() + vector3d.x / 2 + worldIn.random.nextGaussian(),
                        caster.getEyeY() - 0.2,
                        caster.getZ() + vector3d.z / 2 + worldIn.random.nextGaussian(),
                        vector3d.x,
                        vector3d.y,
                        vector3d.z);
                if (CuriosFinder.hasUnholySet(caster)){
                    fireballEntity1 = new HellBlast(caster.getX() + vector3d.x / 2 + worldIn.random.nextGaussian(),
                            caster.getEyeY() - 0.2,
                            caster.getZ() + vector3d.z / 2 + worldIn.random.nextGaussian(),
                            vector3d.x,
                            vector3d.y,
                            vector3d.z,
                            worldIn);
                }
                fireballEntity1.setOwner(caster);
                if (fireballEntity1 instanceof Lavaball lavaball) {
                    lavaball.setUpgraded(true);
                    if (isShifting(caster)) {
                        lavaball.setDangerous(false);
                    }
                    lavaball.setExtraDamage(potency);
                    lavaball.setFiery(burning);
                    lavaball.setExplosionPower(lavaball.getExplosionPower() + radius);
                } else if (fireballEntity1 instanceof HellBlast hellBlast){
                    hellBlast.setExtraDamage(potency);
                    hellBlast.setRadius(hellBlast.getRadius() + radius);
                    hellBlast.setFiery(burning);
                }
                worldIn.addFreshEntity(fireballEntity1);
            }
        }
        this.playSound(worldIn, caster, SoundEvents.GHAST_SHOOT, 2.0F, this.projPitch(worldIn.getRandom()));
    }
}