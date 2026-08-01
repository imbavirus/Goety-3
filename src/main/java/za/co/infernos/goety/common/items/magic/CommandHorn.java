package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.api.entities.IOwned;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class CommandHorn extends Item {
    public static String MODE = "Mode";
    public static String FOLLOW = "Follow";
    public static String WANDER = "Wander";
    public static String STAND_BY = "Stand_By";
    public static String GUARD = "Guard";
    public static String NONE = "None";
    public static float RANGE = 8.0F;

    public CommandHorn() {
        super(new Item.Properties().stacksTo(1));
    }

    public static void switchMode(ItemStack itemStack, Player player) {
        String mode = getMode(itemStack);
        if (NONE.equals(mode)) {
            setFollow(itemStack);
        } else if (FOLLOW.equals(mode)) {
            setWander(itemStack);
        } else if (WANDER.equals(mode)) {
            setStandBy(itemStack);
        } else if (STAND_BY.equals(mode)) {
            setGuard(itemStack);
        } else {
            setNone(itemStack);
        }
        if (player != null) {
            player.displayClientMessage(Component.literal("Command Horn: " + getMode(itemStack)), true);
        }
    }

    public static boolean isNone(ItemStack itemStack) {
        return NONE.equals(getMode(itemStack));
    }

    public static boolean isWander(ItemStack itemStack) {
        return WANDER.equals(getMode(itemStack));
    }

    public static boolean isStandBy(ItemStack itemStack) {
        return STAND_BY.equals(getMode(itemStack));
    }

    public static boolean isGuard(ItemStack itemStack) {
        return GUARD.equals(getMode(itemStack));
    }

    public static boolean isFollow(ItemStack itemStack) {
        return FOLLOW.equals(getMode(itemStack));
    }

    public static String getMode(ItemStack itemStack) {
        String mode = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(MODE);
        if (FOLLOW.equals(mode) || WANDER.equals(mode) || STAND_BY.equals(mode) || GUARD.equals(mode) || NONE.equals(mode)) {
            return mode;
        }
        return NONE;
    }

    public static void setNone(ItemStack itemStack) { setMode(itemStack, NONE); }
    public static void setFollow(ItemStack itemStack) { setMode(itemStack, FOLLOW); }
    public static void setWander(ItemStack itemStack) { setMode(itemStack, WANDER); }
    public static void setStandBy(ItemStack itemStack) { setMode(itemStack, STAND_BY); }
    public static void setGuard(ItemStack itemStack) { setMode(itemStack, GUARD); }

    public static void setMode(ItemStack itemStack, String mode) {
        String safe = (mode == null || mode.isBlank()) ? NONE : mode;
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.putString(MODE, safe));
    }

    public static List<LivingEntity> getEntities(Level level, Player player) {
        List<LivingEntity> result = new ArrayList<>();
        if (level == null || player == null) {
            return result;
        }
        AABB area = player.getBoundingBox().inflate(RANGE);
        for (Entity entity : level.getEntities(player, area, e -> e instanceof LivingEntity)) {
            if (entity instanceof LivingEntity living && entity instanceof IOwned owned && player.equals(owned.getTrueOwner())) {
                result.add(living);
            }
        }
        return result;
    }
}
