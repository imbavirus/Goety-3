package za.co.infernos.goety.client.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class AoEParticleOption implements ParticleOptions {
   public static final MapCodec<AoEParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
           com.mojang.serialization.Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
           com.mojang.serialization.Codec.FLOAT.fieldOf("growing").forGetter(d -> d.growing),
           com.mojang.serialization.Codec.FLOAT.fieldOf("maxSize").forGetter(d -> d.maxSize),
           com.mojang.serialization.Codec.INT.fieldOf("life").forGetter(d -> d.life)
   ).apply(instance, AoEParticleOption::new));
   public static final StreamCodec<RegistryFriendlyByteBuf, AoEParticleOption> STREAM_CODEC = StreamCodec.of(
           (buf, value) -> {
              buf.writeFloat(value.size);
              buf.writeFloat(value.growing);
              buf.writeFloat(value.maxSize);
              buf.writeInt(value.life);
           },
           buf -> new AoEParticleOption(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt())
   );
   private final float size;
   private final float growing;
   private final float maxSize;
   private final int life;

   public AoEParticleOption(float size, int life) {
      this.size = size;
      this.growing = 0.0F;
      this.maxSize = size;
      this.life = life;
   }

   public AoEParticleOption(float initialSize, float growing, float maxSize, int life) {
      this.size = initialSize;
      this.growing = growing;
      this.maxSize = maxSize;
      this.life = life;
   }

   @NotNull
   @Override
   public ParticleType<AoEParticleOption> getType() {
      return ModParticleTypes.AOE_INDICATOR.get();
   }

   public float getSize(){
      return this.size;
   }

   public float getGrowing(){
      return this.growing;
   }

   public float getMaxSize(){
      return this.maxSize;
   }

   public int getLife(){
      return this.life;
   }
}