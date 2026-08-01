package za.co.infernos.goetied.init;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.compat.fml.FMLJavaModLoadingContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers banner patterns referenced by {@code goetied:pattern_item/*} tags.
 */
public class ModBanners {
    public static final DeferredRegister<BannerPattern> BANNER_PATTERNS =
            DeferredRegister.create(Registries.BANNER_PATTERN, Goetied.MOD_ID);

    public static final DeferredHolder<BannerPattern, BannerPattern> CROSS = create("cross");
    public static final DeferredHolder<BannerPattern, BannerPattern> GALE = create("gale");
    public static final DeferredHolder<BannerPattern, BannerPattern> MOON = create("moon");

    private static DeferredHolder<BannerPattern, BannerPattern> create(String name) {
        return BANNER_PATTERNS.register(name,
                () -> new BannerPattern(Goetied.location(name), "block.goetied.banner." + name));
    }

    public static void init() {
        BANNER_PATTERNS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
