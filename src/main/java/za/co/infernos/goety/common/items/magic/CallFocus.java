package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.api.entities.ally.IServant;
import za.co.infernos.goety.common.events.ArcaTeleporter;
import za.co.infernos.goety.common.magic.spells.void_spells.CallSpell;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SPlayWorldSoundPacket;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.EntityFinder;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

import java.util.List;
import java.util.UUID;

public class CallFocus extends MagicFocus {
    public static final String TAG_ENTITY = "Summoned";

    public CallFocus() {
        super(new CallSpell());
    }

    public static void call(ServerPlayer player, ItemStack stack) {
        if (!hasSummon(stack)) {
            return;
        }
        LivingEntity livingEntity = getSummon(stack);
        if (!(player.level() instanceof ServerLevel serverLevel) || livingEntity == null) {
            return;
        }
        LivingEntity original = null;
        if (livingEntity.isPassenger() && livingEntity.getVehicle() instanceof LivingEntity vehicle) {
            original = livingEntity;
            livingEntity = vehicle;
        }
        if (livingEntity.isDeadOrDying()) {
            return;
        }
        BlockPos blockPos = BlockFinder.SummonRadius(player.blockPosition(), livingEntity, serverLevel);
        if (livingEntity.level().dimension() == player.level().dimension()) {
            EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(
                    livingEntity, blockPos.getX(), blockPos.getY(), blockPos.getZ());
            NeoForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                return;
            }
            livingEntity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            MobUtil.moveDownToGround(livingEntity);
            ModNetwork.sendToALL(new SPlayWorldSoundPacket(player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F));
            ModNetwork.sendToALL(new SPlayWorldSoundPacket(blockPos, SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F));
            setFollowing(original);
            setFollowing(livingEntity);
        } else if (player.getServer() != null) {
            ServerLevel dest = player.getServer().getLevel(player.level().dimension());
            if (dest == null) {
                return;
            }
            Vec3 vec3 = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(
                    livingEntity, blockPos.getX(), blockPos.getY(), blockPos.getZ());
            NeoForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                return;
            }
            livingEntity.changeDimension(ArcaTeleporter.transition(dest, livingEntity, vec3));
            livingEntity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            MobUtil.moveDownToGround(livingEntity);
            ModNetwork.sendToALL(new SPlayWorldSoundPacket(player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F));
            setFollowing(original);
            setFollowing(livingEntity);
        }
    }

    private static void setFollowing(LivingEntity entity) {
        if (entity instanceof IServant servant) {
            servant.setFollowing();
        }
    }

    public static boolean hasSummon(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(TAG_ENTITY);
    }

    public static void setSummon(CompoundTag compoundTag, LivingEntity livingEntity) {
        if (compoundTag != null && livingEntity != null) {
            compoundTag.putUUID(TAG_ENTITY, livingEntity.getUUID());
        }
    }

    public static void setSummon(ItemStack stack, LivingEntity livingEntity) {
        if (stack != null && livingEntity != null) {
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putUUID(TAG_ENTITY, livingEntity.getUUID()));
        }
    }

    public static LivingEntity getSummon(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return getSummon(tag);
    }

    public static LivingEntity getSummon(CompoundTag compoundTag) {
        if (compoundTag == null || !compoundTag.contains(TAG_ENTITY)) {
            return null;
        }
        UUID uuid = compoundTag.getUUID(TAG_ENTITY);
        return EntityFinder.getLivingEntityByUuiD(uuid);
    }

    public static LivingEntity getSummon(Level level, ItemStack stack) {
        if (level instanceof ServerLevel) {
            return getSummon(stack);
        }
        return getSummonClient(level, stack);
    }

    public static LivingEntity getSummonClient(Level level, ItemStack stack) {
        if (!hasSummon(stack) || level == null) {
            return null;
        }
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (level instanceof ServerLevel sl) {
            Entity entity = sl.getEntity(tag.getUUID(TAG_ENTITY));
            return entity instanceof LivingEntity le ? le : null;
        }
        return null;
    }

    public static void addCallText(ItemStack stack, List<Component> tooltip) {
        if (!hasSummon(stack)) {
            tooltip.add(Component.translatable("info.goety.focus.noSummon").withStyle(ChatFormatting.GRAY));
            return;
        }
        LivingEntity livingEntity = getSummon(stack);
        if (livingEntity != null) {
            tooltip.add(Component.translatable("info.goety.focus.summon").append(" ")
                    .append(livingEntity.getCustomName() != null ? livingEntity.getCustomName() : livingEntity.getDisplayName())
                    .withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("info.goety.focus.bound").withStyle(ChatFormatting.GRAY));
        }
    }
}
