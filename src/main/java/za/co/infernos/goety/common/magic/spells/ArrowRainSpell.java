package za.co.infernos.goety.common.magic.spells;

import za.co.infernos.goety.common.magic.EverChargeSpell;
import za.co.infernos.goety.config.SpellConfig;

public class ArrowRainSpell extends EverChargeSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ArrowRainCost, 0);
    }

    @Override
    public int defaultCastUp() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ArrowRainChargeUp, 0);
    }

    @Override
    public int shotsNumber() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ArrowRainDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ArrowRainCoolDown, 0);
    }
}
