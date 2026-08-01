package za.co.infernos.goety.data;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.world.structures.ModStructures;
import za.co.infernos.goety.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class ModStructureTagsProvider extends TagsProvider<Structure> {

    public ModStructureTagsProvider(PackOutput p_256522_, CompletableFuture<HolderLookup.Provider> p_256661_, @org.jetbrains.annotations.Nullable net.neoforged.common.data.ExistingFileHelper existingFileHelper) {
        super(p_256522_, Registries.STRUCTURE, p_256661_, Goety.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_255894_) {
        this.tag(ModTags.Structures.WITHER_NECROMANCER_SPAWNS)
                .add(BuiltinStructures.FORTRESS)
                .addOptional(ResourceLocation.fromNamespaceAndPath("betterfortresses", "better_fortresses"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("morevillagers", "on_fortress_explorer_maps"));
        this.tag(ModTags.Structures.VIZIER_SPAWNS)
                .addTag(StructureTags.ON_WOODLAND_EXPLORER_MAPS)
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("repurposed_structures", "collections/mansions"));
        this.tag(ModTags.Structures.CRONE_SPAWNS).add(ModStructures.BLIGHTED_SHACK_KEY);
        this.tag(ModTags.Structures.SKULL_LORD_SPAWNS).add(ModStructures.CRYPT_KEY);
        this.tag(ModTags.Structures.CRYPT).add(ModStructures.CRYPT_KEY);
        this.tag(ModTags.Structures.NECROMANCER_POWER).addTag(ModTags.Structures.CRYPT).add(ModStructures.GRAVEYARD_KEY);
        this.tag(ModTags.Structures.CAN_SUMMON_BRUTES).add(BuiltinStructures.BASTION_REMNANT);
        this.tag(ModTags.Structures.CAN_SUMMON_WITHER_SKELETONS)
                .add(BuiltinStructures.FORTRESS)
                .addOptional(ResourceLocation.fromNamespaceAndPath("betterfortresses", "better_fortresses"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("morevillagers", "on_fortress_explorer_maps"));
    }
}