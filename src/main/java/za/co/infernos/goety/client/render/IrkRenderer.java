package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.IrkModel;
import za.co.infernos.goety.common.entities.neutral.Minion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class IrkRenderer extends MobRenderer<Minion, IrkModel<Minion>> {
    private static final ResourceLocation VEX_TEXTURE = Goety.location("textures/entity/illagers/irk.png");
    private static final ResourceLocation VEX_CHARGING_TEXTURE = Goety.location("textures/entity/illagers/irk_charging.png");

    public IrkRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new IrkModel(renderManagerIn.bakeLayer(ModModelLayer.IRK)), 0.3F);
    }

    protected int getBlockLightLevel(Minion entityIn, BlockPos partialTicks) {
        return 15;
    }

    public ResourceLocation getTextureLocation(Minion entity) {
        return entity.isCharging() ? VEX_CHARGING_TEXTURE : VEX_TEXTURE;
    }
}