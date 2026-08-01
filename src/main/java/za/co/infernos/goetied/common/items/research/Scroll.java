package za.co.infernos.goetied.common.items.research;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.research.Research;
import za.co.infernos.goetied.utils.SEHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class Scroll extends ResearchScroll{
    public Scroll(Properties properties, Research research) {
        super(properties, research);
    }

    public Scroll(Research research) {
        super(research);
    }

    @Override
    public Component researchGet() {
        return Component.translatable("info.goetied.research." + research.getId());
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable("info.goetied.items." + research.getId()).withStyle(ChatFormatting.GOLD));
        if (context.level() != null && context.level().isClientSide){
            net.minecraft.world.entity.player.Player player = Goetied.PROXY.getPlayer();
            if (player != null && SEHelper.hasResearch(player, this.research)){
                tooltip.add(Component.translatable("info.goetied.research.learned").withStyle(ChatFormatting.BLUE));
            } else {
                tooltip.add(Component.translatable("info.goetied.items.scroll").withStyle(ChatFormatting.AQUA));
            }
        }
    }
}
