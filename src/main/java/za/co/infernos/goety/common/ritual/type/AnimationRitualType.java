package za.co.infernos.goety.common.ritual.type;

import za.co.infernos.goety.api.ritual.IRitualType;
import za.co.infernos.goety.common.blocks.entities.RitualBlockEntity;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.ritual.RitualRequirements;
import za.co.infernos.goety.common.ritual.RitualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AnimationRitualType implements IRitualType {
    @Override
    public String getName() {
        return RitualTypes.ANIMATION;
    }

    @Override
    public ItemStack getJeiIcon() {
        return new ItemStack(ModItems.ANIMATION_CORE.get());
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, Player pPlayer, BlockPos pPos, Level pLevel) {
        return RitualRequirements.getStructures(this.getName(), pPlayer, pPos, pLevel);
    }
}