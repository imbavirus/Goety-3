package za.co.infernos.goety.common.magic.spells.wild;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.BlossomBall;
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

public class BlossomSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(3.0D).setVelocity(1.5F);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BlossomCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BlossomDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.WILD_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BlossomCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WILD;
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
        float velocity = WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 3.0F;
        float extraBlast = WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.0F;
        if (this.rightStaff(staff)){
            radius += 1.5F;
        }
        BlossomBall blast = new BlossomBall(caster, worldIn);
        blast.setExtraDamage(spellStat.getPotency() + WandUtil.getPotencyLevel(caster));
        blast.setRadius(extraBlast + radius);
        blast.setDuration(spellStat.getDuration() + WandUtil.getLevels(ModEnchantments.DURATION.get(), caster));
        blast.setStaff(this.rightStaff(staff));
        blast.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0F, spellStat.getVelocity() + velocity, 1.0F);
        worldIn.addFreshEntity(blast);
        this.playSound(worldIn, caster, ModSounds.BLAST_FUNGUS_THROW.get(), 1.0F, 0.75F);
    }
}