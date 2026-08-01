package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.entities.projectiles.NetherMeteor;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NetherMeteorRenderer extends ExplosiveProjectileRenderer<NetherMeteor> {
    private static final ResourceLocation TEXTURE = Goetied.location("textures/entity/projectiles/nether_meteor.png");

    public NetherMeteorRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public ResourceLocation getTextureLocation(NetherMeteor entity) {
        return TEXTURE;
    }
}