package za.co.infernos.goety.common.items.revive;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ReviveServantItem extends Item {
    private static final String TAG_ENTITY_TYPE = "entity_type";
    private static final String TAG_OWNER_NAME = "owner_name";

    public ReviveServantItem(Properties properties) {
        super(properties);
    }

    public static void setSummon(Entity entity, ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(TAG_ENTITY_TYPE, entity.getType().toShortString()));
    }

    public static Entity getSummon(ItemStack stack, Level level) {
        return null;
    }

    public static void setOwnerName(@Nullable LivingEntity entity, ItemStack stack) {
        if (entity == null) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(TAG_OWNER_NAME, entity.getDisplayName().getString()));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        String owner = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(TAG_OWNER_NAME);
        if (!owner.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.goety.arcaPlayer").append(Component.literal(owner)).withStyle(ChatFormatting.GRAY));
        }
    }
}
