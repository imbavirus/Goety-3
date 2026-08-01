package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.MaverickModel;
import za.co.infernos.goety.client.render.model.VillagerArmorModel;
import za.co.infernos.goety.common.entities.hostile.cultists.Maverick;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class MaverickRenderer extends CultistRenderer<Maverick>{
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/maverick.png");

    public MaverickRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new MaverickModel<>(renderManagerIn.bakeLayer(ModModelLayer.MAVERICK)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new VillagerArmorModel<>(renderManagerIn.bakeLayer(ModModelLayer.VILLAGER_ARMOR_INNER)), new VillagerArmorModel<>(renderManagerIn.bakeLayer(ModModelLayer.VILLAGER_ARMOR_OUTER)), renderManagerIn.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(Maverick entity) {
        return TEXTURE;
    }
}