package za.co.infernos.goety.common.magic.spells;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.*;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.SoundUtil;
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

/**
 * Learned you could use this method for better projectile accuracy from codes by @Yunus1903
 */
public class SoulBoltSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulBoltCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulBoltDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulBoltCoolDown, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.CAST_SPELL.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.VELOCITY.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        float velocity = spellStat.getVelocity();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getPotencyLevel(caster);
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster);
        }
        Vec3 vector3d = caster.getViewVector( 1.0F);
        SpellHurtingProjectile soulBolt = new SoulBolt(
                caster.getX() + vector3d.x / 2,
                caster.getEyeY() - 0.2,
                caster.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z, worldIn);
        if (this.typeStaff(staff, SpellType.WILD)) {
            soulBolt = new PoisonBolt(
                    caster.getX() + vector3d.x / 2,
                    caster.getEyeY() - 0.2,
                    caster.getZ() + vector3d.z / 2,
                    vector3d.x,
                    vector3d.y,
                    vector3d.z, worldIn);
            SoundUtil.playSoulBolt(caster);
        } else if (this.typeStaff(staff, SpellType.NETHER) && CuriosFinder.hasNetherSet(caster)) {
            soulBolt = new WitherBolt(
                    caster.getX() + vector3d.x / 2,
                    caster.getEyeY() - 0.2,
                    caster.getZ() + vector3d.z / 2,
                    vector3d.x,
                    vector3d.y,
                    vector3d.z, worldIn);
            this.playSound(worldIn, caster, SoundEvents.WITHER_SHOOT, 0.5F, 0.25F);
            this.playSound(worldIn, caster, ModSounds.HELL_BOLT_SHOOT.get());
        } else if (staff.is(ModItems.NAMELESS_STAFF.get())) {
            soulBolt = new NecroBolt(
                    caster.getX() + vector3d.x / 2,
                    caster.getEyeY() - 0.2,
                    caster.getZ() + vector3d.z / 2,
                    vector3d.x,
                    vector3d.y,
                    vector3d.z, worldIn);
            SoundUtil.playNecroBolt(caster);
        } else {
            SoundUtil.playSoulBolt(caster);
        }
        if (soulBolt instanceof SoulBolt soulBolt1){
            soulBolt1.setNecro(this.typeStaff(staff, SpellType.NECROMANCY));
        }
        soulBolt.setExtraDamage(potency);
        soulBolt.setBoltSpeed((int) velocity);
        soulBolt.setOwner(caster);
        worldIn.addFreshEntity(soulBolt);
    }
}