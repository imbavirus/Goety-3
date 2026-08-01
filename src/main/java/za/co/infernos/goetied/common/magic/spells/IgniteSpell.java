package za.co.infernos.goetied.common.magic.spells;

import za.co.infernos.goetied.api.magic.ITouchSpell;
import za.co.infernos.goetied.common.magic.BlockSpell;
import za.co.infernos.goetied.common.magic.SpellStat;
import za.co.infernos.goetied.config.SpellConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class IgniteSpell extends BlockSpell implements ITouchSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.IgniteCost, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.IgniteCoolDown, 0);
    }

    @Override
    public boolean rightBlock(ServerLevel worldIn, LivingEntity caster, BlockPos target, SpellStat spellStat) {
        return true;
    }

    @Override
    public void blockResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, BlockPos target, SpellStat spellStat) {
    }

    @Override
    public void touchResult(ServerLevel worldIn, LivingEntity caster, LivingEntity target, ItemStack staff, SpellStat spellStat) {
        target.igniteForSeconds(za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.IgniteFireSeconds, 0));
    }
}
