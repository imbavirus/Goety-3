package za.co.infernos.goetied.compat.patchouli;

import net.neoforged.fml.ModList;

public enum PatchouliLoaded {
    PATCHOULI("patchouli");
    private final boolean loaded;

    PatchouliLoaded(String modid) {
        this.loaded = ModList.get() != null && ModList.get().getModContainerById(modid).isPresent();
    }

    public boolean isLoaded() {
        return this.loaded;
    }
}