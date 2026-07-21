package za.co.infernos.goety.init;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.compat.fml.FMLJavaModLoadingContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers banner patterns referenced by {@code goety:pattern_item/*} tags.
 */
public class ModBanners {
    public static final DeferredRegister<BannerPattern> BANNER_PATTERNS =
            DeferredRegister.create(Registries.BANNER_PATTERN, Goety.MOD_ID);

    public static final DeferredHolder<BannerPattern, BannerPattern> CROSS = create("cross");
    public static final DeferredHolder<BannerPattern, BannerPattern> GALE = create("gale");
    public static final DeferredHolder<BannerPattern, BannerPattern> MOON = create("moon");

    private static DeferredHolder<BannerPattern, BannerPattern> create(String name) {
        return BANNER_PATTERNS.register(name,
                () -> new BannerPattern(Goety.location(name), "block.goety.banner." + name));
    }

    public static void init() {
        BANNER_PATTERNS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
