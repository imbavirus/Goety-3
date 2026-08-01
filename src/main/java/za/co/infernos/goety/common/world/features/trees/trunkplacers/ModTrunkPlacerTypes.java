package za.co.infernos.goety.common.world.features.trees.trunkplacers;

import za.co.infernos.goety.Goety;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModTrunkPlacerTypes {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, Goety.MOD_ID);
    
    public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<ChorusTrunkPlacer>> CHORUS_TRUNK_PLACER = 
            TRUNK_PLACER_TYPES.register("chorus_trunk_placer", () -> new TrunkPlacerType<ChorusTrunkPlacer>(ChorusTrunkPlacer.MAP_CODEC));
}
