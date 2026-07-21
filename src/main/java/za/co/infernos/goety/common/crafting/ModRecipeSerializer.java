package za.co.infernos.goety.common.crafting;

import za.co.infernos.goety.Goety;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import za.co.infernos.goety.compat.fml.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neoforged.neoforge.registries.DeferredHolder;



public class ModRecipeSerializer {

        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(
                        BuiltInRegistries.RECIPE_TYPE, Goety.MOD_ID);

        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
                        BuiltInRegistries.RECIPE_SERIALIZER, Goety.MOD_ID);

        public static void init() {
                RECIPE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
                RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final DeferredHolder<RecipeType<?>, RecipeType<CursedInfuserRecipes>> CURSED_INFUSER = register(
                        "cursed_infuser");

        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CursedInfuserRecipes>> CURSED_INFUSER_RECIPES = RECIPE_SERIALIZERS
                        .register("cursed_infuser",
                                        () -> new CursedInfuserRecipeSerializer<>(CursedInfuserRecipes::new, 60));

        public static final DeferredHolder<RecipeType<?>, RecipeType<SoulAbsorberRecipes>> SOUL_ABSORBER = register(
                        "soul_absorber");

        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SoulAbsorberRecipes>> SOUL_ABSORBER_RECIPES = RECIPE_SERIALIZERS
                        .register("soul_absorber_recipes",
                                        () -> new SoulAbsorberRecipeSerializer<>(SoulAbsorberRecipes::new, 25, 200));

        public static final DeferredHolder<RecipeType<?>, RecipeType<RitualRecipe>> RITUAL_TYPE = register("ritual");

        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RitualRecipe>> RITUAL = RECIPE_SERIALIZERS
                        .register("ritual",
                                        () -> RitualRecipe.SERIALIZER);

        public static final DeferredHolder<RecipeType<?>, RecipeType<BrazierRecipe>> BRAZIER_TYPE = register("brazier");

        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BrazierRecipe>> BRAZIER = RECIPE_SERIALIZERS
                        .register("brazier",
                                        () -> BrazierRecipe.SERIALIZER);

        public static final DeferredHolder<RecipeType<?>, RecipeType<BrewingRecipe>> BREWING_TYPE = register("brewing");

        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BrewingRecipe>> BREWING = RECIPE_SERIALIZERS
                        .register("brewing",
                                        () -> new RecipeSerializer<BrewingRecipe>() {
                                                @Override
                                                public MapCodec<BrewingRecipe> codec() {
                                                        return BrewingRecipe.CODEC;
                                                }

                                                @Override
                                                public StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, BrewingRecipe> streamCodec() {
                                                        return BrewingRecipe.STREAM_CODEC;
                                                }
                                        });

        public static final DeferredHolder<RecipeType<?>, RecipeType<PulverizeRecipe>> PULVERIZE_TYPE = register(
                        "pulverize");

        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PulverizeRecipe>> PULVERIZE = RECIPE_SERIALIZERS
                        .register("pulverize",
                                        () -> PulverizeRecipe.SERIALIZER);

        // 1.21+: vanilla shapeless recipe already supports complex ingredients via
        // codecs/stream codecs.
        // This legacy shim is disabled until the custom serializer is ported.
        // public static final RegistryObject<RecipeSerializer<ModShapelessRecipe>>
        // MODDED_SHAPELESS = RECIPE_SERIALIZERS.register("crafting_shapeless",
        // ModShapelessRecipe.Serializer::new);

        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TaglockRecipe>> TAGLOCK = RECIPE_SERIALIZERS
                        .register("taglock", () -> TaglockRecipe.SERIALIZER);

        static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(final String id) {
                return RECIPE_TYPES.register(id, () -> RecipeType.simple(Goety.location(id)));
        }
}