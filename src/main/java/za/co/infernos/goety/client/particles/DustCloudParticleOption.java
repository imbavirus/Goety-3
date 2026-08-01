package za.co.infernos.goety.client.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector3f;

public record DustCloudParticleOption(Vector3f color, float scale) implements net.minecraft.core.particles.ParticleOptions {
   public static final MapCodec<DustCloudParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
           com.mojang.serialization.Codec.FLOAT.fieldOf("r").forGetter(o -> o.color.x()),
           com.mojang.serialization.Codec.FLOAT.fieldOf("g").forGetter(o -> o.color.y()),
           com.mojang.serialization.Codec.FLOAT.fieldOf("b").forGetter(o -> o.color.z()),
           com.mojang.serialization.Codec.FLOAT.fieldOf("scale").forGetter(DustCloudParticleOption::scale)
   ).apply(instance, (r, g, b, scale) -> new DustCloudParticleOption(new Vector3f(r, g, b), scale)));

   public static final StreamCodec<RegistryFriendlyByteBuf, DustCloudParticleOption> STREAM_CODEC = StreamCodec.of(
           (buf, value) -> {
              buf.writeFloat(value.color().x());
              buf.writeFloat(value.color().y());
              buf.writeFloat(value.color().z());
              buf.writeFloat(value.scale());
           },
           buf -> new DustCloudParticleOption(new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat()), buf.readFloat())
   );

   public ParticleType<DustCloudParticleOption> getType() {
      return ModParticleTypes.DUST_CLOUD.get();
   }
}