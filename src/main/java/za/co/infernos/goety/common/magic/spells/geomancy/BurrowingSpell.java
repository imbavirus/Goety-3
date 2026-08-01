package za.co.infernos.goety.common.magic.spells.geomancy;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.magic.EverChargeSpell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BurrowingSpell extends EverChargeSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BurrowingCost, 0);
    }

    @Override
    public int defaultCastUp() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BurrowingChargeUp, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.GEOMANCY;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        // Temporary migration-safe implementation.
    }

    public static void resetMiningProgress(Level world, Player player) {
        // Temporary migration-safe implementation.
    }
}
