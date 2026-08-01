package za.co.infernos.goety.client.render.block;

import za.co.infernos.goety.client.render.ModRenderType;
import za.co.infernos.goety.common.blocks.entities.HoleBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class HoleBlockEntityRenderer<T extends HoleBlockEntity> implements BlockEntityRenderer<T> {

    public HoleBlockEntityRenderer(BlockEntityRendererProvider.Context p_173529_) {
    }

    @Override
    public void render(T entity, float p_112651_, PoseStack poseStack, MultiBufferSource bufferSource, int p_112654_, int p_112655_) {
        Matrix4f matrix4f = poseStack.last().pose();
        this.renderCube(entity, matrix4f, bufferSource.getBuffer(this.renderType()));
    }

    private void renderCube(T entity, Matrix4f matrix4f, VertexConsumer consumer) {
        this.renderFace(entity, matrix4f, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 0.9995F, 0.9995F, 0.9995F, 0.9995F, Direction.SOUTH);
        this.renderFace(entity, matrix4f, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0005F, 0.0005F, 0.0005F, 0.0005F, Direction.NORTH);
        this.renderFace(entity, matrix4f, consumer, 0.9995F, 0.9995F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(entity, matrix4f, consumer, 0.0005F, 0.0005F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderFace(entity, matrix4f, consumer, 0.0F, 1.0F, 0.0005F, 0.0005F, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderFace(entity, matrix4f, consumer, 0.0F, 1.0F, 0.9995F, 0.9995F, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderFace(T entity, Matrix4f matrix4f, VertexConsumer consumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction direction) {
        if (entity.shouldRenderFace(direction)) {
            this.vertex(consumer, matrix4f, x1, y1, z1);
            this.vertex(consumer, matrix4f, x2, y1, z2);
            this.vertex(consumer, matrix4f, x2, y2, z3);
            this.vertex(consumer, matrix4f, x1, y2, z4);
        }
    }

    private void vertex(VertexConsumer consumer, Matrix4f matrix4f, float x, float y, float z) {
        Vector4f vector4f = matrix4f.transform(new Vector4f(x, y, z, 1.0F));
        consumer.addVertex(vector4f.x(), vector4f.y(), vector4f.z());
    }

    protected RenderType renderType() {
        return ModRenderType.hole();
    }

    public int getViewDistance() {
        return 256;
    }
}