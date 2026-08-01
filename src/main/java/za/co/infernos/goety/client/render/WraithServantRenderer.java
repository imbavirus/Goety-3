package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.WraithBandsLayer;
import za.co.infernos.goety.client.render.layer.WraithGlowLayer;
import za.co.infernos.goety.client.render.layer.WraithSecretLayer;
import za.co.infernos.goety.client.render.model.WraithModel;
import za.co.infernos.goety.common.entities.neutral.AbstractWraith;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WraithServantRenderer<T extends AbstractWraith> extends AbstractWraithRenderer<T> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/wraith/wraith.png");

    public WraithServantRenderer(EntityRendererProvider.Context renderManagerIn){
        super(renderManagerIn, new WraithModel<>(renderManagerIn.bakeLayer(ModModelLayer.WRAITH)), 0.5F);
        this.addLayer(new WraithGlowLayer<>(this));
        this.addLayer(new WraithBandsLayer<>(this, renderManagerIn.getModelSet()));
        this.addLayer(new WraithSecretLayer<>(this, renderManagerIn.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }

}
