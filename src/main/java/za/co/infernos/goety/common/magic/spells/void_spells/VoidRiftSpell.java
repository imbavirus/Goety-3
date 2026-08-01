package za.co.infernos.goety.common.magic.spells.void_spells;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.util.VoidRift;
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

public class VoidRiftSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setDuration(300);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.RuptureCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.RuptureDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.VOID_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.RuptureCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public void startSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int warmUp = this.castDuration(caster, staff) - 10;
        int duration = spellStat.getDuration() * (WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1);
        int range = spellStat.getRange() + WandUtil.getRangeLevel(caster);
        Vec3 vec3 = this.rayTrace(worldIn, caster, range, 3).getLocation();
        VoidRift voidRift = new VoidRift(worldIn, vec3.x, vec3.y, vec3.z);
        voidRift.setOwner(caster);
        voidRift.setDuration(duration);
        voidRift.setWarmUp(warmUp);
        voidRift.setStaff(rightStaff(staff));
        voidRift.setSize((float) (spellStat.getRadius() + WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster)));
        voidRift.setExtraDamage(spellStat.getPotency() + WandUtil.getPotencyLevel(caster));
        worldIn.addFreshEntity(voidRift);
    }
}