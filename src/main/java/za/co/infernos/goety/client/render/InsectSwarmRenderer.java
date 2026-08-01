package za.co.infernos.goety.client.render;

import za.co.infernos.goety.common.entities.neutral.InsectSwarm;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class InsectSwarmRenderer extends EntityRenderer<InsectSwarm> {
    public InsectSwarmRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(InsectSwarm pEntity) {
        // Return a dummy texture - entity is rendered via particles only
        return ResourceLocation.parse("textures/entity/empty.png");
    }
}
