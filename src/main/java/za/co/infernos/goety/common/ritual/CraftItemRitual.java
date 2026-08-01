package za.co.infernos.goety.common.ritual;

import za.co.infernos.goety.api.items.magic.IWand;
import za.co.infernos.goety.common.blocks.entities.DarkAltarBlockEntity;
import za.co.infernos.goety.common.crafting.RitualRecipe;
import za.co.infernos.goety.common.items.handler.SoulUsingItemHandler;
import za.co.infernos.goety.config.MainConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

public class CraftItemRitual extends Ritual{

    public CraftItemRitual(RitualRecipe recipe) {
        super(recipe);
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

        ItemStack result = this.recipe.getResultItem(world.registryAccess()).copy();

        if (activationItem.getItem() instanceof IWand && result.getItem() instanceof IWand){
            SoulUsingItemHandler initWand = SoulUsingItemHandler.get(activationItem);
            SoulUsingItemHandler resultWand = SoulUsingItemHandler.get(result);

            resultWand.insertItem(initWand.getStackInSlot(0));
        }
        // TODO NeoForge 1.21: restore enchantment transfer using Holder-based APIs.
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.RitualCraftDamage, false)) {
            if (result.isDamageableItem()) {
                float percent = 1.0F - (float) (activationItem.getMaxDamage() - activationItem.getDamageValue()) / activationItem.getMaxDamage();
                int damage = (int) (result.getMaxDamage() * percent);
                result.setDamageValue(damage);
            }
        }
        activationItem.shrink(1);
        result.onCraftedBy(world, castingPlayer, 1);
        IItemHandler handler = tileEntity.itemStackHandler;
        handler.insertItem(0, result, false);
    }
}