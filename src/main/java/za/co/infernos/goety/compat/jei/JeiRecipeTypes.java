package za.co.infernos.goety.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.crafting.CursedInfuserRecipes;

public class JeiRecipeTypes {
    public static final RecipeType<CursedInfuserRecipes> CURSED_INFUSER =
            RecipeType.create(Goety.MOD_ID, "cursed_infuser", CursedInfuserRecipes.class);
}
