package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class NecromancerRenderer extends AbstractNecromancerRenderer{
    public NecromancerRenderer(EntityRendererProvider.Context p_174380_) {
        super(p_174380_);
        this.addLayer(new NecromancerEyesLayer<>(this, Goety.location("textures/entity/necromancer/necromancer_glow.png")));
    }
}