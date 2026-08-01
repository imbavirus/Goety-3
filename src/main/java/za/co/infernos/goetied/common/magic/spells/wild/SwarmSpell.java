package za.co.infernos.goetied.common.magic.spells.wild;

import za.co.infernos.goetied.api.magic.SpellType;
import za.co.infernos.goetied.common.magic.BreathingSpell;
import za.co.infernos.goetied.config.SpellConfig;

public class SwarmSpell extends BreathingSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.SwarmCost, 0);
    }

    @Override
    public int defaultCastUp() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.SwarmChargeUp, 0);
    }

    @Override
    public int shotsNumber() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.SwarmDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.SwarmCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WILD;
    }
}
