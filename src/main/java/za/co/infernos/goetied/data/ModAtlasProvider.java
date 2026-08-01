package za.co.infernos.goetied.data;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.block.ModChestRenderer;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.client.resources.model.Material;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.Optional;

/**
 * Based on @TeamTwilight's AtlasGenerator
 */
public class ModAtlasProvider extends SpriteSourceProvider {
    public ModAtlasProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, helper, Goetied.MOD_ID);
    }

    @Override
    protected void addSources() {
        ModChestRenderer.MATERIALS.values().stream().flatMap(e -> e.values().stream()).map(Material::texture)
                .forEach(resourceLocation -> this.atlas(CHESTS_ATLAS).addSource(new SingleFile(resourceLocation, Optional.empty())));
    }
}