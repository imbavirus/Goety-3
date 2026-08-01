package za.co.infernos.goetied.common.world.structures;

import za.co.infernos.goetied.Goetied;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModStructures {
    public static final ResourceKey<Structure> GRAVEYARD_KEY = ResourceKey.create(Registries.STRUCTURE, Goetied.location("graveyard"));
    public static final ResourceKey<Structure> CRYPT_KEY = ResourceKey.create(Registries.STRUCTURE, Goetied.location("crypt"));
    public static final ResourceKey<Structure> BLIGHTED_SHACK_KEY = ResourceKey.create(Registries.STRUCTURE, Goetied.location("blighted_shack"));
}
