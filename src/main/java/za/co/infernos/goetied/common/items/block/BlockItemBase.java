package za.co.infernos.goetied.common.items.block;

import za.co.infernos.goetied.common.blocks.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class BlockItemBase extends BlockItem {
    public BlockItemBase(Block blockIn) {
        super(blockIn, new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        ChatFormatting main = ChatFormatting.GOLD;
        ChatFormatting secondary = ChatFormatting.LIGHT_PURPLE;

        if (stack.getItem() instanceof BlockItemBase base) {
            if (base.getBlock() instanceof IceBouquetTrapBlock) {
                tooltip.add(Component.translatable("info.goetied.ice_bouquet_trap").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.ice_bouquet_trap_extra").withStyle(secondary));
            }
            if (base.getBlock() instanceof WindBlowerBlock) {
                tooltip.add(Component.translatable("info.goetied.wind_blower").withStyle(main));
            }
            if (base.getBlock() instanceof SculkDevourerBlock) {
                tooltip.add(Component.translatable("info.goetied.sculk_devourer").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.sculk_devourer_extra").withStyle(secondary));
            }
            if (base.getBlock() instanceof SculkConverterBlock) {
                tooltip.add(Component.translatable("info.goetied.sculk_converter").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.sculk_converter_extra").withStyle(secondary));
            }
            if (base.getBlock() instanceof SculkRelayBlock) {
                tooltip.add(Component.translatable("info.goetied.sculk_relay").withStyle(main));
            }
            if (base.getBlock() instanceof SculkGrowerBlock) {
                tooltip.add(Component.translatable("info.goetied.sculk_grower").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.sculk_grower_extra").withStyle(secondary));
            }
            if (base.getBlock() instanceof ForbiddenGrassBlock) {
                tooltip.add(Component.translatable("info.goetied.forbidden_grass").withStyle(main));
            }
            if (base.getBlock() instanceof HookBellBlock) {
                tooltip.add(Component.translatable("info.goetied.hook_bell").withStyle(main));
            }
            if (base.getBlock() instanceof HauntedGlassBlock glassBlock) {
                if (glassBlock.isPlayerOnly) {
                    if (glassBlock.isTinted) {
                        tooltip.add(Component.translatable("info.goetied.haunted_glass_tinted").withStyle(main));
                    } else {
                        tooltip.add(Component.translatable("info.goetied.haunted_glass").withStyle(main));
                    }
                } else {
                    if (glassBlock.isTinted) {
                        tooltip.add(Component.translatable("info.goetied.haunted_glass_mob_tinted").withStyle(main));
                    } else {
                        tooltip.add(Component.translatable("info.goetied.haunted_glass_mob").withStyle(main));
                    }
                }
            }
            if (base.getBlock() instanceof FreezeLampBlock) {
                tooltip.add(Component.translatable("info.goetied.freezing_lamp").withStyle(main));
            }
            if (base.getBlock() instanceof ApparitionDoorBlock) {
                tooltip.add(Component.translatable("info.goetied.apparition_door").withStyle(main));
            }
        }
    }
}