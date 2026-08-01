package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.BoundIllagerModel;
import za.co.infernos.goety.client.render.model.VillagerArmorModel;
import za.co.infernos.goety.common.entities.ally.undead.bound.AbstractBoundIllager;
import za.co.infernos.goety.common.entities.ally.undead.bound.BoundEvoker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class BoundEvokerRenderer extends BoundIllagerRenderer<BoundEvoker>{
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/bound_illager/bound_evoker.png");
    protected static final ResourceLocation CASTING = Goety.location("textures/entity/servants/bound_illager/bound_evoker_casting.png");

    public BoundEvokerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BoundIllagerModel<>(renderManagerIn.bakeLayer(ModModelLayer.BOUND_ILLAGER)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new VillagerArmorModel<>(renderManagerIn.bakeLayer(ModModelLayer.VILLAGER_ARMOR_INNER)), new VillagerArmorModel<>(renderManagerIn.bakeLayer(ModModelLayer.VILLAGER_ARMOR_OUTER)), renderManagerIn.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()) {
            public void render(PoseStack p_116352_, MultiBufferSource p_116353_, int p_116354_, BoundEvoker p_116355_, float p_116356_, float p_116357_, float p_116358_, float p_116359_, float p_116360_, float p_116361_) {
                if (p_116355_.getArmPose() != AbstractBoundIllager.BoundArmPose.CROSSED) {
                    super.render(p_116352_, p_116353_, p_116354_, p_116355_, p_116356_, p_116357_, p_116358_, p_116359_, p_116360_, p_116361_);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(BoundEvoker p_114482_) {
        if (p_114482_.isCastingSpell()){
            return CASTING;
        }
        return TEXTURE;
    }
}