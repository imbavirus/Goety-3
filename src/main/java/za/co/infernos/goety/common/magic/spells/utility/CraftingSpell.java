package za.co.infernos.goety.common.magic.spells.utility;

import za.co.infernos.goety.client.inventory.container.CraftingFocusMenu;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class CraftingSpell extends Spell {
    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CraftingCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CraftingDuration, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return null;
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CraftingCoolDown, 0);
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        if (caster instanceof Player player){
            player.openMenu(new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) -> {
                return new CraftingFocusMenu(p_52229_, p_52230_, ContainerLevelAccess.create(worldIn, caster.blockPosition()));
            }, CONTAINER_TITLE));
        }
    }
}