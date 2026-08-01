package za.co.infernos.goety.common.ritual;

import za.co.infernos.goety.common.blocks.entities.DarkAltarBlockEntity;
import za.co.infernos.goety.common.crafting.RitualRecipe;
import za.co.infernos.goety.common.items.WaystoneItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public class TeleportRitual extends Ritual {
    public TeleportRitual(RitualRecipe recipe) {
        super(recipe);
    }

    public boolean isValid(Level world, BlockPos darkAltarPos, DarkAltarBlockEntity tileEntity,
                           Player castingPlayer, ItemStack activationItem,
                           List<Ingredient> remainingAdditionalIngredients) {
        GlobalPos globalPos = WaystoneItem.getPosition(activationItem);
        return activationItem.getItem() instanceof WaystoneItem
                && globalPos != null
                && WaystoneItem.isSameDimension(castingPlayer, activationItem)
                && this.areAdditionalIngredientsFulfilled(world, darkAltarPos, castingPlayer, remainingAdditionalIngredients);
    }

    @Override
    public void finish(Level world, BlockPos blockPos, DarkAltarBlockEntity tileEntity,
                       Player castingPlayer, ItemStack activationItem) {
        super.finish(world, blockPos, tileEntity, castingPlayer, activationItem);

        GlobalPos globalPos = WaystoneItem.getPosition(activationItem);
        if (globalPos != null) {
            BlockPos blockPos1 = globalPos.pos();
            castingPlayer.teleportTo(blockPos1.getX(), blockPos1.getY() + 1.0F, blockPos1.getZ());

            if (world instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.PORTAL, blockPos.getX() + 0.5,
                        blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 1, 0, 0, 0, 0);
                serverLevel.playSound(null, BlockPos.containing(castingPlayer.xo, castingPlayer.yo, castingPlayer.zo), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
                serverLevel.playSound(null, blockPos1, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }

            activationItem.shrink(1);
        }
    }
}