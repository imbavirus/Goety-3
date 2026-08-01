package za.co.infernos.goetied.common.magic;

import za.co.infernos.goetied.api.magic.IBlockSpell;

public abstract class BlockSpell extends Spell implements IBlockSpell {

    public int defaultCastDuration() {
        return 0;
    }
}