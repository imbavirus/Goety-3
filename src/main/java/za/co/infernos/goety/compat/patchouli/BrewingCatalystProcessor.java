package za.co.infernos.goety.compat.patchouli;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.crafting.BrewingRecipe;
import za.co.infernos.goety.common.crafting.ModRecipeSerializer;
import za.co.infernos.goety.common.effects.brew.BrewEffects;

import java.util.Map;

/**
 * Patchouli page processor for brewing catalyst recipes.
 * Processes brewing recipe data and replaces template placeholders.
 */
public class BrewingCatalystProcessor {
    
    /**
     * Process brewing catalyst recipe data for Patchouli template.
     * This method is called by Patchouli via reflection.
     * 
     * @param effectId The effect ID (e.g., "effect.minecraft.absorption")
     * @param level The level (can be null for book rendering)
     * @return A map of placeholder keys to their replacement values
     */
    public static Map<String, String> process(String effectId, Object level) {
        try {
            ResourceLocation id = ResourceLocation.parse(effectId);
            
            // Get the effect from the registry
            MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(id);
            if (effect == null) {
                Goety.LOGGER.warn("Mob effect {} not found", effectId);
                return java.util.Collections.emptyMap();
            }
            
            // Get brewing recipe for this effect
            BrewingRecipe recipe = null;
            if (level != null) {
                try {
                    // Use reflection to access recipe manager
                    Object recipeManager = level.getClass().getMethod("getRecipeManager").invoke(level);
                    if (recipeManager != null) {
                        Object recipeType = ModRecipeSerializer.BREWING_TYPE.get();
                        java.util.List<?> recipes = (java.util.List<?>) recipeManager.getClass()
                            .getMethod("getAllRecipesFor", net.minecraft.world.item.crafting.RecipeType.class)
                            .invoke(recipeManager, recipeType);
                        
                        for (Object holder : recipes) {
                            RecipeHolder<?> recipeHolder = (RecipeHolder<?>) holder;
                            if (recipeHolder.value() instanceof BrewingRecipe) {
                                BrewingRecipe brewingRecipe = (BrewingRecipe) recipeHolder.value();
                                if (brewingRecipe.getOutput().value() == effect) {
                                    recipe = brewingRecipe;
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Goety.LOGGER.warn("Failed to load brewing recipe for effect {} from recipe manager: {}", effectId, e.getMessage());
                }
            }
            
            // Build replacement map
            java.util.Map<String, String> replacements = new java.util.HashMap<>();
            
            // Output effect name
            replacements.put("output", effect.getDisplayName().getString());
            
            // Input/catalyst
            ItemStack catalyst = BrewEffects.INSTANCE.getCatalystFromEffect(effectId);
            if (!catalyst.isEmpty()) {
                replacements.put("input", catalyst.getItem().toString());
            } else if (recipe != null && !recipe.getInput().isEmpty()) {
                ItemStack[] stacks = recipe.getInput().getItems();
                if (stacks.length > 0) {
                    replacements.put("input", stacks[0].getItem().toString());
                }
            }
            
            // Recipe data
            if (recipe != null) {
                replacements.put("soulCost", String.valueOf(recipe.getSoulCost()));
                replacements.put("duration", String.valueOf(recipe.getDuration()));
                replacements.put("capacityExtra", String.valueOf(recipe.getCapacityExtra()));
            } else {
                // Fallback values
                replacements.put("soulCost", "0");
                replacements.put("duration", "0");
                replacements.put("capacityExtra", "0");
            }
            
            // Linger (not typically in brewing recipes, but placeholder for template)
            replacements.put("linger", "");
            
            // Text (custom text can be provided in the entry JSON)
            replacements.put("text", "");
            
            return replacements;
        } catch (Exception e) {
            Goety.LOGGER.error("Error processing brewing catalyst recipe {}: {}", effectId, e.getMessage(), e);
            return java.util.Collections.emptyMap();
        }
    }
}
