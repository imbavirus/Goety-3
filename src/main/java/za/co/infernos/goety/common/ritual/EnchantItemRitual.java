package za.co.infernos.goety.common.ritual;

import za.co.infernos.goety.common.blocks.entities.DarkAltarBlockEntity;
import za.co.infernos.goety.common.crafting.RitualRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnchantItemRitual extends Ritual{

    public EnchantItemRitual(RitualRecipe recipe) {
        super(recipe);
    }

    public boolean isValid(Level world, BlockPos darkAltarPos, DarkAltarBlockEntity tileEntity,
                           Player castingPlayer, ItemStack activationItem,
                           List<Ingredient> remainingAdditionalIngredients) {
        return this.areAdditionalIngredientsFulfilled(world, darkAltarPos, castingPlayer, remainingAdditionalIngredients);
    }

    public boolean identify(Level world, BlockPos darkAltarPos, Player player, ItemStack activationItem) {
        return this.areAdditionalIngredientsFulfilled(world, darkAltarPos, player, this.recipe.getIngredients());
    }

    public int getLevelCost(ItemStack activationItem){
        return this.recipe.getXPLevelCost();
    }

    public boolean compatibleEnchant(ItemStack activationItem){
        return true;
    }

    @Override
    public void finish(Level world, BlockPos blockPos, DarkAltarBlockEntity tileEntity,
                       Player castingPlayer, ItemStack activationItem) {
        super.finish(world, blockPos, tileEntity, castingPlayer, activationItem);

        for(int i = 0; i < 20; ++i) {
            double d0 = (double)blockPos.getX() + world.random.nextDouble();
            double d1 = (double)blockPos.getY() + world.random.nextDouble();
            double d2 = (double)blockPos.getZ() + world.random.nextDouble();
            world.addParticle(ParticleTypes.POOF, d0, d1, d2, 0, 0, 0);
        }

        activationItem.onCraftedBy(world, castingPlayer, 1);
    }
}