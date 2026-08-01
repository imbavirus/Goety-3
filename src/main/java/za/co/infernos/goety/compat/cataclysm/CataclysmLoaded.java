package za.co.infernos.goety.compat.cataclysm;

import net.neoforged.fml.ModList;

public enum CataclysmLoaded {
    CATACLYSM("cataclysm");
    private final boolean loaded;

    CataclysmLoaded(String modid) {
        this.loaded = ModList.get() != null && ModList.get().getModContainerById(modid).isPresent();
    }

    public boolean isLoaded() {
        return this.loaded;
    }
}