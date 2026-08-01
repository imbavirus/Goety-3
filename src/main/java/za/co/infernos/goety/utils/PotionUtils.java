package za.co.infernos.goety.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class PotionUtils {

    private PotionUtils() {
    }

    public static Holder<Potion> getPotion(ItemStack stack) {
        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return contents.potion().orElse(Potions.WATER);
    }

    public static List<MobEffectInstance> getCustomEffects(ItemStack stack) {
        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return new ArrayList<>(contents.customEffects());
    }

    public static List<MobEffectInstance> getMobEffects(ItemStack stack) {
        List<MobEffectInstance> effects = new ArrayList<>();
        Holder<Potion> potion = getPotion(stack);
        effects.addAll(potion.value().getEffects());
        effects.addAll(getCustomEffects(stack));
        return effects;
    }

    public static ItemStack setPotion(ItemStack stack, Holder<Potion> potion) {
        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.ofNullable(potion), contents.customColor(), contents.customEffects()));
        return stack;
    }

    public static ItemStack setCustomEffects(ItemStack stack, Collection<MobEffectInstance> effects) {
        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(contents.potion(), contents.customColor(), List.copyOf(effects)));
        return stack;
    }

    public static int getColor(Iterable<MobEffectInstance> effects) {
        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;
        int totalWeight = 0;

        for (MobEffectInstance effect : effects) {
            if (!effect.isVisible()) {
                continue;
            }
            int color = effect.getEffect().value().getColor();
            int weight = effect.getAmplifier() + 1;
            r += (float) (weight * (color >> 16 & 255)) / 255.0F;
            g += (float) (weight * (color >> 8 & 255)) / 255.0F;
            b += (float) (weight * (color & 255)) / 255.0F;
            totalWeight += weight;
        }

        if (totalWeight == 0) {
            return 3694022;
        }

        int rr = (int) (r / totalWeight * 255.0F);
        int gg = (int) (g / totalWeight * 255.0F);
        int bb = (int) (b / totalWeight * 255.0F);
        return rr << 16 | gg << 8 | bb;
    }

    public static int getColor(ItemStack stack) {
        return getColor(getMobEffects(stack));
    }
}
