package za.co.infernos.goety.common.magic.spells.storm;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.ElectroOrb;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ElectroOrbSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRange(32);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ElectroOrbCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ElectroOrbDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ElectroOrbCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.STORM;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        int potency = spellStat.getPotency();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getPotencyLevel(caster);
            range += WandUtil.getRangeLevel(caster);
        }
        LivingEntity livingEntity = this.getTarget(caster, range);
        ElectroOrb blast = new ElectroOrb(worldIn, caster, livingEntity);
        Vec3 vector3d;
        if (livingEntity != null){
            vector3d = livingEntity.position().add(livingEntity.getDeltaMovement().multiply(10.0F, 10.0F, 10.0F)).subtract(livingEntity.position()).normalize();
            blast.setPos(blast.getX() + vector3d.x,
                    caster.getEyeY() - 0.2,
                    blast.getZ() + vector3d.z);
        } else {
            vector3d = caster.getViewVector( 1.0F);
            blast.setPos(caster.getX() + vector3d.x / 2,
                    caster.getEyeY() - 0.2,
                    caster.getZ() + vector3d.z / 2);
        }
        blast.setExtraDamage(potency);
        blast.setStaff(this.rightStaff(staff));
        blast.shoot(vector3d.x,
                vector3d.y,
                vector3d.z, 0.66F, 3.0F);
        worldIn.addFreshEntity(blast);
        this.playSound(worldIn, caster, ModSounds.SHOCK_CAST.get());
    }
}