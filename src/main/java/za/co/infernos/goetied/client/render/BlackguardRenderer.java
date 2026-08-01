package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.model.BlackguardModel;
import za.co.infernos.goetied.common.entities.ally.undead.zombie.BlackguardServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BlackguardRenderer<T extends BlackguardServant> extends MobRenderer<T, BlackguardModel<T>> {
   private static final ResourceLocation TEXTURE = Goetied.location("textures/entity/servants/zombie/blackguard.png");
   private static final ResourceLocation HOSTILE = Goetied.location("textures/entity/servants/zombie/blackguard_hostile.png");

   public BlackguardRenderer(EntityRendererProvider.Context p_174443_) {
      super(p_174443_, new BlackguardModel<>(p_174443_.bakeLayer(ModModelLayer.BLACKGUARD)), 0.5F);
   }

   public ResourceLocation getTextureLocation(T p_116410_) {
      if (p_116410_.isHostile()){
         return HOSTILE;
      }
      return TEXTURE;
   }
}