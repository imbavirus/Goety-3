package za.co.infernos.goety.common.world.placements;

import za.co.infernos.goety.Goety;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModPlacementType {
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPE = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, Goety.MOD_ID);
    
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<ModRandomSpread>> RANDOM_SPREAD = 
            STRUCTURE_PLACEMENT_TYPE.register("random_spread", () -> new StructurePlacementType<ModRandomSpread>() {
                @Override
                public MapCodec<ModRandomSpread> codec() {
                    return ModRandomSpread.MAP_CODEC;
                }
            });
    
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<ModMinorRandomSpread>> MINOR_RANDOM_SPREAD = 
            STRUCTURE_PLACEMENT_TYPE.register("minor_random_spread", () -> new StructurePlacementType<ModMinorRandomSpread>() {
                @Override
                public MapCodec<ModMinorRandomSpread> codec() {
                    return ModMinorRandomSpread.MAP_CODEC;
                }
            });
}
