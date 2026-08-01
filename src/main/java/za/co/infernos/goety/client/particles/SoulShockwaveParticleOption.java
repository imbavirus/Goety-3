package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import net.minecraft.core.particles.ParticleType;

public class SoulShockwaveParticleOption extends ShockwaveParticleOption {

   public SoulShockwaveParticleOption() {
      super(new ColorUtil(0x2ac9cf));
   }

   public ParticleType<ShockwaveParticleOption> getType() {
      return ModParticleTypes.SHOCKWAVE.get();
   }
}