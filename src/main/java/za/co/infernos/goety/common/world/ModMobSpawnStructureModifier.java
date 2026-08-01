package za.co.infernos.goety.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.world.ModifiableStructureInfo;
import net.neoforged.neoforge.common.world.StructureModifier;

/**
 * Based of codes by @AlexModGuy
 */
public class ModMobSpawnStructureModifier implements StructureModifier {
    public ModMobSpawnStructureModifier() {
    }

    public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder) {
        if (phase == Phase.ADD) {
            ModLevelRegistry.addStructureSpawns(structure, builder);
        }
    }

    public MapCodec<? extends StructureModifier > codec() {
        return makeCodec();
    }

    public static MapCodec<ModMobSpawnStructureModifier> makeCodec() {
        return MapCodec.unit(new ModMobSpawnStructureModifier());
    }
}
