package za.co.infernos.goetied.common.magic.spells.frost;

import za.co.infernos.goetied.api.magic.SpellType;
import za.co.infernos.goetied.common.magic.BreathingSpell;
import za.co.infernos.goetied.config.SpellConfig;

public class FrostBreathSpell extends BreathingSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.FrostBreathCost, 0);
    }

    @Override
    public int defaultCastUp() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.FrostBreathChargeUp, 0);
    }

    @Override
    public int shotsNumber() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.FrostBreathDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.FrostBreathCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.FROST;
    }
}
