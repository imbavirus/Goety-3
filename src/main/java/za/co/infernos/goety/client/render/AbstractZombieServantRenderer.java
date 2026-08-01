package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.ZombieServantModel;
import za.co.infernos.goety.common.entities.ally.undead.zombie.ZombieServant;
import za.co.infernos.goety.config.MobsConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractZombieServantRenderer<T extends ZombieServant, M extends ZombieServantModel<T>> extends HumanoidMobRenderer<T, M> {
   protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/zombie/zombie_servant.png");
   private static final ResourceLocation ZOMBIE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/zombie.png");

   protected AbstractZombieServantRenderer(EntityRendererProvider.Context p_173910_, M p_173911_, M p_173912_, M p_173913_) {
      super(p_173910_, p_173911_, 0.5F);
      this.addLayer(new HumanoidArmorLayer<>(this, p_173912_, p_173913_, p_173910_.getModelManager()));
   }

   public ResourceLocation getTextureLocation(ZombieServant p_113771_) {
      if (p_113771_.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.ZombieServantTexture, false)){
         return ZOMBIE_LOCATION;
      } else {
         return TEXTURE;
      }
   }

   protected boolean isShaking(T p_113773_) {
      return super.isShaking(p_113773_) || p_113773_.isUnderWaterConverting();
   }
}