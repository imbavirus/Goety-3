package za.co.infernos.goety.client.render;

import za.co.infernos.goety.client.render.model.ZombieServantModel;
import za.co.infernos.goety.common.entities.ally.undead.zombie.ZombieServant;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ZombieServantRenderer extends AbstractZombieServantRenderer<ZombieServant, ZombieServantModel<ZombieServant>> {
   public ZombieServantRenderer(EntityRendererProvider.Context p_174456_) {
      this(p_174456_, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
   }

   public ZombieServantRenderer(EntityRendererProvider.Context p_174458_, ModelLayerLocation p_174459_, ModelLayerLocation p_174460_, ModelLayerLocation p_174461_) {
      super(p_174458_, new ZombieServantModel<>(p_174458_.bakeLayer(p_174459_)), new ZombieServantModel<>(p_174458_.bakeLayer(p_174460_)), new ZombieServantModel<>(p_174458_.bakeLayer(p_174461_)));
   }
}