package za.co.infernos.goetied.common.items.magic;

import za.co.infernos.goetied.api.entities.ally.IServant;
import za.co.infernos.goetied.common.events.ArcaTeleporter;
import za.co.infernos.goetied.common.magic.spells.void_spells.TroopSpell;
import za.co.infernos.goetied.common.network.ModNetwork;
import za.co.infernos.goetied.common.network.server.SPlayWorldSoundPacket;
import za.co.infernos.goetied.utils.BlockFinder;
import za.co.infernos.goetied.utils.MobUtil;
import za.co.infernos.goetied.utils.SEHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

import java.util.ArrayList;
import java.util.List;

public class TroopFocus extends MagicFocus {
    public static final String TAG_SUMMON_TYPE = "TroopSummonType";

    public TroopFocus() {
        super(new TroopSpell());
    }

    public static boolean hasSummonType(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(TAG_SUMMON_TYPE);
    }

    public static void setSummonType(CompoundTag compoundTag, EntityType<?> entityType) {
        if (compoundTag != null && entityType != null) {
            ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            if (key != null) {
                compoundTag.putString(TAG_SUMMON_TYPE, key.toString());
            }
        }
    }

    public static void setSummonType(ItemStack stack, EntityType<?> entityType) {
        if (stack != null && entityType != null) {
            ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            if (key != null) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(TAG_SUMMON_TYPE, key.toString()));
            }
        }
    }

    public static EntityType<?> getSummonType(ItemStack stack) {
        return getSummonType(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag());
    }

    public static EntityType<?> getSummonType(CompoundTag tag) {
        if (tag == null || !tag.contains(TAG_SUMMON_TYPE)) {
            return null;
        }
        ResourceLocation loc = ResourceLocation.tryParse(tag.getString(TAG_SUMMON_TYPE));
        return loc == null ? null : BuiltInRegistries.ENTITY_TYPE.get(loc);
    }

    public static void call(ServerPlayer player, ItemStack stack) {
        if (!(player.level() instanceof ServerLevel serverLevel) || !hasSummonType(stack)) {
            return;
        }
        EntityType<?> entityType = getSummonType(stack);
        if (entityType == null) {
            return;
        }
        List<LivingEntity> list = new ArrayList<>();
        for (Entity entity : serverLevel.getAllEntities()) {
            if (entity instanceof LivingEntity living && entity.getType() == entityType) {
                if (MobUtil.getOwner(living) == player && !SEHelper.getGroundedEntities(player).contains(living)) {
                    list.add(living);
                }
            }
        }
        if (list.isEmpty()) {
            return;
        }
        for (LivingEntity livingEntity1 : list) {
            LivingEntity original = null;
            if (livingEntity1.isPassenger() && livingEntity1.getVehicle() instanceof LivingEntity vehicle) {
                original = livingEntity1;
                livingEntity1 = vehicle;
            }
            if (livingEntity1.isDeadOrDying()) {
                continue;
            }
            BlockPos blockPos = BlockFinder.SummonRadius(player.blockPosition(), livingEntity1, serverLevel);
            if ((player.isShiftKeyDown() || player.isCrouching()) && list.size() == 1) {
                blockPos = player.blockPosition();
            }
            if (livingEntity1.level().dimension() == player.level().dimension()) {
                EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(
                        livingEntity1, blockPos.getX(), blockPos.getY(), blockPos.getZ());
                NeoForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    break;
                }
                livingEntity1.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                MobUtil.moveDownToGround(livingEntity1);
                ModNetwork.sendToALL(new SPlayWorldSoundPacket(player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F));
                ModNetwork.sendToALL(new SPlayWorldSoundPacket(blockPos, SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F));
                setFollowing(original);
                setFollowing(livingEntity1);
            } else if (player.getServer() != null) {
                ServerLevel dest = player.getServer().getLevel(player.level().dimension());
                if (dest == null) {
                    continue;
                }
                Vec3 vec3 = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(
                        livingEntity1, blockPos.getX(), blockPos.getY(), blockPos.getZ());
                NeoForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    break;
                }
                livingEntity1.changeDimension(ArcaTeleporter.transition(dest, livingEntity1, vec3));
                livingEntity1.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                MobUtil.moveDownToGround(livingEntity1);
                ModNetwork.sendToALL(new SPlayWorldSoundPacket(player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F));
                setFollowing(original);
                setFollowing(livingEntity1);
            }
        }
    }

    private static void setFollowing(LivingEntity entity) {
        if (entity instanceof IServant servant) {
            servant.setFollowing();
        }
    }

    public static void addCallText(ItemStack stack, List<Component> tooltip) {
        if (!hasSummonType(stack)) {
            tooltip.add(Component.translatable("info.goetied.focus.noSummonType").withStyle(ChatFormatting.GRAY));
            return;
        }
        EntityType<?> entityType = getSummonType(stack);
        if (entityType != null) {
            tooltip.add(Component.translatable("info.goetied.focus.summonType").append(" ")
                    .append(entityType.getDescription())
                    .withStyle(ChatFormatting.DARK_GREEN));
        }
    }
}
