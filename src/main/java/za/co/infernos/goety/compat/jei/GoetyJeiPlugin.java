package za.co.infernos.goety.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.crafting.CursedInfuserRecipes;
import za.co.infernos.goety.common.crafting.ModRecipeSerializer;

@JeiPlugin
public class GoetyJeiPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelper;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper = registration.getJeiHelpers();
        registration.addRecipeCategories(new CursedInfuserCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CURSED_INFUSER.get()), JeiRecipeTypes.CURSED_INFUSER);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GRIM_INFUSER.get()), JeiRecipeTypes.CURSED_INFUSER);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);
        RecipeManager recipeManager = level.getRecipeManager();
        List<CursedInfuserRecipes> cursedRecipes = recipeManager
                .getAllRecipesFor(ModRecipeSerializer.CURSED_INFUSER.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(JeiRecipeTypes.CURSED_INFUSER, cursedRecipes);
    }
}
