package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goetied.client.render.model.GeomancerModel;
import za.co.infernos.goetied.common.entities.ally.undead.bound.BoundGeomancer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class BoundGeomancerRenderer<T extends BoundGeomancer> extends MobRenderer<T, GeomancerModel<T>> {
    protected static final ResourceLocation TEXTURE = Goetied.location("textures/entity/servants/bound_illager/bound_geomancer.png");
    protected static final ResourceLocation CASTING = Goetied.location("textures/entity/servants/bound_illager/bound_geomancer_casting.png");

    public BoundGeomancerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new GeomancerModel<>(renderManagerIn.bakeLayer(ModModelLayer.GEOMANCER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
        this.addLayer(new NecklaceGlowLayer<>(this));
    }

    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        if (p_114482_.isAttacking()){
            return CASTING;
        }
        return TEXTURE;
    }

    public static class NecklaceGlowLayer<T extends LivingEntity, M extends GeomancerModel<T>> extends EyesLayer<T, M> {
        private static final RenderType NECKLACE = RenderType.eyes(Goetied.location("textures/entity/servants/illager/geomancer_glow.png"));

        public NecklaceGlowLayer(RenderLayerParent<T, M> p_117507_) {
            super(p_117507_);
        }

        public RenderType renderType() {
            return NECKLACE;
        }
    }
}