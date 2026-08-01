package za.co.infernos.goetied.common.blocks;

import za.co.infernos.goetied.Goetied;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ModBlockSetType {
    public static final BlockSetType HAUNTED =
            BlockSetType.register(new BlockSetType(Goetied.location("haunted").toString()));
    public static final BlockSetType ROTTEN =
            BlockSetType.register(new BlockSetType(Goetied.location("rotten").toString()));
    public static final BlockSetType WINDSWEPT =
            BlockSetType.register(new BlockSetType(Goetied.location("windswept").toString()));
    public static final BlockSetType PINE =
            BlockSetType.register(new BlockSetType(Goetied.location("pine").toString()));
    public static final BlockSetType CHORUS =
            BlockSetType.register(new BlockSetType(Goetied.location("chorus").toString()));
    public static final BlockSetType CORRUPT_CHORUS =
            BlockSetType.register(new BlockSetType(Goetied.location("corrupt_chorus").toString()));
    public static final BlockSetType MOD_METAL =
            BlockSetType.register(new BlockSetType(Goetied.location("metal").toString()));
}
