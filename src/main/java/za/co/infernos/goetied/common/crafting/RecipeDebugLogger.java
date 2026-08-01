package za.co.infernos.goetied.common.crafting;

import za.co.infernos.goetied.Goetied;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Collection;

@EventBusSubscriber(modid = Goetied.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class RecipeDebugLogger {

    @SubscribeEvent
    public static void onRegisterRecipeTypes(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_TYPE) {
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] ========== Registering Recipe Types ==========");
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] CURSED_INFUSER: {}", ModRecipeSerializer.CURSED_INFUSER.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] SOUL_ABSORBER: {}", ModRecipeSerializer.SOUL_ABSORBER.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] RITUAL_TYPE: {}", ModRecipeSerializer.RITUAL_TYPE.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] BRAZIER_TYPE: {}", ModRecipeSerializer.BRAZIER_TYPE.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] BREWING_TYPE: {}", ModRecipeSerializer.BREWING_TYPE.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] PULVERIZE_TYPE: {}", ModRecipeSerializer.PULVERIZE_TYPE.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] ===============================================");
        }
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] ========== Registering Recipe Serializers ==========");
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] CURSED_INFUSER_RECIPES: {}", ModRecipeSerializer.CURSED_INFUSER_RECIPES.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] SOUL_ABSORBER_RECIPES: {}", ModRecipeSerializer.SOUL_ABSORBER_RECIPES.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] RITUAL: {}", ModRecipeSerializer.RITUAL.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] BRAZIER: {}", ModRecipeSerializer.BRAZIER.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] BREWING: {}", ModRecipeSerializer.BREWING.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] PULVERIZE: {}", ModRecipeSerializer.PULVERIZE.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] TAGLOCK: {}", ModRecipeSerializer.TAGLOCK.getId());
            Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] ===================================================");
        }
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        RecipeManager recipeManager = server.getRecipeManager();
        
        Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] ========== Recipe Loading Report ==========");
        
        // Log all recipe types and their counts
        logRecipeType(recipeManager, ModRecipeSerializer.CURSED_INFUSER.get(), "CURSED_INFUSER");
        logRecipeType(recipeManager, ModRecipeSerializer.SOUL_ABSORBER.get(), "SOUL_ABSORBER");
        logRecipeType(recipeManager, ModRecipeSerializer.RITUAL_TYPE.get(), "RITUAL");
        logRecipeType(recipeManager, ModRecipeSerializer.BRAZIER_TYPE.get(), "BRAZIER");
        logRecipeType(recipeManager, ModRecipeSerializer.BREWING_TYPE.get(), "BREWING");
        logRecipeType(recipeManager, ModRecipeSerializer.PULVERIZE_TYPE.get(), "PULVERIZE");
        
        // Log all recipes by ID
        Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] ========== All Goetied Recipes by ID ==========");
        for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
            ResourceLocation id = holder.id();
            if (id.getNamespace().equals(Goetied.MOD_ID)) {
                Recipe<?> recipe = holder.value();
                Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] Recipe: {} | Type: {} | Serializer: {}", 
                    id, 
                    recipe.getType().toString(),
                    recipe.getSerializer().toString());
            }
        }
        Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] ===========================================");
    }

    private static void logRecipeType(RecipeManager recipeManager, RecipeType<?> type, String name) {
        Collection<RecipeHolder<?>> recipes = recipeManager.getRecipes().stream()
            .filter(holder -> holder.value().getType() == type)
            .toList();
        
        Goetied.LOGGER.info("[GOETIED RECIPE DEBUG] {}: Found {} recipes", name, recipes.size());
        if (recipes.isEmpty()) {
            Goetied.LOGGER.warn("[GOETIED RECIPE DEBUG] WARNING: No recipes found for type {}!", name);
        } else {
            for (RecipeHolder<?> holder : recipes) {
                Goetied.LOGGER.info("[GOETIED RECIPE DEBUG]   - {}", holder.id());
            }
        }
    }
}
