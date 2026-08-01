package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.neutral.AbstractNecromancer;
import za.co.infernos.goety.config.MobsConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MossyNecromancerRenderer extends AbstractNecromancerRenderer{
    private static final ResourceLocation SKELETON_LOCATION = Goety.location("textures/entity/necromancer/mossy_necromancer.png");
    private static final ResourceLocation SERVANT_LOCATION = Goety.location("textures/entity/necromancer/mossy_necromancer_servant.png");

    public MossyNecromancerRenderer(EntityRendererProvider.Context p_174380_) {
        super(p_174380_);
        this.addLayer(new NecromancerEyesLayer<>(this, Goety.location("textures/entity/necromancer/mossy_necromancer_glow.png")));
    }

    public ResourceLocation getTextureLocation(AbstractNecromancer p_115941_) {
        if (p_115941_.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.NecromancerServantTexture, false)){
            return SKELETON_LOCATION;
        } else {
            return SERVANT_LOCATION;
        }
    }
}