package za.co.infernos.goety.common.magic.spells.geomancy;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.magic.BlockSpell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class PulverizeSpell extends BlockSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PulverizeCost, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PulverizeCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.GEOMANCY;
    }

    @Override
    public boolean rightBlock(ServerLevel worldIn, LivingEntity caster, BlockPos target, Direction direction, SpellStat spellStat) {
        return false;
    }

    @Override
    public void blockResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, BlockPos target, Direction direction, SpellStat spellStat) {
        // Temporary migration-safe implementation.
    }

    public void pulverize(ServerLevel worldIn, LivingEntity caster, BlockPos target) {
        worldIn.setBlockAndUpdate(target, Blocks.AIR.defaultBlockState());
    }
}
