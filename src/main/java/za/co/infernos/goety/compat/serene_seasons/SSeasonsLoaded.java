package za.co.infernos.goety.compat.serene_seasons;

import net.neoforged.fml.ModList;

public enum SSeasonsLoaded {
    SERENE_SEASONS("sereneseasons");
    private final boolean loaded;

    SSeasonsLoaded(String modid) {
        this.loaded = ModList.get() != null && ModList.get().getModContainerById(modid).isPresent();
    }

    public boolean isLoaded() {
        return this.loaded;
    }
}