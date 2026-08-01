package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.PhantomServantModel;
import za.co.infernos.goety.common.entities.ally.undead.PhantomServant;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class PhantomServantRenderer extends MobRenderer<PhantomServant, PhantomServantModel<PhantomServant>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/phantom_servant.png");
    protected static final ResourceLocation PHANTOM_LOCATION = ResourceLocation.parse("textures/entity/phantom.png");

    public PhantomServantRenderer(EntityRendererProvider.Context p_174338_) {
        super(p_174338_, new PhantomServantModel<>(p_174338_.bakeLayer(ModelLayers.PHANTOM)), 0.75F);
        this.addLayer(new PhantomEyesLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(PhantomServant p_114482_) {
        if (p_114482_.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.PhantomServantTexture, false)){
            return PHANTOM_LOCATION;
        } else {
            return TEXTURE;
        }
    }

    protected void scale(PhantomServant p_115681_, PoseStack p_115682_, float p_115683_) {
        int i = p_115681_.getPhantomSize();
        float f = 1.0F + 0.15F * (float)i;
        p_115682_.scale(f, f, f);
        p_115682_.translate(0.0F, 1.3125F, 0.1875F);
    }

    protected void setupRotations(PhantomServant p_115685_, PoseStack p_115686_, float p_115687_, float p_115688_, float p_115689_, float p_115690_) {
        super.setupRotations(p_115685_, p_115686_, p_115687_, p_115688_, p_115689_, p_115690_);
        p_115686_.mulPose(Axis.XP.rotationDegrees(p_115685_.getXRot()));
    }

    public static class PhantomEyesLayer<T extends PhantomServant> extends EyesLayer<T, PhantomServantModel<T>> {
        private static final RenderType PHANTOM_EYES = RenderType.eyes(ResourceLocation.parse("textures/entity/phantom_eyes.png"));

        public PhantomEyesLayer(RenderLayerParent<T, PhantomServantModel<T>> p_117342_) {
            super(p_117342_);
        }

        public RenderType renderType() {
            return PHANTOM_EYES;
        }
    }
}