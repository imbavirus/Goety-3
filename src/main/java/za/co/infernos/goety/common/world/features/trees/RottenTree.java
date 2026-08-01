package za.co.infernos.goety.common.world.features.trees;

import za.co.infernos.goety.common.world.features.ConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public final class RottenTree {
    public static final TreeGrower GROWER = new TreeGrower(
            "rotten",
            Optional.of(ConfiguredFeatures.SAPLING_ROTTEN_TREE),
            Optional.of(ConfiguredFeatures.SAPLING_FANCY_ROTTEN_TREE),
            Optional.empty()
    );

    private RottenTree() {}
}