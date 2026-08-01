package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import net.minecraft.core.particles.ParticleType;

public class TeleportShockwaveParticleOption extends ShockwaveParticleOption {

   public TeleportShockwaveParticleOption() {
      super(new ColorUtil(0x9a62e7));
   }

   public TeleportShockwaveParticleOption(int originSize, int size, int life) {
      super(new ColorUtil(0x9a62e7).red, new ColorUtil(0x9a62e7).green, new ColorUtil(0x9a62e7).blue, originSize, size, 0, life, true);
   }

   public TeleportShockwaveParticleOption(int life) {
      super(new ColorUtil(0x9a62e7).red, new ColorUtil(0x9a62e7).green, new ColorUtil(0x9a62e7).blue, 20, 10, 0, life, true);
   }

   public ParticleType<ShockwaveParticleOption> getType() {
      return ModParticleTypes.SHOCKWAVE.get();
   }
}