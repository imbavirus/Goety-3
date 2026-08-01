package za.co.infernos.goetied.common.effects.brew;

import za.co.infernos.goetied.common.entities.ally.AnimalSummon;
import za.co.infernos.goetied.common.entities.ally.illager.AbstractIllagerServant;
import za.co.infernos.goetied.config.BrewConfig;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;

import javax.annotation.Nullable;

public class FertilityBrewEffect extends BrewEffect {
    public FertilityBrewEffect() {
        super("fertility", za.co.infernos.goetied.utils.ConfigHelper.getInt(BrewConfig.FertilityCost, 0), MobEffectCategory.BENEFICIAL, 0x515151);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public boolean canLinger() {
        return true;
    }

    public void applyEntityEffect(LivingEntity pTarget, @Nullable Entity pSource, @Nullable Entity pIndirectSource, int pAmplifier){
        if (!pTarget.level().isClientSide) {
            if (pTarget instanceof Animal animal){
                if (animal.getAge() > 0){
                    animal.setAge(0);
                }
            }
            if (pTarget instanceof AnimalSummon animal){
                if (animal.getAge() > 0){
                    animal.setAge(0);
                }
            }
            if (pTarget instanceof AbstractIllagerServant servant){
                if (servant.getBreedCool() > 0){
                    servant.setBreedCool(0);
                }
            }
        }
    }
}
