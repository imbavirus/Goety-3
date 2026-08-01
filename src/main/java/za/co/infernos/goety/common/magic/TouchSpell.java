package za.co.infernos.goety.common.magic;

import za.co.infernos.goety.api.magic.ITouchSpell;

public abstract class TouchSpell extends Spell implements ITouchSpell {

    public int defaultCastDuration() {
        return 0;
    }

}
