package za.co.infernos.goetied.common.ritual.type;

import za.co.infernos.goetied.api.ritual.IRitualType;
import za.co.infernos.goetied.common.blocks.ModBlocks;
import za.co.infernos.goetied.common.blocks.entities.RitualBlockEntity;
import za.co.infernos.goetied.common.ritual.RitualRequirements;
import za.co.infernos.goetied.common.ritual.RitualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FrostRitualType implements IRitualType {
    @Override
    public String getName() {
        return RitualTypes.FROST;
    }

    @Override
    public ItemStack getJeiIcon() {
        return new ItemStack(ModBlocks.FREEZING_LAMP.get());
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, Player pPlayer, BlockPos pPos, Level pLevel) {
        return RitualRequirements.frostRitual(pPlayer, pPos, pLevel);
    }
}