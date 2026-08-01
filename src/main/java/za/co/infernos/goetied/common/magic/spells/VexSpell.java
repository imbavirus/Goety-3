package za.co.infernos.goetied.common.magic.spells;

import za.co.infernos.goetied.api.magic.SpellType;
import za.co.infernos.goetied.common.magic.Spell;
import za.co.infernos.goetied.config.SpellConfig;

public class VexSpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.VexCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.VexDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.VexCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.ILL;
    }
}
