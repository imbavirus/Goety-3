package za.co.infernos.goety.common.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class SoulAbsorberRecipes implements Recipe<net.minecraft.world.item.crafting.SingleRecipeInput> {
    public final Ingredient ingredient;
    public final int soulIncrease;
    public final int cookingTime;

    public SoulAbsorberRecipes(Ingredient pIngredient, int pSouls, int pCookingTime) {
        this.ingredient = pIngredient;
        this.soulIncrease = pSouls;
        this.cookingTime = pCookingTime;
    }

    public boolean matches(net.minecraft.world.item.crafting.SingleRecipeInput pInv, Level pLevel) {
        return this.ingredient.test(pInv.item());
    }

    @Override

    public ItemStack assemble(net.minecraft.world.item.crafting.SingleRecipeInput pInv, net.minecraft.core.HolderLookup.Provider pAccess) {
        return this.getResultItem(pAccess);
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public int getSoulIncrease() {
        return this.soulIncrease;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider pAccess) {
        // Soul Absorber recipes don't produce items, they consume items and increase soul energy
        // Return a dummy item for recipe encoding (Minecraft 1.21.1 doesn't allow empty ItemStacks in recipes)
        // The actual result is consumed and soul energy is added to the player's Arca
        return za.co.infernos.goety.common.items.ModItems.JEI_DUMMY_NONE.get().getDefaultInstance();
    }



    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.SOUL_ABSORBER_RECIPES.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeSerializer.SOUL_ABSORBER.get();
    }
}