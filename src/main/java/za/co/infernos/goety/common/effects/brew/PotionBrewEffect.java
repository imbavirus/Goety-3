package za.co.infernos.goety.common.effects.brew;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PotionBrewEffect extends BrewEffect{
    public Holder<MobEffect> mobEffect;
    public Holder<MobEffect> inverted;

    public PotionBrewEffect(Holder<MobEffect> effect, int soulCost, int cap, int duration) {
        super(effect.value(), soulCost, cap, effect.value().getCategory(), effect.value().getColor());
        this.mobEffect = effect;
        this.duration = duration;
    }

    public PotionBrewEffect(Holder<MobEffect> effect, int soulCost, int duration) {
        super(effect.value(), soulCost, effect.value().getCategory(), effect.value().getColor());
        this.mobEffect = effect;
        this.duration = duration;
    }

    public PotionBrewEffect(Holder<MobEffect> effect, Holder<MobEffect> inverted, int soulCost, int duration){
        super(effect.value(), soulCost, effect.value().getCategory(), effect.value().getColor());
        this.mobEffect = effect;
        this.inverted = inverted;
        this.duration = duration;
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity p_19467_, int p_19468_) {
        mobEffect.value().applyEffectTick(p_19467_, p_19468_);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {
        mobEffect.value().applyInstantenousEffect(pSource, pIndirectSource, pLivingEntity, pAmplifier, pHealth);
    }

    @Override
    public boolean isInstantenous() {
        return mobEffect.value().isInstantenous();
    }

    @Override
    public boolean canLinger() {
        return true;
    }

    @Override
    protected String getOrCreateDescriptionId() {
        return this.mobEffect.value().getDescriptionId();
    }
}