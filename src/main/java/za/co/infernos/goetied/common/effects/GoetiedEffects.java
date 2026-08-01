package za.co.infernos.goetied.common.effects;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.config.BrewConfig;
import za.co.infernos.goetied.utils.ModUUIDUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForgeMod;
import za.co.infernos.goetied.compat.fml.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.resources.ResourceLocation;

public class GoetiedEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Goetied.MOD_ID);

    public static void init(){
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final DeferredHolder<MobEffect, ? extends MobEffect> ILLAGUE = EFFECTS.register("illague",
            IllagueEffect::new);

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SUMMON_DOWN = EFFECTS.register("summon_down",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> GOLD_TOUCHED = EFFECTS.register("gold_touched",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 4866583));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> BURN_HEX = EFFECTS.register("burn_hex",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 2236962));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SAPPED = EFFECTS.register("sapped",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x3f395f));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> CLIMBING = EFFECTS.register("climbing",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xf5e895));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> CHARGED = EFFECTS.register("charged",
            () -> new GoetiedBaseEffect(MobEffectCategory.NEUTRAL, 0xd67b5b));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> BUFF = EFFECTS.register("buff",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "buff_attack"),
                            1.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> RAMPAGE = EFFECTS.register("rampage",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x6a0000)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "rampage_attack"),
                            1.0D, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "rampage_speed"),
                            (double)0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> WANE = EFFECTS.register("wane",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x425b64)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "wane_attack"),
                            -4.0D, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "wane_speed"),
                            -0.15D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> BUSTED = EFFECTS.register("busted",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x232f58)
                    .addAttributeModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "busted_armor"),
                            -0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SOUL_ARMOR = EFFECTS.register("soul_armor",
            () -> new BrewMobEffect(MobEffectCategory.BENEFICIAL, 0x668785, false)
                    .addAttributeModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "soul_armor"),
                            2.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> IRON_HIDE = EFFECTS.register("iron_hide",
            () -> new BrewMobEffect(MobEffectCategory.BENEFICIAL, 0x585858, false)
                    .addAttributeModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "iron_hide"),
                            4.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> CHILL_HIDE = EFFECTS.register("chill_hide",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0)
                    .addAttributeModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "chill_hide"),
                            3.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SHADOW_WALK = EFFECTS.register("shadow_walk",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SOUL_HUNGER = EFFECTS.register("soul_hunger",
            SoulHungerEffect::new);

    public static final DeferredHolder<MobEffect, ? extends MobEffect> CURSED = EFFECTS.register("cursed",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x1e1f24));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> FREEZING = EFFECTS.register("freezing",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0xf4fcfc));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> DOOM = EFFECTS.register("doom",
            DoomEffect::new);

    public static final DeferredHolder<MobEffect, ? extends MobEffect> ACID_VENOM = EFFECTS.register("acid_venom",
            VenomEffect::new);

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SPASMS = EFFECTS.register("spasms",
            SpasmEffect::new);

    public static final DeferredHolder<MobEffect, ? extends MobEffect> ELECTRIFIED = EFFECTS.register("electrified",
            ElectrifiedEffect::new);

    public static final DeferredHolder<MobEffect, ? extends MobEffect> VOID_TOUCHED = EFFECTS.register("void_touched",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> IMPAIRED = EFFECTS.register("impaired",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> TREMOR_SENSE = EFFECTS.register("tremor_sense",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> WOUNDED = EFFECTS.register("wounded",
            () -> new BrewMobEffect(MobEffectCategory.HARMFUL, 0, false));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> CRIPPLED = EFFECTS.register("crippled",
            () -> new BrewMobEffect(MobEffectCategory.HARMFUL, 0, false)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "crippled_movement_speed"),
                            -0.75D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "crippled_attack_speed"),
                            -0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "crippled_attack_damage"),
                            -0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> STUNNED = EFFECTS.register("stunned",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0xffbc2e)
                    .addAttributeModifier(NeoForgeMod.SWIM_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "stunned_swim"),
                            -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "stunned_movement"),
                            -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> TANGLED = EFFECTS.register("tangled",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0)
                    .addAttributeModifier(NeoForgeMod.SWIM_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "tangled_swim"),
                            -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "tangled_movement"),
                            -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    //Brew Exclusive
    // Use default value during registration, config will be accessed lazily when effect is actually used
    public static final DeferredHolder<MobEffect, ? extends MobEffect> PRESSURE = EFFECTS.register("pressure",
            () -> new BrewMobEffect(MobEffectCategory.HARMFUL, 0x007200, true)); // Default to curable, actual value set lazily

    public static final DeferredHolder<MobEffect, ? extends MobEffect> ENDER_GROUND = EFFECTS.register("ender_ground",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x258474));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> ENDER_FLUX = EFFECTS.register("ender_flux",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x441d5a));

    // Use default values during registration, configs will be accessed lazily when effects are actually used
    public static final DeferredHolder<MobEffect, ? extends MobEffect> NYCTOPHOBIA = EFFECTS.register("nyctophobia",
            () -> new BrewMobEffect(MobEffectCategory.HARMFUL, 0x0d1305, true)); // Default to curable

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SUN_ALLERGY = EFFECTS.register("sun_allergy",
            () -> new BrewMobEffect(MobEffectCategory.HARMFUL, 0x1f1421, true)); // Default to curable

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SNOW_SKIN = EFFECTS.register("snow_skin",
            () -> new BrewMobEffect(MobEffectCategory.HARMFUL, 0xe3f3f3, true)); // Default to curable

    public static final DeferredHolder<MobEffect, ? extends MobEffect> EVIL_EYE = EFFECTS.register("evil_eye",
            () -> new EvilEyeEffect(MobEffectCategory.HARMFUL, 0x560269, true)); // Default to curable

    public static final DeferredHolder<MobEffect, ? extends MobEffect> TRIPPING = EFFECTS.register("tripping",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x101636));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> ARROWMANTIC = EFFECTS.register("arrowmantic",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x969696));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> PLUNGE = EFFECTS.register("plunge",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x8d989a));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> FLIMSY = EFFECTS.register("flimsy",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0xf5f5f5)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "flimsy_knockback"),
                    -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SENSE_LOSS = EFFECTS.register("sense_loss",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 2039587));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> FLAMMABLE = EFFECTS.register("flammable",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0x1e0f07));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> STORMS_WRATH = EFFECTS.register("storms_wrath",
            () -> new GoetiedBaseEffect(MobEffectCategory.HARMFUL, 0xe77c56));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> EXPLOSIVE = EFFECTS.register("explosive",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x912d11));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SWIFT_SWIM = EFFECTS.register("swift_swim",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xbead6a)
                    .addAttributeModifier(NeoForgeMod.SWIM_SPEED, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "swift_swim"),
                            1.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> FROG_LEG = EFFECTS.register("frog_leg",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x6abe30));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> FLAME_HANDS = EFFECTS.register("flame_hands",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xff3d29));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> VENOMOUS_HANDS = EFFECTS.register("venomous_hands",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x122620));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> REPULSIVE = EFFECTS.register("repulsive",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x67502c));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> FIRE_TRAIL = EFFECTS.register("fire_trail",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xffc800));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> FIERY_AURA = EFFECTS.register("fiery_aura",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xff0000));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> FROSTY_AURA = EFFECTS.register("frosty_aura",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x212d5f));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> PHOTOSYNTHESIS = EFFECTS.register("photosynthesis",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xffec4f));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> INSIGHT = EFFECTS.register("insight",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x59b057));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> BOTTLING = EFFECTS.register("bottling",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x3e250f));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> CORPSE_EATER = EFFECTS.register("corpse_eater",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x48565e));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> FORTUNATE = EFFECTS.register("fortunate",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x4aedd9));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> ALTRUISTIC = EFFECTS.register("altruistic",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xa10000));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> RADIANCE = EFFECTS.register("radiance",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xffbc2e));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> LEECHING = EFFECTS.register("leeching",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x690000));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SHIELDING = EFFECTS.register("shielding",
            () -> new AuraEffect(MobEffectCategory.BENEFICIAL, 0xb3bec0));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SHIELDED = EFFECTS.register("shielded",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x939d9e));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> RALLYING = EFFECTS.register("rallying",
            () -> new AuraEffect(MobEffectCategory.BENEFICIAL, 0xff8609)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "rallying_attack"),
                            0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> RALLIED = EFFECTS.register("rallied",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xf65500)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "rallied_attack"),
                            0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> DEFLECTIVE = EFFECTS.register("deflective",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xa575a5));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SWIRLING = EFFECTS.register("swirling",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0xffffff));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> SAVE_EFFECTS = EFFECTS.register("save_effects",
            () -> new GoetiedBaseEffect(MobEffectCategory.BENEFICIAL, 0x4f446b));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> GRAVITY_PULSE = EFFECTS.register("gravity_pulse",
            () -> new GoetiedBaseEffect(MobEffectCategory.NEUTRAL, 0x580c56));

    public static final DeferredHolder<MobEffect, ? extends MobEffect> WILD_RAGE = EFFECTS.register("wild_rage",
            () -> new GoetiedBaseEffect(MobEffectCategory.NEUTRAL, 0xa8311c));
}