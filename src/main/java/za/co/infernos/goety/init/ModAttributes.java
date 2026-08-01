package za.co.infernos.goety.init;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.magic.ISpell;
import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.entities.ai.attributes.SpellAttribute;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import za.co.infernos.goety.compat.fml.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModAttributes {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, Goety.MOD_ID);

    public static void init(){
        ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final DeferredHolder<Attribute, Attribute> SPELL_POTENCY = ATTRIBUTES.register("spell_potency", () -> (new RangedAttribute("attribute.name.goety.spell_potency", 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SPELL_DURATION = ATTRIBUTES.register("spell_duration", () -> (new RangedAttribute("attribute.name.goety.spell_duration", 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SPELL_RANGE = ATTRIBUTES.register("spell_range", () -> (new RangedAttribute("attribute.name.goety.spell_range", 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SPELL_RADIUS = ATTRIBUTES.register("spell_radius", () -> (new RangedAttribute("attribute.name.goety.spell_radius", 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SPELL_BURNING = ATTRIBUTES.register("spell_burning", () -> (new RangedAttribute("attribute.name.goety.spell_burning", 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SPELL_VELOCITY = ATTRIBUTES.register("spell_velocity", () -> (new RangedAttribute("attribute.name.goety.spell_velocity", 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> CASTING_SPEED = ATTRIBUTES.register("casting_speed", () -> (new RangedAttribute("attribute.name.goety.casting_speed", 0.0D, -1.0D, 1.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> COOLDOWN_DISCOUNT = ATTRIBUTES.register("cooldown_discount", () -> (new RangedAttribute("attribute.name.goety.cooldown_discount", 0.0D, -1.0D, 1.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SOUL_DISCOUNT = ATTRIBUTES.register("soul_discount", () -> (new RangedAttribute("attribute.name.goety.soul_discount", 0.0D, -1.0D, 1.0D).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> ABYSS_POTENCY = ATTRIBUTES.register("abyss_potency", () -> (SpellAttribute.potency(SpellType.ABYSS, 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> FROST_POTENCY = ATTRIBUTES.register("frost_potency", () -> (SpellAttribute.potency(SpellType.FROST, 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> GEOMANCY_POTENCY = ATTRIBUTES.register("geomancy_potency", () -> (SpellAttribute.potency(SpellType.GEOMANCY, 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> NECROMANCY_POTENCY = ATTRIBUTES.register("necromancy_potency", () -> (SpellAttribute.potency(SpellType.NECROMANCY, 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> NETHER_POTENCY = ATTRIBUTES.register("nether_potency", () -> (SpellAttribute.potency(SpellType.NETHER, 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> STORM_POTENCY = ATTRIBUTES.register("storm_potency", () -> (SpellAttribute.potency(SpellType.STORM, 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> VOID_POTENCY = ATTRIBUTES.register("void_potency", () -> (SpellAttribute.potency(SpellType.VOID, 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> WILD_POTENCY = ATTRIBUTES.register("wild_potency", () -> (SpellAttribute.potency(SpellType.WILD, 0.0D, 0.0D, 2048.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> WIND_POTENCY = ATTRIBUTES.register("wind_potency", () -> (SpellAttribute.potency(SpellType.WIND, 0.0D, 0.0D, 2048.0D).setSyncable(true)));

    public static int getPotency(LivingEntity livingEntity) {
        return (int) livingEntity.getAttributeValue(ModAttributes.SPELL_POTENCY);
    }

    public static int getDuration(LivingEntity livingEntity) {
        return (int) livingEntity.getAttributeValue(ModAttributes.SPELL_DURATION);
    }

    public static int getRange(LivingEntity livingEntity) {
        return (int) livingEntity.getAttributeValue(ModAttributes.SPELL_RANGE);
    }

    public static double getRadius(LivingEntity livingEntity) {
        return livingEntity.getAttributeValue(ModAttributes.SPELL_RADIUS);
    }

    public static int getBurning(LivingEntity livingEntity) {
        return (int) livingEntity.getAttributeValue(ModAttributes.SPELL_BURNING);
    }

    public static float getVelocity(LivingEntity livingEntity) {
        return (float) livingEntity.getAttributeValue(ModAttributes.SPELL_VELOCITY);
    }

    public static int getPotency(LivingEntity livingEntity, ISpell spell) {
        Holder<Attribute> attribute = ModAttributes.SPELL_POTENCY;
        Optional<? extends Holder<Attribute>> optional = ATTRIBUTES.getEntries()
                .stream()
                .filter(attribute1 -> attribute1.value() instanceof SpellAttribute spellAttribute
                        && spellAttribute.getSpellType() == spell.getSpellType())
                .findFirst();
        if (optional.isPresent()){
            attribute = optional.get();
        }
        return (int) livingEntity.getAttributeValue(attribute);
    }

    public static double getCastingSpeed(LivingEntity livingEntity) {
        return 1.0D - livingEntity.getAttributeValue(ModAttributes.CASTING_SPEED);
    }

    public static double getCooldownDiscount(LivingEntity livingEntity) {
        return 1.0D - livingEntity.getAttributeValue(ModAttributes.COOLDOWN_DISCOUNT);
    }

    public static double getSoulDiscount(LivingEntity livingEntity) {
        return 1.0D - livingEntity.getAttributeValue(ModAttributes.SOUL_DISCOUNT);
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> event.add(entity, attribute)));
    }

}
