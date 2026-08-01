package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.entities.projectiles.BlastFungus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BlastFungusRenderer extends AbstractFungusRenderer<BlastFungus> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID,"textures/entity/projectiles/blast_fungus.png");

    public BlastFungusRenderer(EntityRendererProvider.Context p_174426_) {
        super(p_174426_);
    }

    @Override
    public ResourceLocation getTextureLocation(BlastFungus p_114482_) {
        return TEXTURE;
    }
}