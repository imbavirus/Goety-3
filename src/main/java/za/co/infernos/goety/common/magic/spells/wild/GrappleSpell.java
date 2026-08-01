package za.co.infernos.goety.common.magic.spells.wild;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.VineHook;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.SEHelper;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GrappleSpell extends Spell {
    // Lazy evaluation to avoid accessing config before it's loaded
    private int trueCooldownCache = -1;
    
    public int getTrueCooldown() {
        if (trueCooldownCache < 0) {
            trueCooldownCache = this.defaultSpellCooldown();
        }
        return trueCooldownCache;
    }
    
    public void setTrueCooldown(int cooldown) {
        this.trueCooldownCache = cooldown;
    }
    
    @Deprecated // Use getTrueCooldown() instead
    public int trueCooldown = 0; // Will be calculated lazily

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.GrappleCost, 0);
    }

    @Override
    public int soulCost(LivingEntity caster, ItemStack staff) {
        if (caster instanceof Player player){
            Projectile projectile = SEHelper.getGrappling(player);
            if (projectile != null) {
                return 0;
            }
        }
        return super.soulCost(caster, staff);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.GrappleDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.WILD_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.GrappleCoolDown, 0);
    }

    public int spellCooldown(LivingEntity caster){
        return getTrueCooldown();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WILD;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.VELOCITY.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        float velocity = spellStat.getVelocity();
        if (WandUtil.enchantedFocus(caster)) {
            velocity = WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 2.0F;
        }
        if (caster instanceof Player player){
            Projectile projectile = SEHelper.getGrappling(player);
            if (projectile != null) {
                projectile.discard();
                SEHelper.setGrappling(player, null);
                this.playSound(worldIn, player, SoundEvents.FISHING_BOBBER_RETRIEVE, 1.0F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));
                worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, this.getSoundSource(), 1.0F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));
                setTrueCooldown(this.defaultSpellCooldown());
            } else {
                VineHook vineHook = new VineHook(worldIn, player, 2.5F + velocity);
                vineHook.setStaff(rightStaff(staff));
                worldIn.addFreshEntity(vineHook);
                this.playSound(worldIn, player, SoundEvents.FISHING_BOBBER_THROW, 0.5F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));
                setTrueCooldown(0);
            }
        }
    }
}