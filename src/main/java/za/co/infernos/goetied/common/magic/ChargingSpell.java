package za.co.infernos.goetied.common.magic;

import za.co.infernos.goetied.api.magic.IChargingSpell;

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