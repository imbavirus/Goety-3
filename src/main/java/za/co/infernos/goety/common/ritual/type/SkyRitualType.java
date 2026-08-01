package za.co.infernos.goety.common.ritual.type;

import za.co.infernos.goety.api.ritual.IRitualType;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.entities.DarkAltarBlockEntity;
import za.co.infernos.goety.common.blocks.entities.RitualBlockEntity;
import za.co.infernos.goety.common.ritual.RitualRequirements;
import za.co.infernos.goety.common.ritual.RitualTypes;
import za.co.infernos.goety.utils.ColorUtil;
import za.co.infernos.goety.utils.ServerParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SkyRitualType implements IRitualType {
    @Override
    public String getName() {
        return RitualTypes.SKY;
    }

    @Override
    public ItemStack getJeiIcon() {
        return new ItemStack(ModBlocks.MARBLE_BLOCK.get());
    }

    @Override
    public boolean getRequirement(RitualBlockEntity pTileEntity, Player pPlayer, BlockPos pPos, Level pLevel) {
        return RitualRequirements.skyRitual(pPlayer, pTileEntity, pLevel, pPos);
    }

    public void onPerformRitual(Level world, BlockPos darkAltarPos, DarkAltarBlockEntity tileEntity,
                                Player castingPlayer, ItemStack activationItem) {
        if (world instanceof ServerLevel serverLevel) {
            ColorUtil color = new ColorUtil(0xffffff);
            ServerParticleUtil.windParticle(serverLevel, color, 1.0F + world.random.nextFloat() * 0.5F, 0.0F, -1, Vec3.atBottomCenterOf(darkAltarPos));
        }
    }
}