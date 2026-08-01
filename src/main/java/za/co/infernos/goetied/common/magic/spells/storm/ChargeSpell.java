package za.co.infernos.goetied.common.magic.spells.storm;

import za.co.infernos.goetied.api.magic.SpellType;
import za.co.infernos.goetied.common.magic.SpellStat;
import za.co.infernos.goetied.common.magic.TouchSpell;
import za.co.infernos.goetied.config.SpellConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ChargeSpell extends TouchSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.ChargeCost, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.ChargeCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.STORM;
    }

    @Override
    public void touchResult(ServerLevel worldIn, LivingEntity caster, LivingEntity target, ItemStack staff, SpellStat spellStat) {
        target.hurtMarked = true;
    }
}
