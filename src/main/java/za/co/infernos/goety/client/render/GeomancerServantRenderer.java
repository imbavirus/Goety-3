package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goety.client.render.model.GeomancerModel;
import za.co.infernos.goety.common.entities.ally.illager.GeomancerServant;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class GeomancerServantRenderer<T extends GeomancerServant> extends MobRenderer<T, GeomancerModel<T>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/illager/geomancer.png");
    protected static final ResourceLocation ORIGINAL = Goety.location("textures/entity/servants/illager/geomancer_original.png");

    public GeomancerServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new GeomancerModel<>(renderManagerIn.bakeLayer(ModModelLayer.GEOMANCER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
        this.addLayer(new NecklaceGlowLayer<>(this));
    }

    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.GeomancerServantTexture, false)){
            return ORIGINAL;
        } else {
            return TEXTURE;
        }
    }

    public static class NecklaceGlowLayer<T extends LivingEntity, M extends GeomancerModel<T>> extends EyesLayer<T, M> {
        private static final RenderType NECKLACE = RenderType.eyes(Goety.location("textures/entity/servants/illager/geomancer_glow.png"));

        public NecklaceGlowLayer(RenderLayerParent<T, M> p_117507_) {
            super(p_117507_);
        }

        public RenderType renderType() {
            return NECKLACE;
        }
    }
}