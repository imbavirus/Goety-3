package za.co.infernos.goety.common.magic.spells.wind;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.EverChargeSpell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.ColorUtil;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.ServerParticleUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WhirlwindSpell extends EverChargeSpell {
    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(2.0D);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WhirlwindCost, 0);
    }

    @Override
    public int defaultCastUp() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WhirlwindChargeUp, 0);
    }

    @Override
    public int shotsNumber() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WhirlwindDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WhirlwindCoolDown, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.WIND.get();
    }

    @Override
    public SoundEvent loopSound(LivingEntity caster) {
        return ModSounds.WHIRLWIND.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WIND;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        Vec3 vec3 = caster.getDeltaMovement();
        double y = 0.04D;
        float radius = (float) spellStat.getRadius();
        if (rightStaff(staff)){
            radius += 1.0F;
        }
        if (WandUtil.enchantedFocus(caster)) {
            y += WandUtil.getPotencyLevel(caster) / 100.0D;
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
        }
        if (vec3.y < 0.0D) {
            y = 0.2D;
        }
        int fireTicks = caster.getRemainingFireTicks();
        if (fireTicks > 0){
            int amount = rightStaff(staff) ? 4 : 1;
            caster.setRemainingFireTicks(Math.max(fireTicks - amount, 0));
        }
        MobUtil.forcePush(caster, 0.0D, y, 0.0D);
        for (Entity entity : worldIn.getEntitiesOfClass(Entity.class, caster.getBoundingBox().inflate(radius))){
            if (entity != caster) {
                if (caster.getVehicle() == null || caster.getVehicle() != entity) {
                    boolean flag = entity instanceof LivingEntity
                            || (entity instanceof AbstractArrow arrow
                            && (arrow.getOwner() == null
                            || !arrow.getOwner().getType().is(Tags.EntityTypes.BOSSES)));
                    if (flag) {
                        Vec3 vec31 = new Vec3(caster.getX(), caster.getY(), caster.getZ());
                        Vec3 vec32 = new Vec3(entity.getX(), entity.getY(), entity.getZ());
                        double distance = vec31.distanceTo(vec32) + 0.1D;
                        Vec3 vec33 = new Vec3(vec32.x - vec31.x, vec32.y - vec31.y, vec32.z - vec31.z);
                        MobUtil.push(entity, vec33.x / radius / distance,
                                vec33.y / radius / distance,
                                vec33.z / radius / distance);
                        if (rightStaff(staff)) {
                            if (entity instanceof AbstractArrow arrow) {
                                if (!arrow.onGround()){
                                    double d0 = arrow.getX() - caster.getX();
                                    double d1 = arrow.getY() - caster.getY();
                                    double d2 = arrow.getZ() - caster.getZ();
                                    double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
                                    arrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.0F, 10);
                                }
                            }
                        }
                    }
                }
            }
        }
        ColorUtil color = new ColorUtil(0xffffff);
        ServerParticleUtil.windParticle(worldIn, color, radius - 1.0F, -0.5F, caster.getId(), caster.position());
        ServerParticleUtil.windParticle(worldIn, color, radius - 1.0F, 1.0F, caster.getId(), caster.position());
        ServerParticleUtil.windParticle(worldIn, color, radius, 0.5F, caster.getId(), caster.position());
    }
}