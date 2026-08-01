package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goety.client.render.model.MountaineerModel;
import za.co.infernos.goety.common.entities.ally.illager.MountaineerServant;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class MountaineerServantRenderer<T extends MountaineerServant> extends MobRenderer<T, MountaineerModel<T>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/illager/mountaineer.png");
    protected static final ResourceLocation ORIGINAL = Goety.location("textures/entity/servants/illager/mountaineer_original.png");

    public MountaineerServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new MountaineerModel<>(renderManagerIn.bakeLayer(ModModelLayer.MOUNTAINEER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.MountaineerServantTexture, false)){
            return ORIGINAL;
        } else {
            return TEXTURE;
        }
    }
}