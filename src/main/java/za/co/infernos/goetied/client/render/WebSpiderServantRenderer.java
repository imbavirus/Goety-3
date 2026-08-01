package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.model.WebSpiderModel;
import za.co.infernos.goetied.common.entities.ally.spider.WebSpiderServant;
import za.co.infernos.goetied.config.MobsConfig;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class WebSpiderServantRenderer<T extends WebSpiderServant> extends MobRenderer<T, WebSpiderModel<T>> {
   private static final ResourceLocation ORIGINAL = Goetied.location("textures/entity/servants/spider/web_spider.png");
   private static final ResourceLocation SPIDER_LOCATION = Goetied.location("textures/entity/servants/spider/web_spider_servant.png");

   public WebSpiderServantRenderer(EntityRendererProvider.Context p_173946_) {
      super(p_173946_, new WebSpiderModel<>(p_173946_.bakeLayer(ModModelLayer.WEB_SPIDER)), 0.8F);
      this.addLayer(new SpiderEyesLayer<>(this));
   }

   protected float getFlipDegrees(T p_116011_) {
      return 180.0F;
   }

   public ResourceLocation getTextureLocation(WebSpiderServant p_113972_) {
      if (p_113972_.isHostile() || !za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MobsConfig.WebSpiderServantTexture, false)){
         return ORIGINAL;
      } else {
         return SPIDER_LOCATION;
      }
   }

   public static class SpiderEyesLayer<T extends Entity, M extends WebSpiderModel<T>> extends EyesLayer<T, M> {
      private static final RenderType SPIDER_EYES = RenderType.eyes(Goetied.location("textures/entity/servants/spider/spider_servant_eyes.png"));

      public SpiderEyesLayer(RenderLayerParent<T, M> p_117507_) {
         super(p_117507_);
      }

      public RenderType renderType() {
         return SPIDER_EYES;
      }
   }
}