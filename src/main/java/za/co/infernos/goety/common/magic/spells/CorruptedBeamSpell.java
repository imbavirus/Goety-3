package za.co.infernos.goety.common.magic.spells;

import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.projectiles.CorruptedBeam;
import za.co.infernos.goety.common.magic.EverChargeSpell;
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

import java.util.ArrayList;
import java.util.List;

public class CorruptedBeamSpell extends EverChargeSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CorruptionCost, 0);
    }

    @Override
    public int defaultCastUp() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CorruptionChargeUp, 0);
    }

    @Override
    public int shotsNumber() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CorruptionDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CorruptionCoolDown, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.CORRUPT_BEAM_START.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        float potency = spellStat.getPotency();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster) / 2.0F;
        }
        List<CorruptedBeam> entities = worldIn.getEntitiesOfClass(CorruptedBeam.class, caster.getBoundingBox().inflate(2.0F),
                corruptedBeam -> corruptedBeam.getOwner() == caster);
        Vec3 vector3d = caster.getViewVector( 1.0F);
        if (entities.isEmpty()) {
            CorruptedBeam corruptedBeam = new CorruptedBeam(ModEntityType.CORRUPTED_BEAM.get(), worldIn, caster);
            corruptedBeam.moveTo(
                    caster.getX() + vector3d.x / 2,
                    caster.getEyeY() - 0.2,
                    caster.getZ() + vector3d.z / 2, caster.getYRot(), caster.getXRot());
            corruptedBeam.setOwner(caster);
            corruptedBeam.setExtraDamage(potency);
            corruptedBeam.setItemBase(true);
            worldIn.addFreshEntity(corruptedBeam);
        }
    }
}