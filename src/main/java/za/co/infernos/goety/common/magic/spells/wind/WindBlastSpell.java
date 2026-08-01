package za.co.infernos.goety.common.magic.spells.wind;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.config.SpellConfig;

public class WindBlastSpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WindBlastCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WindBlastDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WindBlastCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WIND;
    }
}
