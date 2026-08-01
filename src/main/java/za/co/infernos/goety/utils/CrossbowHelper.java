package za.co.infernos.goety.utils;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class CrossbowHelper {
    public static List<ItemStack> getChargedProjectiles(ItemStack stack) {
        return java.util.Collections.emptyList();
    }

    public static float[] getShotPitches(RandomSource random) {
        boolean flag = random.nextBoolean();
        return new float[] {1.0F, getRandomShotPitch(flag, random), getRandomShotPitch(!flag, random)};
    }

    public static float getRandomShotPitch(boolean high, RandomSource random) {
        float f = high ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    public static void performCustomShooting(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, Projectile projectile, float velocity, float inaccuracy) {
        performCustomShooting(level, shooter, hand, crossbow, projectile, SoundEvents.CROSSBOW_SHOOT, velocity, inaccuracy);
    }

    public static void performCustomShooting(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, Projectile projectile, SoundEvent soundEvent, float velocity, float inaccuracy) {
        shootCustomProjectile(level, shooter, hand, crossbow, projectile, soundEvent, 1.0F, velocity, inaccuracy, 0.0F);
        onCrossbowShot(level, shooter, crossbow);
    }

    public static void onCrossbowShot(Level level, LivingEntity shooter, ItemStack crossbow) {
        if (shooter instanceof ServerPlayer serverPlayer) {
            if (!level.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverPlayer, crossbow);
            }
            serverPlayer.awardStat(Stats.ITEM_USED.get(crossbow.getItem()));
        }
        clearChargedProjectiles(crossbow);
    }

    public static void clearChargedProjectiles(ItemStack stack) {
        // no-op in compatibility path
    }

    public static void shootCustomProjectile(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, Projectile projectile, SoundEvent soundEvent, float pitch, float velocity, float inaccuracy, float spreadYaw) {
        if (!level.isClientSide) {
            Vec3 up = shooter.getUpVector(1.0F);
            Quaternionf rot = (new Quaternionf()).setAngleAxis((double) (spreadYaw * ((float) Math.PI / 180F)), up.x, up.y, up.z);
            Vec3 view = shooter.getViewVector(1.0F);
            Vector3f vec = view.toVector3f().rotate(rot);
            projectile.shoot((double) vec.x(), (double) vec.y(), (double) vec.z(), velocity, inaccuracy);
            crossbow.hurtAndBreak(1, shooter, hand == InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
            level.addFreshEntity(projectile);
            level.playSound((Player) null, shooter.getX(), shooter.getY(), shooter.getZ(), soundEvent, SoundSource.PLAYERS, 1.0F, pitch);
        }
    }
}
