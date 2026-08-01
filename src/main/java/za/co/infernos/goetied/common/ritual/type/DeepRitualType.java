package za.co.infernos.goetied.common.ritual.type;

import za.co.infernos.goetied.api.ritual.IRitualType;
import za.co.infernos.goetied.common.blocks.entities.RitualBlockEntity;
import za.co.infernos.goetied.common.ritual.RitualRequirements;
import za.co.infernos.goetied.common.ritual.RitualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class DeepRitualType implements IRitualType {
    @Override
    public String getName() {
        return RitualTypes.DEEP;
    }

    @Override
    public ItemStack getJeiIcon() {
        return new ItemStack(Blocks.PRISMARINE_BRICKS);
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, Player pPlayer, BlockPos pPos, Level pLevel) {
        return RitualRequirements.deepRitual(pPlayer, pTileEntity, pLevel, pPos);
    }
}