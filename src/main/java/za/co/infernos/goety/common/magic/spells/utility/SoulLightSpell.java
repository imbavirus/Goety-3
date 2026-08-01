package za.co.infernos.goety.common.magic.spells.utility;

import za.co.infernos.goety.common.entities.projectiles.SoulLight;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SoulLightSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulLightCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulLightDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulLightCoolDown, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.EGG_THROW;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        SoulLight soulLightEntity = new SoulLight(worldIn, caster);
        soulLightEntity.setOwner(caster);
        soulLightEntity.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(soulLightEntity);
        this.playSound(worldIn, caster, 1.0F, 1.0F);
    }
}