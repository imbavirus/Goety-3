package za.co.infernos.goety.common.magic.spells.wild;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.PoisonQuill;
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

import java.util.ArrayList;
import java.util.List;

public class PoisonDartSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setVelocity(1.6F);
    }

    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PoisonDartCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PoisonDartDuration, 0);
    }

    public SoundEvent CastingSound() {
        return ModSounds.WILD_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PoisonDartCoolDown, 0);
    }

    public SpellType getSpellType() {
        return SpellType.WILD;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        list.add(ModEnchantments.VELOCITY.get());
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        float velocity = spellStat.getVelocity();
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)) {
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 3.0F;
            potency += WandUtil.getPotencyLevel(caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }
        PoisonQuill poisonQuill = new PoisonQuill(worldIn, caster);
        poisonQuill.setSpear(rightStaff(staff), potency + 1);
        poisonQuill.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0F, velocity, 1.0F);
        poisonQuill.setOwner(caster);
        poisonQuill.setExtraDamage(potency);
        poisonQuill.setDuration(duration);
        if (caster.isUnderWater()){
            poisonQuill.setAqua(true);
        }
        worldIn.addFreshEntity(poisonQuill);
        this.playSound(worldIn, caster, ModSounds.POISON_QUILL_VINE_SHOOT.get(), 1.0F, this.projPitch(worldIn.getRandom()));
    }
}