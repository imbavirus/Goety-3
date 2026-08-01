package za.co.infernos.goety.common.effects.brew.block;

import za.co.infernos.goety.common.effects.brew.BrewEffect;
import za.co.infernos.goety.common.magic.spells.geomancy.PulverizeSpell;
import za.co.infernos.goety.config.BrewConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PulverizeBlockEffect extends BrewEffect {
    public PulverizeBlockEffect() {
        super("pulverize", za.co.infernos.goety.utils.ConfigHelper.getInt(BrewConfig.PulverizeCost, 0), 2, MobEffectCategory.NEUTRAL, 0xbae633);
    }

    @Override
    public void applyBlockEffect(Level pLevel, BlockPos pPos, LivingEntity pSource, int pAmplifier, int pAreaOfEffect) {
        if (pLevel instanceof ServerLevel serverLevel) {
            for (BlockPos blockPos : this.getSpherePos(pPos, pAreaOfEffect + 3)) {
                new PulverizeSpell().pulverize(serverLevel, pSource, blockPos);
            }
        }
    }
}