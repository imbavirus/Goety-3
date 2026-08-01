package za.co.infernos.goety.common.blocks;

import za.co.infernos.goety.Goety;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ModBlockSetType {
    public static final BlockSetType HAUNTED =
            BlockSetType.register(new BlockSetType(Goety.location("haunted").toString()));
    public static final BlockSetType ROTTEN =
            BlockSetType.register(new BlockSetType(Goety.location("rotten").toString()));
    public static final BlockSetType WINDSWEPT =
            BlockSetType.register(new BlockSetType(Goety.location("windswept").toString()));
    public static final BlockSetType PINE =
            BlockSetType.register(new BlockSetType(Goety.location("pine").toString()));
    public static final BlockSetType CHORUS =
            BlockSetType.register(new BlockSetType(Goety.location("chorus").toString()));
    public static final BlockSetType CORRUPT_CHORUS =
            BlockSetType.register(new BlockSetType(Goety.location("corrupt_chorus").toString()));
    public static final BlockSetType MOD_METAL =
            BlockSetType.register(new BlockSetType(Goety.location("metal").toString()));
}
