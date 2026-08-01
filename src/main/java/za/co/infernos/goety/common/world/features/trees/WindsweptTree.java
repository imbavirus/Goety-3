package za.co.infernos.goety.common.world.features.trees;

import za.co.infernos.goety.common.world.features.ConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public final class WindsweptTree {
    public static final TreeGrower GROWER = new TreeGrower(
            "windswept",
            Optional.of(ConfiguredFeatures.WINDSWEPT_TREE),
            Optional.of(ConfiguredFeatures.WINDSWEPT_TREE_2),
            Optional.empty()
    );

    private WindsweptTree() {}
}