package za.co.infernos.goety.common.world.features.trees.trunkplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

import static za.co.infernos.goety.common.world.features.trees.trunkplacers.ModTrunkPlacerTypes.CHORUS_TRUNK_PLACER;

public class ChorusTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<ChorusTrunkPlacer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> trunkPlacerParts(instance).apply(instance, ChorusTrunkPlacer::new)
    );
    public static final Codec<ChorusTrunkPlacer> CODEC = MAP_CODEC.codec();

    public ChorusTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    // Compatibility constructor for existing data-driven wiring.
    public ChorusTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider branchCount, IntProvider branchHorizontalLength, IntProvider branchStartOffsetFromTop, IntProvider branchEndOffsetFromTop) {
        this(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return CHORUS_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> placer, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        for (int i = 0; i < freeTreeHeight; ++i) {
            this.placeLog(level, placer, random, pos.above(i), config);
        }
        return List.of(new FoliagePlacer.FoliageAttachment(pos.above(freeTreeHeight), 0, false));
    }
}
