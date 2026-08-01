package za.co.infernos.goety.common.magic.spells.nether;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.MagmaBomb;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MagmaSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(3.0D).setVelocity(1.5F);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MagmaBombCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MagmaBombDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MagmaBombCoolDown, 0);
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
        list.add(ModEnchantments.RADIUS.get());
        list.add(ModEnchantments.VELOCITY.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        float radius = (float) spellStat.getRadius();
        float velocity = spellStat.getVelocity() + WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 3.0F;
        float extraBlast = WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.0F;
        if (this.rightStaff(staff)){
            radius += 1.0F;
        }
        MagmaBomb blast = new MagmaBomb(caster, worldIn);
        blast.setExtraDamage(spellStat.getPotency() + WandUtil.getPotencyLevel(caster));
        blast.setExplosionPower(extraBlast + radius);
        blast.setDuration(spellStat.getDuration() + WandUtil.getLevels(ModEnchantments.DURATION.get(), caster));
        blast.setStaff(this.rightStaff(staff));
        blast.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0F, velocity, 1.0F);
        worldIn.addFreshEntity(blast);

        this.playSound(worldIn, caster, ModSounds.CAST_SPELL.get());
    }
}