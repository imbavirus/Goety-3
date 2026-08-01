package za.co.infernos.goetied.common.magic.spells.void_spells;

import za.co.infernos.goetied.api.magic.SpellType;
import za.co.infernos.goetied.common.magic.Spell;
import za.co.infernos.goetied.config.SpellConfig;

public class BlinkSpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.BlinkCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.BlinkDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.BlinkCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }
}
