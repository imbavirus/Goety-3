package za.co.infernos.goety.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBase extends Item {
    public ItemBase() {
        super(new Properties());
    }

    @Override

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
            TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        ChatFormatting main = ChatFormatting.DARK_PURPLE;
        ChatFormatting secondary = ChatFormatting.BLUE;

        if (stack.getItem() instanceof ItemBase) {
            if (stack.is(ModItems.OMINOUS_SADDLE.get())) {
                tooltip.add(Component.translatable("info.goety.ominous_saddle").withStyle(main));
            }
            if (stack.is(ModItems.VOID_KEY.get())) {
                tooltip.add(Component.translatable("info.goety.void_key").withStyle(main));
            }
            if (stack.is(ModItems.VOID_SHARD.get())) {
                tooltip.add(Component.translatable("info.goety.void_shard").withStyle(main));
            }
        }
    }
}