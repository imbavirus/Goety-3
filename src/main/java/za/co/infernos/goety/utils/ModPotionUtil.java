package za.co.infernos.goety.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import za.co.infernos.goety.utils.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;

public class ModPotionUtil extends BrewingRecipe {

    private final ItemStack inputStack;

    public ModPotionUtil(ItemStack inputStack, Ingredient ingredient, ItemStack output) {
        super(Ingredient.of(inputStack), ingredient, output);
        this.inputStack = inputStack;
    }

    @Override
    public boolean isInput(ItemStack stack) {
        return super.isInput(stack) && stack.has(net.minecraft.core.component.DataComponents.POTION_CONTENTS)
                && inputStack.has(net.minecraft.core.component.DataComponents.POTION_CONTENTS) &&
                stack.get(net.minecraft.core.component.DataComponents.POTION_CONTENTS)
                        .equals(inputStack.get(net.minecraft.core.component.DataComponents.POTION_CONTENTS));
    }

    public static ItemStack setPotion(net.minecraft.core.Holder<Potion> pPotion) {
        return net.minecraft.world.item.alchemy.PotionContents.createItemStack(Items.POTION, pPotion);
    }

    public static ItemStack setSplashPotion(net.minecraft.core.Holder<Potion> pPotion) {
        return net.minecraft.world.item.alchemy.PotionContents.createItemStack(Items.SPLASH_POTION, pPotion);
    }

    public static ItemStack setLingeringPotion(net.minecraft.core.Holder<Potion> pPotion) {
        return net.minecraft.world.item.alchemy.PotionContents.createItemStack(Items.LINGERING_POTION, pPotion);
    }
}