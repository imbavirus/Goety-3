package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.projectiles.IllBomb;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class IllBombRenderer extends ExplosiveProjectileRenderer<IllBomb> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID,"textures/item/ill_bomb.png");

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