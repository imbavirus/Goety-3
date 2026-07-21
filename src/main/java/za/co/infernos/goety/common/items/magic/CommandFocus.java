package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.common.magic.spells.utility.CommandSpell;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;

public class CommandFocus extends MagicFocus {
    public static final String TAG_ENTITY = "Servant";
    public static final String TAG_ENTITY_CLIENT = "ServantClient";

    public CommandFocus() {
        super(new CommandSpell());
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return hasServant(p_41453_);
    }

    public static boolean hasServant(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(TAG_ENTITY) || tag.contains(TAG_ENTITY_CLIENT);
    }

    public static void setServant(CompoundTag compoundTag, LivingEntity livingEntity) {
        if (compoundTag != null && livingEntity != null) {
            compoundTag.putUUID(TAG_ENTITY, livingEntity.getUUID());
            compoundTag.putInt(TAG_ENTITY_CLIENT, livingEntity.getId());
        }
    }

    public static LivingEntity getServant(ItemStack stack) {
        // Without a Level reference we can't resolve a UUID. Use getServant(Level, stack) on the server.
        return null;
    }

    public static LivingEntity getServant(CompoundTag compoundTag) {
        return null;
    }

    public static LivingEntity getServant(Level level, ItemStack stack) {
        if (level == null) {
            return null;
        }
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains(TAG_ENTITY)) {
            return getServantClient(level, tag);
        }
        if (level instanceof net.minecraft.server.level.ServerLevel sl) {
            net.minecraft.world.entity.Entity entity = sl.getEntity(tag.getUUID(TAG_ENTITY));
            return entity instanceof LivingEntity le ? le : null;
        }
        return getServantClient(level, tag);
    }

    public static LivingEntity getServantClient(Level level, ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return getServantClient(level, tag);
    }

    public static LivingEntity getServantClient(Level level, CompoundTag compoundTag) {
        if (level == null || compoundTag == null || !compoundTag.contains(TAG_ENTITY_CLIENT)) {
            return null;
        }
        return level.getEntity(compoundTag.getInt(TAG_ENTITY_CLIENT)) instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    public static void addCommandText(Level level, ItemStack stack, List<Component> tooltip) {
        LivingEntity servant = getServantClient(level, stack);
        if (servant != null) {
            tooltip.add(Component.translatable("info.goety.focus.target").append(": ").append(servant.getDisplayName()));
        }
    }
}

