package za.co.infernos.goetied.data;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.world.features.ConfiguredFeatures;
import za.co.infernos.goetied.common.world.features.PlacedFeatures;
import za.co.infernos.goetied.init.ModTrimMaterials;
import za.co.infernos.goetied.utils.ModDamageSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Based on @TeamTwilight's RegisteryDataGenerator: <a href="https://github.com/TeamTwilight/twilightforest/blob/1.20.x/src/main/java/twilightforest/data/RegistryDataGenerator.java">...</a>
 */
public class ModRegisteryDataProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatures::bootstrap)
            .add(Registries.DAMAGE_TYPE, ModDamageSource::bootstrap)
            .add(Registries.TRIM_MATERIAL, ModTrimMaterials::bootstrap);

    public ModRegisteryDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", Goetied.MOD_ID));
    }
}
