package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.entities.ally.spider.CaveSpiderServant;
import za.co.infernos.goetied.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CaveSpiderServantRenderer extends SpiderServantRenderer<CaveSpiderServant> {
   private static final ResourceLocation CAVE_SPIDER_LOCATION = Goetied.location("textures/entity/servants/spider/cave_spider_servant.png");
   private static final ResourceLocation ORIGINAL = ResourceLocation.parse("textures/entity/spider/cave_spider.png");

   public CaveSpiderServantRenderer(EntityRendererProvider.Context p_173946_) {
      super(p_173946_, ModelLayers.CAVE_SPIDER);
      this.shadowRadius *= 0.7F;
   }

   protected void scale(CaveSpiderServant p_113974_, PoseStack p_113975_, float p_113976_) {
      p_113975_.scale(0.7F, 0.7F, 0.7F);
   }

   public ResourceLocation getTextureLocation(CaveSpiderServant p_113972_) {
      if (p_113972_.isHostile() || !za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MobsConfig.CaveSpiderServantTexture, false)){
         return ORIGINAL;
      } else {
         return CAVE_SPIDER_LOCATION;
      }
   }
}