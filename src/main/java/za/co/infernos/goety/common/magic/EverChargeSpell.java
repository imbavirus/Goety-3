package za.co.infernos.goety.common.magic;

public abstract class EverChargeSpell extends ChargingSpell {
    @Override
    public int Cooldown() {
        return 0;
    }

    @Override
    public boolean everCharge() {
        return true;
    }
}