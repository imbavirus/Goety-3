package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.api.magic.SpellType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MagicCrownItem extends SingleStackItem {
    public SpellType spellType;

    public MagicCrownItem(SpellType spellType) {
        this(new Properties().stacksTo(1), spellType);
    }

    public MagicCrownItem(Properties properties, SpellType spellType) {
        super(properties);
        this.spellType = spellType;
    }

    public SpellType getSpellType() {
        return this.spellType;
    }

    @Override
    public boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity) {
        return this.spellType == SpellType.VOID;
    }
}