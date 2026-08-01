package za.co.infernos.goetied.data;

import za.co.infernos.goetied.Goetied;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Goetied.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Item item : NeoForgeRegistries.ITEMS) {
            if (NeoForgeRegistries.ITEMS.getKey(item) != null) {
                ResourceLocation resourceLocation = NeoForgeRegistries.ITEMS.getKey(item);
                if (resourceLocation != null) {
                    if (item instanceof SpawnEggItem && resourceLocation.getNamespace().equals(Goetied.MOD_ID)) {
                        getBuilder(resourceLocation.getPath())
                                .parent(getExistingFile(ResourceLocation.parse("item/template_spawn_egg")));
                    }
                }
            }
        }
    }
}
