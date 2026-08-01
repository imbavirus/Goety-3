package za.co.infernos.goety.common.magic.spells.abyss;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.BouncyBubble;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BouncyBubbleSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BouncyBubbleCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BouncyBubbleDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.GENERIC_SPLASH;
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BouncyBubbleCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.ABYSS;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.VELOCITY.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int velocity = (int) spellStat.getVelocity();
        int radius = (int) spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
        }
        Vec3 vector3d = caster.getLookAngle();
        BouncyBubble bouncyBubble = new BouncyBubble(
                caster.getX() + vector3d.x / 2,
                caster.getEyeY() - 0.2,
                caster.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z, worldIn);
        bouncyBubble.shoot(vector3d);
        bouncyBubble.setOwner(caster);
        bouncyBubble.setExtraDamage(potency);
        bouncyBubble.setBoltSpeed(velocity);
        bouncyBubble.setSize(radius);
        worldIn.addFreshEntity(bouncyBubble);
        if (rightStaff(staff)) {
            for (int i = 0; i < 2; ++i) {
                BouncyBubble bouncyBubble2 = new BouncyBubble(
                        caster.getX() + vector3d.x / 2 + worldIn.random.nextGaussian(),
                        caster.getEyeY() - 0.2,
                        caster.getZ() + vector3d.z / 2 + worldIn.random.nextGaussian(),
                        vector3d.x,
                        vector3d.y,
                        vector3d.z, worldIn);
                bouncyBubble2.shoot(vector3d);
                bouncyBubble2.setOwner(caster);
                bouncyBubble2.setExtraDamage(potency);
                bouncyBubble2.setBoltSpeed(velocity);
                bouncyBubble2.setSize(radius);
                worldIn.addFreshEntity(bouncyBubble2);
            }
        }
    }
}