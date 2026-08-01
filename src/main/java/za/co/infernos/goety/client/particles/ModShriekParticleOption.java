package za.co.infernos.goety.client.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ModShriekParticleOption implements ParticleOptions {
   public static final MapCodec<ModShriekParticleOption> CODEC = RecordCodecBuilder.mapCodec((p_235952_) -> {
      return p_235952_.group(Codec.INT.fieldOf("delay").forGetter((p_235954_) -> {
         return p_235954_.delay;
      })).apply(p_235952_, ModShriekParticleOption::new);
   });
   public static final StreamCodec<RegistryFriendlyByteBuf, ModShriekParticleOption> STREAM_CODEC = StreamCodec.of(
         (buf, value) -> buf.writeVarInt(value.delay),
         buf -> new ModShriekParticleOption(buf.readVarInt()));
   private final int delay;

   public ModShriekParticleOption(int p_235949_) {
      this.delay = p_235949_;
   }

   public ParticleType<ModShriekParticleOption> getType() {
      return ModParticleTypes.MOD_SHRIEK.get();
   }

   public int getDelay() {
      return this.delay;
   }
}