package za.co.infernos.goety.common.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BrewMobEffect extends GoetyBaseEffect {
    public boolean curable;

    public BrewMobEffect(MobEffectCategory p_19451_, int p_19452_, boolean curable) {
        super(p_19451_, p_19452_);
        this.curable = curable;
    }

    public BrewMobEffect(MobEffectCategory p_19451_, int p_19452_) {
        this(p_19451_, p_19452_, true);
    }

}