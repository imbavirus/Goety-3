package za.co.infernos.goety.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

public class LichCuriosLayer<T extends LivingEntity, M extends EntityModel<T>> extends
        RenderLayer<T, M> {
    private final RenderLayerParent<T, M> renderLayerParent;

    public LichCuriosLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
        this.renderLayerParent = renderer;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource renderTypeBuffer,
                       int light, @Nonnull T livingEntity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        // Curios not present on the compile classpath for this 1.21.1 NeoForge port (yet).
        // TODO: Re-enable this layer when Curios 1.21.1 is available.
    }
}