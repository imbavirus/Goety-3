package za.co.infernos.goety.common.magic;

import za.co.infernos.goety.api.magic.IChargingSpell;

public abstract class ChargingSpell extends Spell implements IChargingSpell {

    public abstract int Cooldown();

    public int defaultCastDuration() {
        return 72000;
    }

    @Override
    public int defaultSpellCooldown() {
        return 0;
    }
}