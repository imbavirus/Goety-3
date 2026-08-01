package za.co.infernos.goety.common.magic.spells.utility;

import za.co.infernos.goety.common.magic.Spell;
import net.minecraft.sounds.SoundEvent;

public class CommandSpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return 0;
    }

    @Override
    public int defaultCastDuration() {
        return 0;
    }

    @Override
    public SoundEvent CastingSound() {
        return null;
    }

    @Override
    public int defaultSpellCooldown() {
        return 0;
    }
}