package za.co.infernos.goety.common.magic.spells.storm;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SThunderBoltPacket;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ThunderboltSpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ThunderboltCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ThunderboltDuration, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.ZAP.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ThunderboltCoolDown, 0);
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
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int range = spellStat.getRange();
        float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.ThunderboltDamage, 1.0F) * WandUtil.damageMultiply();
        if (WandUtil.enchantedFocus(caster)) {
            range += WandUtil.getRangeLevel(caster);
            damage += WandUtil.getPotencyLevel(caster);
        }
        ColorUtil colorUtil = new ColorUtil(0xb1abf1);
        if (staff.is(ModItems.NAMELESS_STAFF.get())) {
            colorUtil = new ColorUtil(0xa7fc3e);
        }
        damage += spellStat.getPotency();
        Vec3 vec3 = caster.getEyePosition();
        BlockHitResult rayTraceResult = this.blockResult(worldIn, caster, range);
        Entity target = MobUtil.getNearbyTarget(worldIn, caster, range, 1.0F);
        Optional<BlockPos> lightningRod = BlockFinder.findLightningRod(worldIn, BlockPos.containing(rayTraceResult.getLocation()), range);
        if (lightningRod.isPresent() && !rightStaff(staff)){
            BlockPos blockPos = lightningRod.get();
            ModNetwork.sendToALL(new SThunderBoltPacket(vec3, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()), colorUtil, 10));
            this.playSound(worldIn, caster, ModSounds.THUNDERBOLT.get());
        } else {
            LivingEntity livingEntity = MobUtil.getLivingTarget(target);
            if (livingEntity != null) {
                Vec3 vec31 = new Vec3(livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() / 2, livingEntity.getZ());
                ModNetwork.sendToALL(new SThunderBoltPacket(vec3, vec31, colorUtil, 10));
                if (livingEntity.hurt(ModDamageSource.directShock(caster), damage)){
                    float chance = rightStaff(staff) ? 0.25F : 0.05F;
                    float chainDamage = damage / 2.0F;
                    if (worldIn.isThundering() && worldIn.isRainingAt(livingEntity.blockPosition())){
                        chance += 0.25F;
                        chainDamage = damage;
                    }
                    if (worldIn.random.nextFloat() <= chance){
                        livingEntity.addEffect(new MobEffectInstance(GoetyEffects.SPASMS, MathHelper.secondsToTicks(5)));
                    }
                    if (rightStaff(staff)){
                        WandUtil.chainLightning(livingEntity, caster, range / 4.0D, chainDamage);
                    }
                }
                this.playSound(worldIn, caster, ModSounds.THUNDERBOLT.get());
            } else {
                BlockPos blockPos = rayTraceResult.getBlockPos();
                ModNetwork.sendToALL(new SThunderBoltPacket(vec3, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()), colorUtil, 10));
                this.playSound(worldIn, caster, ModSounds.THUNDERBOLT.get());
            }
        }
    }
}
