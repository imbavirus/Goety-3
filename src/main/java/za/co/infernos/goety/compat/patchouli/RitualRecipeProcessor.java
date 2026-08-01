package za.co.infernos.goety.compat.patchouli;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.crafting.RitualRecipe;
import za.co.infernos.goety.common.crafting.ModRecipeSerializer;

import java.util.Map;

/**
 * Patchouli page processor for ritual recipes.
 * Processes ritual recipe data and replaces template placeholders.
 */
public class RitualRecipeProcessor {
    
    /**
     * Process ritual recipe data for Patchouli template.
     * This method is called by Patchouli via reflection.
     * 
     * @param recipeId The recipe ID (e.g., "goety:convert_villager_to_ravaged")
     * @param level The level (can be null for book rendering)
     * @return A map of placeholder keys to their replacement values
     */
    public static Map<String, String> process(String recipeId, Object level) {
        try {
            ResourceLocation id = ResourceLocation.parse(recipeId);
            
            // Try to get the recipe from the recipe manager if level is available
            RitualRecipe recipe = null;
            if (level != null) {
                try {
                    // Use reflection to access recipe manager
                    Object recipeManager = level.getClass().getMethod("getRecipeManager").invoke(level);
                    if (recipeManager != null) {
                        Object recipeType = ModRecipeSerializer.RITUAL_TYPE.get();
                        java.util.List<?> recipes = (java.util.List<?>) recipeManager.getClass()
                            .getMethod("getAllRecipesFor", net.minecraft.world.item.crafting.RecipeType.class)
                            .invoke(recipeManager, recipeType);
                        
                        for (Object holder : recipes) {
                            RecipeHolder<?> recipeHolder = (RecipeHolder<?>) holder;
                            if (recipeHolder.id().equals(id) && recipeHolder.value() instanceof RitualRecipe) {
                                recipe = (RitualRecipe) recipeHolder.value();
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    Goety.LOGGER.warn("Failed to load ritual recipe {} from recipe manager: {}", recipeId, e.getMessage());
                }
            }
            
            // If recipe not found, return empty map (Patchouli will handle gracefully)
            if (recipe == null) {
                Goety.LOGGER.warn("Ritual recipe {} not found", recipeId);
                return java.util.Collections.emptyMap();
            }
            
            // Build replacement map
            java.util.Map<String, String> replacements = new java.util.HashMap<>();
            
            // Basic recipe info
            replacements.put("craftType", recipe.getCraftType());
            replacements.put("soulCost", String.valueOf(recipe.getSoulCost()));
            replacements.put("duration", String.valueOf(recipe.getDuration()));
            
            // Activation item
            Ingredient activationItem = recipe.getActivationItem();
            if (!activationItem.isEmpty()) {
                ItemStack[] stacks = activationItem.getItems();
                if (stacks.length > 0) {
                    replacements.put("activation_item", stacks[0].getItem().toString());
                }
            }
            
            // Ingredients (up to 12)
            for (int i = 0; i < Math.min(recipe.getIngredients().size(), 12); i++) {
                Ingredient ing = recipe.getIngredients().get(i);
                if (!ing.isEmpty()) {
                    ItemStack[] stacks = ing.getItems();
                    if (stacks.length > 0) {
                        replacements.put("ingredient" + (i + 1), stacks[0].getItem().toString());
                    }
                }
            }
            
            // Pedestals (default to empty pedestal item if not specified)
            // In Goety, pedestals are typically empty, but we can use a default
            for (int i = 1; i <= 12; i++) {
                replacements.put("pedestal" + i, "goety:pedestal");
            }
            
            // Result/output
            ItemStack result = recipe.getResultItem(null);
            if (!result.isEmpty()) {
                replacements.put("output", result.getItem().toString());
            }
            
            // Entity-related fields (if applicable)
            if (recipe.getEntityToSacrifice() != null) {
                replacements.put("entity_to_sacrifice", recipe.getEntityToSacrificeDisplayName());
            }
            if (recipe.getEntityToConvert() != null) {
                replacements.put("entity_to_convert", recipe.getEntityToConvertDisplayName());
            }
            if (recipe.getEntityToSummon() != null) {
                replacements.put("entity_to_summon", BuiltInRegistries.ENTITY_TYPE.getKey(recipe.getEntityToSummon()).toString());
            }
            if (recipe.getEntityToConvertInto() != null) {
                replacements.put("entity_to_convert_into", BuiltInRegistries.ENTITY_TYPE.getKey(recipe.getEntityToConvertInto()).toString());
            }
            
            // XP levels
            if (recipe.getXPLevelCost() > 0) {
                replacements.put("xp_levels", String.valueOf(recipe.getXPLevelCost()));
            }
            
            // Enchantment (if applicable - not typically in ritual recipes)
            replacements.put("enchantment", "");
            
            return replacements;
        } catch (Exception e) {
            Goety.LOGGER.error("Error processing ritual recipe {}: {}", recipeId, e.getMessage(), e);
            return java.util.Collections.emptyMap();
        }
    }
}
