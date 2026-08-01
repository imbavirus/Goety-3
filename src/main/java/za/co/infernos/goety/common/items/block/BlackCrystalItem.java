package za.co.infernos.goety.common.items.block;

import za.co.infernos.goety.client.render.block.ModISTER;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class BlackCrystalItem extends BlockItemBase {

    public BlackCrystalItem() {
        super(ModBlocks.BLACK_CRYSTAL.get());
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.world.item.enchantment.Enchantment enchantment) {
        return stack.getCount() == 1
                && (enchantment == ModEnchantments.SOUL_EATER.get()
                || enchantment == ModEnchantments.RADIUS.get());
    }

    public int getMaxStackSize(ItemStack itemStack){
        return itemStack.isEnchanted() ? 1 : super.getMaxStackSize(itemStack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 25;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new ModISTER();
            }
        });
    }
}