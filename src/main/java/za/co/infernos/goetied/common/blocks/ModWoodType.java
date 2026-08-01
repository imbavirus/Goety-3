package za.co.infernos.goetied.common.blocks;

import za.co.infernos.goetied.Goetied;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodType {
    public static final WoodType HAUNTED =
            WoodType.register(new WoodType(Goetied.location("haunted").toString(), ModBlockSetType.HAUNTED));
    public static final WoodType ROTTEN =
            WoodType.register(new WoodType(Goetied.location("rotten").toString(), ModBlockSetType.ROTTEN));
    public static final WoodType WINDSWEPT =
            WoodType.register(new WoodType(Goetied.location("windswept").toString(), ModBlockSetType.WINDSWEPT));
    public static final WoodType PINE =
            WoodType.register(new WoodType(Goetied.location("pine").toString(), ModBlockSetType.PINE));
    public static final WoodType CHORUS =
            WoodType.register(new WoodType(Goetied.location("chorus").toString(), ModBlockSetType.CHORUS));
    public static final WoodType CORRUPT_CHORUS =
            WoodType.register(new WoodType(Goetied.location("corrupt_chorus").toString(), ModBlockSetType.CORRUPT_CHORUS));
}
