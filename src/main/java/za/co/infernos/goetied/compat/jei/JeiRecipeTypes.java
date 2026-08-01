package za.co.infernos.goetied.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.crafting.CursedInfuserRecipes;

public class JeiRecipeTypes {
    public static final RecipeType<CursedInfuserRecipes> CURSED_INFUSER =
            RecipeType.create(Goetied.MOD_ID, "cursed_infuser", CursedInfuserRecipes.class);
}
