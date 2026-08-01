package za.co.infernos.goetied.common.magic.spells.void_spells;

import za.co.infernos.goetied.api.magic.SpellType;
import za.co.infernos.goetied.common.magic.TouchSpell;
import za.co.infernos.goetied.config.SpellConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BanishSpell extends TouchSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.BanishCost, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.BanishCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }

    @Override
    public void touchResult(ServerLevel worldIn, LivingEntity caster, LivingEntity target, ItemStack staff, za.co.infernos.goetied.common.magic.SpellStat spellStat) {
        // Temporary migration-safe implementation.
    }
}
