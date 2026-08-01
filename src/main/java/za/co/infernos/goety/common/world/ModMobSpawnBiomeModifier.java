package za.co.infernos.goety.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

/**
 * Based of codes by @AlexModGuy
 */
public class ModMobSpawnBiomeModifier implements BiomeModifier {
    public ModMobSpawnBiomeModifier() {
    }

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            String biomeName = biome.unwrapKey().map(k -> k.location().toString()).orElse("unknown");
            za.co.infernos.goety.Goety.LOGGER.info("ModMobSpawnBiomeModifier: Modifying biome {}", biomeName);
            ModLevelRegistry.addBiomeSpawns(biome, builder);
        }
    }

    public MapCodec<? extends BiomeModifier> codec() {
        return makeCodec();
    }

    public static MapCodec<ModMobSpawnBiomeModifier> makeCodec() {
        return MapCodec.unit(new ModMobSpawnBiomeModifier());
    }
}
