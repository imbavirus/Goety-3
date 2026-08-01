package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.DrownedServantOuterLayer;
import za.co.infernos.goety.client.render.model.DrownedServantModel;
import za.co.infernos.goety.common.entities.ally.undead.zombie.DrownedServant;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DrownedServantRenderer extends AbstractZombieServantRenderer<DrownedServant, DrownedServantModel<DrownedServant>> {
   protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/zombie/drowned_servant.png");
   private static final ResourceLocation DROWNED_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned.png");

   public DrownedServantRenderer(EntityRendererProvider.Context p_173964_) {
      super(p_173964_, new DrownedServantModel<>(p_173964_.bakeLayer(ModelLayers.DROWNED)), new DrownedServantModel<>(p_173964_.bakeLayer(ModelLayers.DROWNED_INNER_ARMOR)), new DrownedServantModel<>(p_173964_.bakeLayer(ModelLayers.DROWNED_OUTER_ARMOR)));
      this.addLayer(new DrownedServantOuterLayer<>(this, p_173964_.getModelSet()));
   }

   public ResourceLocation getTextureLocation(DrownedServant p_114115_) {
      if (p_114115_.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.DrownedServantTexture, false)){
         return DROWNED_LOCATION;
      } else {
         return TEXTURE;
      }
   }

   protected void setupRotations(DrownedServant p_114109_, PoseStack p_114110_, float p_114111_, float p_114112_, float p_114113_, float p_114114_) {
      super.setupRotations(p_114109_, p_114110_, p_114111_, p_114112_, p_114113_, p_114114_);
      float f = p_114109_.getSwimAmount(p_114113_);
      if (f > 0.0F) {
         p_114110_.mulPose(Axis.XP.rotationDegrees(Mth.lerp(f, p_114109_.getXRot(), -10.0F - p_114109_.getXRot())));
      }

   }
}