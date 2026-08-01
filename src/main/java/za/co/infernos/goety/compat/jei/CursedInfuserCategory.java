package za.co.infernos.goety.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.crafting.CursedInfuserRecipes;

public class CursedInfuserCategory implements IRecipeCategory<CursedInfuserRecipes> {
    private static final int WIDTH = 82;
    private static final int HEIGHT = 36;

    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final int regularCookTime;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public CursedInfuserCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.CURSED_INFUSER.get()));
        ResourceLocation guiTexture = ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "textures/gui/jei/jei_gui.png");
        this.background = guiHelper.drawableBuilder(guiTexture, 0, 220, WIDTH, HEIGHT)
                .addPadding(0, 0, 0, 0)
                .build();
        this.regularCookTime = 400;
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(@Nonnull Integer cookTime) {
                        return guiHelper.drawableBuilder(guiTexture, 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        this.localizedName = Component.translatable("gui.jei.category.cursed_infuser");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public RecipeType<CursedInfuserRecipes> getRecipeType() {
        return JeiRecipeTypes.CURSED_INFUSER;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public void draw(CursedInfuserRecipes recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(guiGraphics, 24, 8);
        drawCookTime(recipe, guiGraphics, 30);
    }

    private IDrawableAnimated getArrow(CursedInfuserRecipes recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    private void drawCookTime(CursedInfuserRecipes recipe, GuiGraphics matrixStack, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            MutableComponent timeString = Component.translatable("gui.jei.category.cursed_infuser.time", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            matrixStack.drawString(fontRenderer, timeString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CursedInfuserRecipes recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                .addIngredients(recipe.getIngredients().get(0));

        if (recipe.isGrim()) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 30, 20)
                    .addIngredients(Ingredient.of(ModBlocks.GRIM_INFUSER.get()));
        } else {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 30, 20)
                    .addIngredients(Ingredient.of(ModBlocks.CURSED_INFUSER.get()));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9)
                .addItemStack(recipe.getResultItem(null));
    }
}
