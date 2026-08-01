package za.co.infernos.goety.common.ritual.type;

import za.co.infernos.goety.api.ritual.IRitualType;
import za.co.infernos.goety.common.blocks.entities.RitualBlockEntity;
import za.co.infernos.goety.common.ritual.RitualRequirements;
import za.co.infernos.goety.common.ritual.RitualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MagicRitualType implements IRitualType {
    @Override
    public String getName() {
        return RitualTypes.MAGIC;
    }

    @Override
    public ItemStack getJeiIcon() {
        return new ItemStack(Items.ENCHANTING_TABLE);
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, Player pPlayer, BlockPos pPos, Level pLevel) {
        return RitualRequirements.getStructures(this.getName(), pPlayer, pPos, pLevel);
    }
}