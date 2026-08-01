package za.co.infernos.goetied.common.effects;

import za.co.infernos.goetied.Goetied;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SoulHungerEffect extends GoetiedBaseEffect {

    public SoulHungerEffect() {
        super(MobEffectCategory.NEUTRAL, 5064781);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "soul_hunger_attack"), -4.0D, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "soul_hunger_speed"), (double)-0.05F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "soul_hunger_attack_speed"), (double)-0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}