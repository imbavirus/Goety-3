package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class NecromancerRenderer extends AbstractNecromancerRenderer{
    public NecromancerRenderer(EntityRendererProvider.Context p_174380_) {
        super(p_174380_);
        this.addLayer(new NecromancerEyesLayer<>(this, Goetied.location("textures/entity/necromancer/necromancer_glow.png")));
    }
}