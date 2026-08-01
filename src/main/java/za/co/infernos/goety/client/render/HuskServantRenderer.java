package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.ally.undead.zombie.ZombieServant;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class HuskServantRenderer extends ZombieServantRenderer {
   protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/zombie/husk_servant.png");
   private static final ResourceLocation ZOMBIE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/husk.png");

   public HuskServantRenderer(EntityRendererProvider.Context p_174180_) {
      super(p_174180_, ModelLayers.HUSK, ModelLayers.HUSK_INNER_ARMOR, ModelLayers.HUSK_OUTER_ARMOR);
   }

   protected void scale(ZombieServant p_114907_, PoseStack p_114908_, float p_114909_) {
      float f = 1.0625F;
      p_114908_.scale(1.0625F, 1.0625F, 1.0625F);
      super.scale(p_114907_, p_114908_, p_114909_);
   }

   public ResourceLocation getTextureLocation(ZombieServant p_114905_) {
      if (p_114905_.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.HuskServantTexture, false)){
         return ZOMBIE_LOCATION;
      } else {
         return TEXTURE;
      }
   }
}