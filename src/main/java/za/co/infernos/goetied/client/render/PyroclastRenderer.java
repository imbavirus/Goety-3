package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.entities.projectiles.Pyroclast;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PyroclastRenderer extends AbstractFungusRenderer<Pyroclast> {
    private static final ResourceLocation TEXTURE = Goetied.location("textures/entity/projectiles/pyroclast.png");

    public PyroclastRenderer(EntityRendererProvider.Context p_174426_) {
        super(p_174426_);
    }

    @Override
    public ResourceLocation getTextureLocation(Pyroclast p_114482_) {
        return TEXTURE;
    }
}