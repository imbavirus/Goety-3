package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.PlayerZombieModel;
import za.co.infernos.goety.common.entities.ally.undead.zombie.FrozenZombieServant;
import za.co.infernos.goety.config.MobsConfig;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class FrozenZombieRenderer extends HumanoidMobRenderer<FrozenZombieServant, PlayerZombieModel<FrozenZombieServant>> {
   protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/zombie/frozen_zombie_servant.png");
   private static final ResourceLocation ZOMBIE_LOCATION = Goety.location("textures/entity/servants/zombie/frozen_zombie_original.png");

   public FrozenZombieRenderer(EntityRendererProvider.Context entityRendererManager) {
      super(entityRendererManager, new PlayerZombieModel<>(entityRendererManager.bakeLayer(ModelLayers.PLAYER)), 0.5F);
      this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(entityRendererManager.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(entityRendererManager.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), entityRendererManager.getModelManager()));
   }

   public ResourceLocation getTextureLocation(FrozenZombieServant p_113771_) {
      if (p_113771_.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.FrozenZombieServantTexture, false)){
         return ZOMBIE_LOCATION;
      } else {
         return TEXTURE;
      }
   }

   protected boolean isShaking(FrozenZombieServant p_113773_) {
      return super.isShaking(p_113773_) || p_113773_.isUnderWaterConverting();
   }
}