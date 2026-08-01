package za.co.infernos.goety.client.render.block;

import za.co.infernos.goety.common.blocks.entities.PedestalBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PedestalRenderer implements BlockEntityRenderer<PedestalBlockEntity> {
    public PedestalRenderer(BlockEntityRendererProvider.Context p_i226007_1_) {
    }

    public void render(PedestalBlockEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        ItemStackHandler handler = pBlockEntity.itemStackHandler;
        ItemStack stack = handler.getStackInSlot(0);
        Minecraft minecraft = Minecraft.getInstance();
        if (!stack.isEmpty()) {
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.5D, 0.75D, 0.5D);
            pMatrixStack.scale(0.5F, 0.5F, 0.5F);
            long time = 0;
            if (minecraft.level != null) {
                time = minecraft.level.getGameTime();
                pMatrixStack.mulPose(Axis.YP.rotationDegrees(3 * (time % 360 + pPartialTicks)));
            }
            minecraft.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, pCombinedLight, pCombinedOverlay, pMatrixStack, pBuffer, pBlockEntity.getLevel(), 0);
            pMatrixStack.popPose();
        }
    }

}
