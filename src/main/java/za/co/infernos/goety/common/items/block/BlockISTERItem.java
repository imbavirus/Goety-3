package za.co.infernos.goety.common.items.block;

import za.co.infernos.goety.client.render.block.ModISTER;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class BlockISTERItem extends BlockItemBase{

    public BlockISTERItem(Block blockIn) {
        super(blockIn);
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