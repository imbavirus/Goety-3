package za.co.infernos.goety.common.magic.spells.void_spells;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.config.SpellConfig;

public class BlinkSpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BlinkCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BlinkDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BlinkCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }
}
