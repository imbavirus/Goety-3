package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.utils.ColorUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ShockwaveParticleOption implements ParticleOptions {
   public static final MapCodec<ShockwaveParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
           com.mojang.serialization.Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
           com.mojang.serialization.Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
           com.mojang.serialization.Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
           com.mojang.serialization.Codec.FLOAT.fieldOf("originSize").forGetter(d -> d.originSize),
           com.mojang.serialization.Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
           com.mojang.serialization.Codec.INT.fieldOf("speed").forGetter(d -> d.speed),
           com.mojang.serialization.Codec.INT.fieldOf("life").forGetter(d -> d.life),
           com.mojang.serialization.Codec.BOOL.fieldOf("fade").forGetter(d -> d.fade)
   ).apply(instance, ShockwaveParticleOption::new));
   public static final StreamCodec<RegistryFriendlyByteBuf, ShockwaveParticleOption> STREAM_CODEC = StreamCodec.of(
           (buf, value) -> {
              buf.writeFloat(value.red);
              buf.writeFloat(value.green);
              buf.writeFloat(value.blue);
              buf.writeFloat(value.originSize);
              buf.writeFloat(value.size);
              buf.writeInt(value.speed);
              buf.writeInt(value.life);
              buf.writeBoolean(value.fade);
           },
           buf -> new ShockwaveParticleOption(
                   buf.readFloat(), buf.readFloat(), buf.readFloat(),
                   buf.readFloat(), buf.readFloat(),
                   buf.readInt(), buf.readInt(),
                   buf.readBoolean()
           )
   );
   private final float red;
   private final float green;
   private final float blue;
   private final float originSize;
   private final float size;
   private final int speed;
   private final int life;
   private final boolean fade;

   public ShockwaveParticleOption() {
      this.red = 1.0F;
      this.green = 1.0F;
      this.blue = 1.0F;
      this.originSize = 20;
      this.size = 10;
      this.speed = 0;
      this.life = 30;
      this.fade = true;
   }

   public ShockwaveParticleOption(float size, int speed) {
      this.red = 1.0F;
      this.green = 1.0F;
      this.blue = 1.0F;
      this.originSize = size * 2;
      this.size = size;
      this.speed = speed;
      this.life = 30;
      this.fade = true;
   }

   public ShockwaveParticleOption(float originSize, float size, int speed) {
      this.red = 1.0F;
      this.green = 1.0F;
      this.blue = 1.0F;
      this.originSize = originSize;
      this.size = size;
      this.speed = speed;
      this.life = 30;
      this.fade = true;
   }

   public ShockwaveParticleOption(ColorUtil colorUtil) {
      this.red = colorUtil.red;
      this.green = colorUtil.green;
      this.blue = colorUtil.blue;
      this.originSize = 20;
      this.size = 10;
      this.speed = 0;
      this.life = 30;
      this.fade = true;
   }

   public ShockwaveParticleOption(float r, float g, float b) {
      this.red = r;
      this.green = g;
      this.blue = b;
      this.originSize = 20;
      this.size = 10;
      this.speed = 0;
      this.life = 30;
      this.fade = true;
   }

   public ShockwaveParticleOption(float r, float g, float b, float size, int speed, boolean fade) {
      this.red = r;
      this.green = g;
      this.blue = b;
      this.originSize = size * 2.0F;
      this.size = size;
      this.speed = speed;
      this.life = 30;
      this.fade = fade;
   }

   public ShockwaveParticleOption(float r, float g, float b, float originSize, float size, int speed, boolean fade) {
      this.red = r;
      this.green = g;
      this.blue = b;
      this.originSize = originSize;
      this.size = size;
      this.speed = speed;
      this.life = 30;
      this.fade = fade;
   }

   public ShockwaveParticleOption(float r, float g, float b, float originSize, float size, int speed, int life, boolean fade) {
      this.red = r;
      this.green = g;
      this.blue = b;
      this.originSize = originSize;
      this.size = size;
      this.speed = speed;
      this.life = life;
      this.fade = fade;
   }

   public ParticleType<ShockwaveParticleOption> getType() {
      return ModParticleTypes.SHOCKWAVE.get();
   }

   public float getRed() {
      return this.red;
   }

   public float getGreen() {
      return this.green;
   }

   public float getBlue() {
      return this.blue;
   }

   public float getOriginSize(){
      return this.originSize;
   }

   public float getSize(){
      return this.size;
   }

   public int getSpeed(){
      return this.speed;
   }

   public int getLife(){
      return this.life;
   }

   public boolean isFade() {
      return this.fade;
   }
}