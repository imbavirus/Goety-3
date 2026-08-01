package za.co.infernos.goety.compat.minecolonies;

import net.neoforged.fml.ModList;

public enum MinecoloniesLoaded {
    MINECOLONIES("minecolonies");
    private final boolean loaded;

    MinecoloniesLoaded(String modid) {
        this.loaded = ModList.get() != null && ModList.get().getModContainerById(modid).isPresent();
    }

    public boolean isLoaded() {
        return this.loaded;
    }
}