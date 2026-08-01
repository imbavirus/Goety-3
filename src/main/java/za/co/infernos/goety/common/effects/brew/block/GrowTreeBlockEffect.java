package za.co.infernos.goety.common.effects.brew.block;

import za.co.infernos.goety.common.effects.brew.BrewEffect;
import za.co.infernos.goety.config.BrewConfig;
import za.co.infernos.goety.utils.BlockFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class GrowTreeBlockEffect extends BrewEffect {
    public Block block;
    public TreeGrower treeGrower;

    public GrowTreeBlockEffect(Block block, TreeGrower treeGrower) {
        super(block.getDescriptionId(), za.co.infernos.goety.utils.ConfigHelper.getInt(BrewConfig.GrowTreeCost, 0), 0, MobEffectCategory.NEUTRAL, 0x5a3f1e, true);
        this.block = block;
        this.treeGrower = treeGrower;
    }

    @Override
    public void applyBlockEffect(Level pLevel, BlockPos pPos, LivingEntity pSource, int pAmplifier, int pAreaOfEffect) {
        if (pLevel instanceof ServerLevel serverLevel) {
            BlockState blockState = serverLevel.getBlockState(pPos.above());
            if (this.treeGrower != null) {
                if (this.block.defaultBlockState().canSurvive(pLevel, pPos.above()) || BlockFinder.canBeReplaced(pLevel, pPos)) {
                    this.treeGrower.growTree(serverLevel, serverLevel.getChunkSource().getGenerator(), pPos.above(), blockState, serverLevel.random);
                }
            }
        }
    }

    public String getDescriptionId() {
        return "effect.goety.grow_tree";
    }

    public MutableComponent getDisplayName() {
        return Component.translatable(this.getDescriptionId()).append(" - ").append(Component.translatable(this.block.getDescriptionId()));
    }

}
