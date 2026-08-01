package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.entities.projectiles.IllBomb;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class IllBombRenderer extends ExplosiveProjectileRenderer<IllBomb> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID,"textures/item/ill_bomb.png");

    public IllBombRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public float scale(IllBomb illBomb) {
        return 1.0F;
    }

    public ResourceLocation getTextureLocation(IllBomb entity) {
        return TEXTURE;
    }
}