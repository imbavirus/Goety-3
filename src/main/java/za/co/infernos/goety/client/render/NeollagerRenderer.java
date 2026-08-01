package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.IllagerServantModel;
import za.co.infernos.goety.common.entities.ally.illager.Neollager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.resources.ResourceLocation;

public class NeollagerRenderer extends MobRenderer<Neollager, IllagerServantModel<Neollager>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/illager/neollager.png");
    protected static final ResourceLocation MAGIC = Goety.location("textures/entity/servants/illager/neollager_magic.png");

    public NeollagerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new IllagerServantModel<>(renderManagerIn.bakeLayer(ModModelLayer.ILLAGER_SERVANT)), 0.5F);
        this.addLayer(new CrossedArmsItemLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    protected void scale(Neollager p_116314_, PoseStack p_116315_, float p_116316_) {
        float f = 0.9375F;
        if (p_116314_.isBaby()) {
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }

        p_116315_.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(Neollager entity) {
        if (entity.isMagic()){
            return MAGIC;
        }
        return TEXTURE;
    }
}