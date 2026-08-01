package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.projectiles.NetherMeteor;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NetherMeteorRenderer extends ExplosiveProjectileRenderer<NetherMeteor> {
    private static final ResourceLocation TEXTURE = Goety.location("textures/entity/projectiles/nether_meteor.png");

    public NetherMeteorRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public ResourceLocation getTextureLocation(NetherMeteor entity) {
        return TEXTURE;
    }
}