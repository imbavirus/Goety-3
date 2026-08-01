package za.co.infernos.goetied.common.ritual.type;

import za.co.infernos.goetied.api.ritual.IRitualType;
import za.co.infernos.goetied.common.blocks.ModBlocks;
import za.co.infernos.goetied.common.blocks.entities.RitualBlockEntity;
import za.co.infernos.goetied.common.ritual.RitualRequirements;
import za.co.infernos.goetied.common.ritual.RitualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EndRitualType implements IRitualType {
    @Override
    public String getName() {
        return RitualTypes.END;
    }

    @Override
    public ItemStack getJeiIcon() {
        return new ItemStack(ModBlocks.VOID_BLOCK.get());
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, Player pPlayer, BlockPos pPos, Level pLevel) {
        if (!(pLevel.dimension() == Level.END || pLevel.getBiome(pPos).is(BiomeTags.IS_END))) {
            if (pPlayer != null) {
                pPlayer.displayClientMessage(Component.translatable("info.goetied.ritual.structure.end"), true);
            }
            return false;
        }
        return RitualRequirements.getStructures(this.getName(), pPlayer, pPos, pLevel);
    }
}