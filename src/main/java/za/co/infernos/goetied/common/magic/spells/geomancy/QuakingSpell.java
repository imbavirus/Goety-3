package za.co.infernos.goetied.common.magic.spells.geomancy;

import za.co.infernos.goetied.api.magic.SpellType;
import za.co.infernos.goetied.common.magic.Spell;
import za.co.infernos.goetied.config.SpellConfig;

public class QuakingSpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.QuakingCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.QuakingDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.QuakingCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.GEOMANCY;
    }
}
