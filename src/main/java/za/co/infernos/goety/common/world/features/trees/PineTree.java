package za.co.infernos.goety.common.world.features.trees;

import za.co.infernos.goety.common.world.features.ConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public final class PineTree {
    public static final TreeGrower GROWER = new TreeGrower(
            "pine",
            Optional.of(ConfiguredFeatures.PINE_TREE),
            Optional.empty(),
            Optional.empty()
    );

    private PineTree() {}
}