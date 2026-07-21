package za.co.infernos.goety.init;

import za.co.infernos.goety.Goety;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import za.co.infernos.goety.compat.fml.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers every painting referenced by {@code goety:placeable} (and other painting tags).
 * Missing registrations caused TagLoader errors for goety:apostle, goety:movie, etc.
 */
public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(Registries.PAINTING_VARIANT, Goety.MOD_ID);

    // 16x32
    public static final DeferredHolder<PaintingVariant, PaintingVariant> APOSTLE = register("apostle", 16, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MOVIE = register("movie", 16, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> ELDRITCH = register("eldritch", 16, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> SATURN = register("saturn", 16, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> WICKER = register("wicker", 16, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> BEGGING = register("begging", 16, 32);

    // 32x16
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CRYPT = register("crypt", 32, 16);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> STONEDRAKE = register("stonedrake", 32, 16);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> LIZARDCALM = register("lizardcalm", 32, 16);

    // 48x32
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MINISTER = register("minister", 48, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> FENG = register("feng", 48, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> FALLEN_KINGDOM = register("fallen_kingdom", 48, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> LADIES_OF_THE_WOOD = register("ladies_of_the_wood", 48, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CAPSTONE = register("capstone", 48, 32);

    // 32x32
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MRAEG_JOEY = register("mraeg_joey", 32, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> REVELATION = register("revelation", 32, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> RUBY = register("ruby", 32, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> THEPANTS = register("thepants", 32, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MANSION = register("mansion", 32, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> HOUND = register("hound", 32, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> KOGANUSAN = register("koganusan", 32, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> GLACIAL_FLOWER = register("glacial_flower", 32, 32);

    // 16x16
    public static final DeferredHolder<PaintingVariant, PaintingVariant> WRAITH = register("wraith", 16, 16);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> BOBBY = register("bobby", 16, 16);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> HEART = register("heart", 16, 16);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> TORMENT = register("torment", 16, 16);

    // Extra originals not all listed in placeable, but present as textures
    public static final DeferredHolder<PaintingVariant, PaintingVariant> KNUCKLES = register("knuckles", 16, 32);
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MRAEG_WALLY = register("mraeg_wally", 32, 32);

    private static DeferredHolder<PaintingVariant, PaintingVariant> register(String name, int width, int height) {
        return PAINTING_VARIANTS.register(name, () -> new PaintingVariant(width, height, Goety.location(name)));
    }

    public static void init() {
        PAINTING_VARIANTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
