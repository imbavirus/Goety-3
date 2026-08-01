package za.co.infernos.goetied.client.particles;

import za.co.infernos.goetied.utils.ColorUtil;
import net.minecraft.core.particles.ParticleType;

public class SoulShockwaveParticleOption extends ShockwaveParticleOption {

   public SoulShockwaveParticleOption() {
      super(new ColorUtil(0x2ac9cf));
   }

   public ParticleType<ShockwaveParticleOption> getType() {
      return ModParticleTypes.SHOCKWAVE.get();
   }
}