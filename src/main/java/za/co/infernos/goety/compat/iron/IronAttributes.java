package za.co.infernos.goety.compat.iron;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class IronAttributes {
    public static final Holder<Attribute> FIRE_MAGIC_RESIST = getResistanceAttribute("fire");
    public static final Holder<Attribute> ICE_MAGIC_RESIST = getResistanceAttribute("ice");
    public static final Holder<Attribute> LIGHTNING_MAGIC_RESIST = getResistanceAttribute("lightning");
    public static final Holder<Attribute> HOLY_MAGIC_RESIST = getResistanceAttribute("holy");
    public static final Holder<Attribute> ENDER_MAGIC_RESIST = getResistanceAttribute("ender");
    public static final Holder<Attribute> BLOOD_MAGIC_RESIST = getResistanceAttribute("blood");
    public static final Holder<Attribute> EVOCATION_MAGIC_RESIST = getResistanceAttribute("evocation");
    public static final Holder<Attribute> NATURE_MAGIC_RESIST = getResistanceAttribute("nature");
    public static final Holder<Attribute> ELDRITCH_MAGIC_RESIST = getResistanceAttribute("eldritch");

    public static final Holder<Attribute> FIRE_SPELL_POWER = getPowerAttribute("fire");
    public static final Holder<Attribute> ICE_SPELL_POWER = getPowerAttribute("ice");
    public static final Holder<Attribute> LIGHTNING_SPELL_POWER = getPowerAttribute("lightning");
    public static final Holder<Attribute> HOLY_SPELL_POWER = getPowerAttribute("holy");
    public static final Holder<Attribute> ENDER_SPELL_POWER = getPowerAttribute("ender");
    public static final Holder<Attribute> BLOOD_SPELL_POWER = getPowerAttribute("blood");
    public static final Holder<Attribute> EVOCATION_SPELL_POWER = getPowerAttribute("evocation");
    public static final Holder<Attribute> NATURE_SPELL_POWER = getPowerAttribute("nature");
    public static final Holder<Attribute> ELDRITCH_SPELL_POWER = getPowerAttribute("eldritch");

    public static final Holder<Attribute> MAX_MANA = getAttribute("max_mana");
    public static final Holder<Attribute> MANA_REGEN = getAttribute("mana_regen");
    public static final Holder<Attribute> SUMMON_DAMAGE = getAttribute("summon_damage");
    public static final Holder<Attribute> SPELL_POWER = getAttribute("spell_power");
    public static final Holder<Attribute> SPELL_RESIST = getAttribute("spell_resist");

    private static Holder<Attribute> getAttribute(String id) {
        return net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", id)).orElse(null);
    }

    private static Holder<Attribute> getResistanceAttribute(String id) {
        return net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE
                .getHolder(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", id + "_magic_resist")).orElse(null);
    }

    private static Holder<Attribute> getPowerAttribute(String id) {
        return net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE
                .getHolder(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", id + "_spell_power")).orElse(null);
    }

    public static List<AttributeInstance> power(Mob mob) {
        List<AttributeInstance> list = new ArrayList<>();
        for (net.minecraft.core.Holder<Attribute> attribute : net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE.asHolderIdMap()) {
            if (attribute.value().getDescriptionId().contains("irons_spellbooks")
                    && attribute.value().getDescriptionId().contains("_spell_power")) {
                if (mob.getAttribute(attribute) != null) {
                    list.add(mob.getAttribute(attribute));
                }
            }
        }
        return list;
    }

    public static List<AttributeInstance> resistances(Mob mob) {
        List<AttributeInstance> list = new ArrayList<>();
        for (net.minecraft.core.Holder<Attribute> attribute : net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE.asHolderIdMap()) {
            if (attribute.value().getDescriptionId().contains("irons_spellbooks")
                    && attribute.value().getDescriptionId().contains("_magic_resist")) {
                if (mob.getAttribute(attribute) != null) {
                    list.add(mob.getAttribute(attribute));
                }
            }
        }
        return list;
    }
}