package za.co.infernos.goety.common.items.magic;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import za.co.infernos.goety.common.magic.spells.utility.CommandSpell;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderFocus extends MagicFocus {
    public static String SERVANT_LIST = "servantList";
    public static String SERVANT_CLIENT_LIST = "servantClientList";

    public OrderFocus() {
        super(new CommandSpell());
    }

    public static List<LivingEntity> getServants(ItemStack stack) {
        // Without a Level reference we cannot resolve UUIDs to entities.
        // Callers that need actual entities should use getServants(Level, stack).
        return new ArrayList<>();
    }

    public static List<LivingEntity> getServants(Level level, ItemStack stack) {
        List<LivingEntity> result = new ArrayList<>();
        if (level == null) {
            return result;
        }
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains(SERVANT_LIST)) {
            return result;
        }
        ListTag list = tag.getList(SERVANT_LIST, 11);
        for (int i = 0; i < list.size(); i++) {
            UUID uuid = NbtUtils.loadUUID(list.get(i));
            if (level instanceof ServerLevel sl) {
                Entity entity = sl.getEntity(uuid);
                if (entity instanceof LivingEntity le) {
                    result.add(le);
                }
            }
        }
        return result;
    }

    public static List<LivingEntity> getServantsClient(Level level, ItemStack stack) {
        List<LivingEntity> result = new ArrayList<>();
        if (level == null) {
            return result;
        }
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains(SERVANT_CLIENT_LIST)) {
            return result;
        }
        int[] ids = tag.getIntArray(SERVANT_CLIENT_LIST);
        for (int id : ids) {
            Entity entity = level.getEntity(id);
            if (entity instanceof LivingEntity le) {
                result.add(le);
            }
        }
        return result;
    }

    public static void setServants(ItemStack stack, Player player, LivingEntity entity) {
        if (stack == null || entity == null) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            ListTag uuidList = tag.contains(SERVANT_LIST) ? tag.getList(SERVANT_LIST, 11) : new ListTag();
            uuidList.add(NbtUtils.createUUID(entity.getUUID()));
            tag.put(SERVANT_LIST, uuidList);

            int[] existing = tag.contains(SERVANT_CLIENT_LIST) ? tag.getIntArray(SERVANT_CLIENT_LIST) : new int[0];
            int[] updated = new int[existing.length + 1];
            System.arraycopy(existing, 0, updated, 0, existing.length);
            updated[existing.length] = entity.getId();
            tag.putIntArray(SERVANT_CLIENT_LIST, updated);
        });
    }

    public static void clearServants(ItemStack stack) {
        if (stack == null) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.remove(SERVANT_LIST);
            tag.remove(SERVANT_CLIENT_LIST);
        });
    }
}
