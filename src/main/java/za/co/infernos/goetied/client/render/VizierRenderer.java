package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.layer.VizierAuraLayer;
import za.co.infernos.goetied.client.render.layer.VizierCapeLayer;
import za.co.infernos.goetied.client.render.model.VizierModel;
import za.co.infernos.goetied.common.entities.boss.Vizier;
import za.co.infernos.goetied.config.MobsConfig;
import za.co.infernos.goetied.utils.HolidayUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class VizierRenderer extends MobRenderer<Vizier, VizierModel> {
    protected static final ResourceLocation TEXTURE = Goetied.location("textures/entity/illagers/vizier.png");
    protected static final ResourceLocation CHRISTMAS = Goetied.location("textures/entity/illagers/vizier_christmas.png");

    public VizierRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new VizierModel(renderManagerIn.bakeLayer(ModModelLayer.VIZIER)), 0.5F);
        this.addLayer(new VizierAuraLayer(this, renderManagerIn.getModelSet()));
        this.addLayer(new VizierCapeLayer(this));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()) {
            public void render(PoseStack p_116352_, MultiBufferSource p_116353_, int p_116354_, Vizier p_116355_, float p_116356_, float p_116357_, float p_116358_, float p_116359_, float p_116360_, float p_116361_) {
                if (p_116355_.isCharging()) {
                    super.render(p_116352_, p_116353_, p_116354_, p_116355_, p_116356_, p_116357_, p_116358_, p_116359_, p_116360_, p_116361_);
                }

            }
        });
    }

    @Override
    protected int getBlockLightLevel(Vizier p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(Vizier entity) {
        return HolidayUtil.isChristmasMonth() && za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MobsConfig.HolidaySkins, false) ? CHRISTMAS : TEXTURE;
    }
}