package za.co.infernos.goety.compat.fml;

import net.neoforged.bus.api.IEventBus;

/**
 * Minimal compatibility bridge for legacy static init paths.
 * Backed by constructor-injected mod event bus from the main mod class.
 */
public final class FMLJavaModLoadingContext {
    private static final FMLJavaModLoadingContext INSTANCE = new FMLJavaModLoadingContext();
    private static IEventBus modEventBus;

    private FMLJavaModLoadingContext() {
    }

    public static FMLJavaModLoadingContext get() {
        return INSTANCE;
    }

    public static void setModEventBus(IEventBus eventBus) {
        modEventBus = eventBus;
    }

    public IEventBus getModEventBus() {
        return modEventBus;
    }
}
