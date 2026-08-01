package za.co.infernos.goety.common.magic;

import za.co.infernos.goety.api.magic.IBlockSpell;

public abstract class BlockSpell extends Spell implements IBlockSpell {

    public int defaultCastDuration() {
        return 0;
    }
}