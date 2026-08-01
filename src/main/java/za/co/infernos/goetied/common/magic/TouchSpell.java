package za.co.infernos.goetied.common.magic;

import za.co.infernos.goetied.api.magic.ITouchSpell;

public abstract class TouchSpell extends Spell implements ITouchSpell {

    public int defaultCastDuration() {
        return 0;
    }

}
