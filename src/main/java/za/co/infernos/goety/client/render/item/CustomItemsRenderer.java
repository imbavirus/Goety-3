package za.co.infernos.goety.client.render.item;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.ModModelLayer;
import za.co.infernos.goety.client.render.ModRenderType;
import za.co.infernos.goety.client.render.model.NamelessStaffModel;
import za.co.infernos.goety.common.items.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CustomItemsRenderer extends BlockEntityWithoutLevelRenderer {
    public static int ticksExisted = 0;
    private final NamelessStaffModel<?> staffModel;
    private static final ResourceLocation NAMELESS_STAFF_TEXTURE = Goety.location("textures/item/nameless_staff_model.png");
    private static final ResourceLocation NAMELESS_STAFF_ORB_TEXTURE = Goety.location("textures/item/nameless_staff_orb.png");
    private static final ResourceLocation NAMELESS_STAFF_ORB_CENTER_TEXTURE = Goety.location("textures/item/nameless_staff_orb_center.png");

    public CustomItemsRenderer() {
        this(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    public CustomItemsRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        this.staffModel = new NamelessStaffModel<>(p_172551_.bakeLayer(ModModelLayer.NAMELESS_STAFF));
    }

    public static void incrementTick() {
        ++ticksExisted;
    }

    public void renderByItem(ItemStack itemStackIn, ItemDisplayContext transformType, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);

        int tick;
        if (Minecraft.getInstance().player != null && !Minecraft.getInstance().isPaused()) {
            tick = Minecraft.getInstance().player.tickCount;
        } else {
            tick = ticksExisted;
        }

        if (itemStackIn.getItem() == ModItems.NAMELESS_STAFF.get()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5F, 0.5F, 0.5F);
            matrixStackIn.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(NAMELESS_STAFF_TEXTURE), itemStackIn.hasFoil());
            this.staffModel.renderToBuffer(matrixStackIn, vertexconsumer, combinedLightIn, combinedOverlayIn, -1);
            matrixStackIn.popPose();
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5F, 0.5F, 0.5F);
            matrixStackIn.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer vertexconsumer2 = bufferIn.getBuffer(RenderType.eyes(NAMELESS_STAFF_ORB_TEXTURE));
            this.staffModel.animate(tick + partialTick);
            this.staffModel.renderToBuffer(matrixStackIn, vertexconsumer2, 15728640, OverlayTexture.NO_OVERLAY, -1);
            matrixStackIn.popPose();
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5F, 0.5F, 0.5F);
            matrixStackIn.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer vertexconsumer3 = bufferIn.getBuffer(ModRenderType.orbCenter(NAMELESS_STAFF_ORB_CENTER_TEXTURE));
            this.staffModel.renderToBuffer(matrixStackIn, vertexconsumer3, 15728640, OverlayTexture.NO_OVERLAY, -1);
            matrixStackIn.popPose();
        }
    }
}