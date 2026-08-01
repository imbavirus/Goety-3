package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.SkeletonServantClothingLayer;
import za.co.infernos.goety.common.entities.ally.undead.skeleton.AbstractSkeletonServant;
import za.co.infernos.goety.common.entities.ally.undead.skeleton.MossySkeletonServant;
import za.co.infernos.goety.common.entities.ally.undead.skeleton.StrayServant;
import za.co.infernos.goety.config.MobsConfig;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class SkeletonServantRenderer extends HumanoidMobRenderer<AbstractSkeletonServant, SkeletonModel<AbstractSkeletonServant>> {
   private static final ResourceLocation TEXTURES = Goety.location("textures/entity/servants/skeleton/skeleton_servant.png");
   private static final ResourceLocation SKELETON_LOCATION = ResourceLocation.parse("textures/entity/skeleton/skeleton.png");
   private static final ResourceLocation STRAY = Goety.location("textures/entity/servants/skeleton/stray_servant.png");
   private static final ResourceLocation STRAY_ORIGINAL = ResourceLocation.parse("textures/entity/skeleton/stray.png");
   private static final ResourceLocation MOSSY = Goety.location("textures/entity/servants/skeleton/mossy_skeleton_servant.png");
   private static final ResourceLocation MOSSY_ORIGINAL = Goety.location("textures/entity/servants/skeleton/mossy_skeleton.png");

   public SkeletonServantRenderer(EntityRendererProvider.Context p_174380_) {
      this(p_174380_, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
   }

   public SkeletonServantRenderer(EntityRendererProvider.Context p_174382_, ModelLayerLocation p_174383_, ModelLayerLocation p_174384_, ModelLayerLocation p_174385_) {
      super(p_174382_, new SkeletonModel<>(p_174382_.bakeLayer(p_174383_)), 0.5F);
      this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel<>(p_174382_.bakeLayer(p_174384_)), new SkeletonModel<>(p_174382_.bakeLayer(p_174385_)), p_174382_.getModelManager()));
      this.addLayer(new SkeletonServantClothingLayer<>(this, p_174382_.getModelSet()));
   }

   public ResourceLocation getTextureLocation(AbstractSkeletonServant servant) {
      if (servant instanceof MossySkeletonServant){
         if (servant.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.MossySkeletonServantTexture, false)){
            return MOSSY_ORIGINAL;
         }
         return MOSSY;
      } else if (servant instanceof StrayServant){
         if (servant.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.StrayServantTexture, false)){
            return STRAY_ORIGINAL;
         }
         return STRAY;
      } else if (servant.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.SkeletonServantTexture, false)){
         return SKELETON_LOCATION;
      } else {
         return TEXTURES;
      }
   }

   protected boolean isShaking(AbstractSkeletonServant p_174389_) {
      return p_174389_.isShaking();
   }
}