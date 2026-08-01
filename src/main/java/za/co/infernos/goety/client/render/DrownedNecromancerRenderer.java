package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.DrownedNecromancerModel;
import za.co.infernos.goety.common.entities.neutral.DrownedNecromancer;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DrownedNecromancerRenderer extends MobRenderer<DrownedNecromancer, DrownedNecromancerModel<DrownedNecromancer>> {
   private static final ResourceLocation SKELETON_LOCATION = Goety.location("textures/entity/necromancer/drowned_necromancer.png");
   private static final ResourceLocation SERVANT_LOCATION = Goety.location("textures/entity/necromancer/drowned_necromancer_servant.png");

   public DrownedNecromancerRenderer(EntityRendererProvider.Context p_174382_) {
      super(p_174382_, new DrownedNecromancerModel<>(p_174382_.bakeLayer(ModModelLayer.DROWNED_NECROMANCER)), 0.5F);
      this.addLayer(new NecromancerEyesLayer<>(this));
   }

   protected void scale(DrownedNecromancer necromancer, PoseStack matrixStackIn, float partialTickTime) {
      float original = 1.45F;
      float f1 = (float)necromancer.getNecroLevel();
      float size = original + Math.max(f1 * 0.15F, 0);
      matrixStackIn.scale(size, size, size);
   }

   public ResourceLocation getTextureLocation(DrownedNecromancer p_115941_) {
      if (p_115941_.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.NecromancerServantTexture, false)){
         return SKELETON_LOCATION;
      } else {
         return SERVANT_LOCATION;
      }
   }

   protected void setupRotations(DrownedNecromancer p_114109_, PoseStack p_114110_, float p_114111_, float p_114112_, float p_114113_, float p_114114_) {
      super.setupRotations(p_114109_, p_114110_, p_114111_, p_114112_, p_114113_, p_114114_);
      float f = p_114109_.getSwimAmount(p_114113_);
      if (f > 0.0F) {
         p_114110_.mulPose(Axis.XP.rotationDegrees(Mth.lerp(f, p_114109_.getXRot(), -10.0F - p_114109_.getXRot())));
      }

   }

   public static class NecromancerEyesLayer<T extends DrownedNecromancer, M extends DrownedNecromancerModel<T>> extends EyesLayer<T, M> {
      private static final ResourceLocation GLOW = Goety.location("textures/entity/necromancer/drowned_necromancer_glow.png");

      public NecromancerEyesLayer(RenderLayerParent<T, M> p_i50919_1_) {
         super(p_i50919_1_);
      }

      @Override
      public RenderType renderType() {
         return RenderType.eyes(GLOW);
      }
   }

}
