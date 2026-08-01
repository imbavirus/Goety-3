package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.TormentorVisageLayer;
import za.co.infernos.goety.client.render.model.TormentorModel;
import za.co.infernos.goety.common.entities.hostile.illagers.Tormentor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class TormentorRenderer extends HumanoidMobRenderer<Tormentor, TormentorModel<Tormentor>> {
    private static final ResourceLocation TEXTURE = Goety.location("textures/entity/illagers/tormentor.png");
    private static final ResourceLocation TEXTURE_CHARGE = Goety.location("textures/entity/illagers/tormentor_charge.png");

    public TormentorRenderer(EntityRendererProvider.Context p_i47190_1_) {
        super(p_i47190_1_, new TormentorModel<>(p_i47190_1_.bakeLayer(ModModelLayer.TORMENTOR)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, p_i47190_1_.getItemInHandRenderer()));
        this.addLayer(new TormentorVisageLayer<>(this));
    }

    protected int getBlockLightLevel(Tormentor pEntity, BlockPos pPos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(Tormentor pEntity) {
        return pEntity.isCharging() ? TEXTURE_CHARGE : TEXTURE;
    }

    protected void scale(Tormentor entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }
}