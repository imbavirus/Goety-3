package za.co.infernos.goety.client.render.block;

import za.co.infernos.goety.common.blocks.entities.VoidShrineBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class VoidShrineRenderer implements BlockEntityRenderer<VoidShrineBlockEntity> {
    public VoidShrineRenderer(BlockEntityRendererProvider.Context p_i226007_1_) {
    }

    public void render(VoidShrineBlockEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        net.neoforged.neoforge.items.ItemStackHandler handler = pBlockEntity.itemStackHandler;
        ItemStack stack = handler.getStackInSlot(0);
        Minecraft minecraft = Minecraft.getInstance();
        if (!stack.isEmpty()) {
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.5F, 1.1F, 0.5F);
            pMatrixStack.scale(1.0F, 1.0F, 1.0F);
            if (minecraft.level != null){
                pMatrixStack.mulPose(Axis.YP.rotationDegrees(3 * (minecraft.level.getGameTime() % 360 + pPartialTicks)));
            }
            minecraft.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, pCombinedLight, pCombinedOverlay, pMatrixStack, pBuffer, pBlockEntity.getLevel(), 0);
            pMatrixStack.popPose();
        }
    }

}
