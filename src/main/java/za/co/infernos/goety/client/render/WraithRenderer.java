package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.WraithGlowLayer;
import za.co.infernos.goety.client.render.model.WraithModel;
import za.co.infernos.goety.common.entities.neutral.AbstractWraith;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WraithRenderer<T extends AbstractWraith> extends AbstractWraithRenderer<T> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/wraith/wraith.png");

    public WraithRenderer(EntityRendererProvider.Context renderManagerIn){
        super(renderManagerIn, new WraithModel<>(renderManagerIn.bakeLayer(ModModelLayer.WRAITH)), 0.5F);
        this.addLayer(new WraithGlowLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }

}
