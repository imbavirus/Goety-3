package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import net.minecraft.core.particles.ParticleType;

public class PortalShockwaveParticleOption extends ShockwaveParticleOption {

   public PortalShockwaveParticleOption() {
      super(new ColorUtil(0x7317d2));
   }

   public ParticleType<ShockwaveParticleOption> getType() {
      return ModParticleTypes.SHOCKWAVE.get();
   }
}