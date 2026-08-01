package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.QuickGrowingVineModel;
import za.co.infernos.goety.common.entities.neutral.QuickGrowingKelp;
import za.co.infernos.goety.common.entities.neutral.QuickGrowingVine;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class QuickGrowingVineRenderer<T extends QuickGrowingVine> extends MobRenderer<T, QuickGrowingVineModel<T>> {
    private static final ResourceLocation TEXTURE_LOCATION = Goety.location("textures/entity/monolith/quick_growing_vine/quick_growing_vine.png");
    private static final ResourceLocation ORIGINAL = Goety.location("textures/entity/monolith/quick_growing_vine/quick_growing_vine_original.png");
    private static final ResourceLocation KELP = Goety.location("textures/entity/monolith/quick_growing_vine/quick_growing_kelp.png");

    public QuickGrowingVineRenderer(EntityRendererProvider.Context p_i47208_1_) {
        super(p_i47208_1_, new QuickGrowingVineModel<>(p_i47208_1_.bakeLayer(ModModelLayer.QUICK_GROWING_VINE)), 0.5F);
        this.addLayer(new GlowLayer<>(this));
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        if (pEntity instanceof QuickGrowingKelp){
            return KELP;
        } else if (pEntity.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.QuickGrowingVineTexture, false)){
            return ORIGINAL;
        } else {
            return TEXTURE_LOCATION;
        }
    }

    public static class GlowLayer<T extends QuickGrowingVine, M extends QuickGrowingVineModel<T>> extends EyesLayer<T, M> {
        private static final RenderType RENDER_TYPE = RenderType.eyes(Goety.location("textures/entity/monolith/quick_growing_vine/quick_growing_vine_glow.png"));
        private static final RenderType ORIGINAL = RenderType.eyes(Goety.location("textures/entity/monolith/quick_growing_vine/quick_growing_vine_glow_original.png"));
        private static final RenderType KELP = RenderType.eyes(Goety.location("textures/entity/monolith/quick_growing_vine/quick_growing_kelp_glow.png"));

        public GlowLayer(RenderLayerParent<T, M> p_i50919_1_) {
            super(p_i50919_1_);
        }

        public void render(PoseStack p_116983_, MultiBufferSource p_116984_, int p_116985_, T p_116986_, float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_, float p_116992_) {
            if (!p_116986_.isInvisible()) {
                VertexConsumer vertexconsumer = p_116984_.getBuffer(this.renderType());
                if (p_116986_ instanceof QuickGrowingKelp){
                    vertexconsumer = p_116984_.getBuffer(KELP);
                } else if (p_116986_.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.QuickGrowingVineTexture, false)) {
                    vertexconsumer = p_116984_.getBuffer(ORIGINAL);
                }
                this.getParentModel().renderToBuffer(p_116983_, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, -1);
            }
        }

        @Override
        public RenderType renderType() {
            return RENDER_TYPE;
        }
    }
}