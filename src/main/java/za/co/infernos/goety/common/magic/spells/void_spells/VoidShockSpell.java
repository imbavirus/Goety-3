package za.co.infernos.goety.common.magic.spells.void_spells;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.VoidShock;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.SEHelper;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VoidShockSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.VoidShockCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.VoidShockDuration, 0);
    }

    @Override
    public int castDuration(LivingEntity caster, ItemStack staff) {
        int i = WandUtil.getStats(caster, this).getDuration();
        if (WandUtil.enchantedFocus(caster)) {
            i += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.VoidShockDuration, 0) * (i + 1);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.VOID_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.VoidShockCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
//        list.add(ModEnchantments.DURATION.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public void stopSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, ItemStack focus, int castTime, SpellStat spellStat) {
        if (castTime >= 10){
            if (caster instanceof Player player && !focus.isEmpty()) {
                SEHelper.addCooldown(player, focus.getItem(), this.spellCooldown(caster));
                SEHelper.sendSEUpdatePacket(player);
            }
        }
    }

    @Override
    public void useSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, int castTime, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        double radius = spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getPotencyLevel(caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.0F;
        }
        int tickRate = this.rightStaff(staff) ? 2 : 5;
        if (castTime >= 10 && castTime % tickRate == 0) {
            VoidShock voidShock = new VoidShock(caster, this.getTarget(caster), worldIn);
            voidShock.setPos(caster.position().add(worldIn.getRandom().nextInt(-3, 3), caster.getBbHeight() + 1.0F, worldIn.getRandom().nextInt(-3, 3)));
            voidShock.setPower(Vec3.ZERO, 10);
            voidShock.setExtraDamage(potency);
            voidShock.setExtraRadius((float) radius);
            worldIn.addFreshEntity(voidShock);
            this.playSound(worldIn, voidShock, ModSounds.TELEPORT_ORB_THROW.get(), 2.0F, caster.getVoicePitch());
        }
        super.useSpell(worldIn, caster, staff, castTime, spellStat);
    }
}