package za.co.infernos.goety.common.world.features;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.world.features.configs.ModTreeFeatureConfig;
import za.co.infernos.goety.common.world.features.trees.features.ChorusTreeFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Goety.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<ModTreeFeatureConfig>> CHORUS_TREE = FEATURES.register("chorus_tree", () -> new ChorusTreeFeature(ModTreeFeatureConfig.CODEC, false));
    public static final DeferredHolder<Feature<?>, Feature<ModTreeFeatureConfig>> CHORUS_VOID_TREE = FEATURES.register("chorus_void_tree", () -> new ChorusTreeFeature(ModTreeFeatureConfig.CODEC, true));
    public static final DeferredHolder<Feature<?>, Feature<NetherForestVegetationConfig>> END_VEGETATION = FEATURES.register("end_vegetation", () -> new EndVegetationFeature(NetherForestVegetationConfig.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NetherForestVegetationConfig>> END_GROWTH = FEATURES.register("end_growth", () -> new EndGrowthFeature(NetherForestVegetationConfig.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NetherForestVegetationConfig>> END_CHORUS = FEATURES.register("end_chorus", () -> new EndChorusFeature(NetherForestVegetationConfig.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<RandomPatchConfiguration>> SINGLE_PATCH = FEATURES.register("single_patch", () -> new SinglePatchFeature(RandomPatchConfiguration.CODEC));
}