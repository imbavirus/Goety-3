package za.co.infernos.goety.common.effects.brew;

import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SPlayWorldSoundPacket;
import za.co.infernos.goety.config.BrewConfig;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class LaunchBrewEffect extends BrewEffect {
    public LaunchBrewEffect() {
        super("launch", za.co.infernos.goety.utils.ConfigHelper.getInt(BrewConfig.LaunchCost, 0), MobEffectCategory.NEUTRAL, 0xb2ccd1);
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
            ModNetwork.sendToALL(new SPlayWorldSoundPacket(pTarget.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 1.0F));
        }
        pTarget.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH);
        int extra = 0;
        if (pIndirectSource instanceof LivingEntity livingEntity){
            if (CuriosFinder.hasWindyRobes(livingEntity)){
                extra = 1;
            }
        }
        MobUtil.push(pTarget, 0.0F, 1.0F + extra + pAmplifier, 0.0F);
    }
}
