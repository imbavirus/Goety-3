package za.co.infernos.goety.client.particles;

import za.co.infernos.goety.Goety;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModParticleTypes {
        public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister
                        .create(Registries.PARTICLE_TYPE, Goety.MOD_ID);

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NONE = PARTICLE_TYPES.register("none",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TOTEM_EFFECT = PARTICLE_TYPES.register(
                        "totem_effect",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PLAGUE_EFFECT = PARTICLE_TYPES.register(
                        "plague_effect",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DOOM = PARTICLE_TYPES.register("doom",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DOOM_DEATH = PARTICLE_TYPES.register(
                        "doom_death",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HEAL_EFFECT = PARTICLE_TYPES.register(
                        "heal",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HEAL_EFFECT_2 = PARTICLE_TYPES.register(
                        "heal2",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BULLET_EFFECT = PARTICLE_TYPES.register(
                        "bullet_effect",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NECRO_EFFECT = PARTICLE_TYPES.register(
                        "necro_effect",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NECRO_BOLT = PARTICLE_TYPES.register(
                        "necro_bolt",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STUN = PARTICLE_TYPES.register("stun",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOUL_LIGHT_EFFECT = PARTICLE_TYPES
                        .register("soul_light",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GLOW_EFFECT = PARTICLE_TYPES.register(
                        "glow_trail",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GLOW_LIGHT_EFFECT = PARTICLE_TYPES
                        .register("glow_light",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOUL_EXPLODE_BITS = PARTICLE_TYPES
                        .register("soul_explode_bits",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> XP_TAKE = PARTICLE_TYPES.register(
                        "xp_take",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LASER_GATHER = PARTICLE_TYPES.register(
                        "laser",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RESONANCE_GATHER = PARTICLE_TYPES
                        .register("resonance",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BURNING = PARTICLE_TYPES.register(
                        "burning",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIERY_PILLAR = PARTICLE_TYPES.register(
                        "fiery_pillar",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CULT_SPELL = PARTICLE_TYPES.register(
                        "cult_spell",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_CULT_SPELL = PARTICLE_TYPES
                        .register("big_cult_spell",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_STATION_CULT_SPELL = PARTICLE_TYPES
                        .register("small_station_cult_spell",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STATION_CULT_SPELL = PARTICLE_TYPES
                        .register("station_cult_spell",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUD_GAS = PARTICLE_TYPES.register(
                        "mud_gas",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LICH = PARTICLE_TYPES.register("lich",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CONFUSED = PARTICLE_TYPES.register(
                        "confused",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WHITE_EFFECT = PARTICLE_TYPES.register(
                        "white_effect",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WRAITH = PARTICLE_TYPES.register(
                        "wraith",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WRAITH_BURST = PARTICLE_TYPES.register(
                        "wraith_burst",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WRAITH_FIRE = PARTICLE_TYPES.register(
                        "wraith_fire",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_FIRE = PARTICLE_TYPES.register(
                        "big_fire",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_FIRE_DROP = PARTICLE_TYPES.register(
                        "big_fire_drop",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_FIRE_GROUND = PARTICLE_TYPES
                        .register("big_fire_ground",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_SOUL_FIRE = PARTICLE_TYPES.register(
                        "big_soul_fire",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_SOUL_FIRE_DROP = PARTICLE_TYPES
                        .register("big_soul_fire_drop",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_SOUL_FIRE_GROUND = PARTICLE_TYPES
                        .register("big_soul_fire_ground",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NECRO_FIRE = PARTICLE_TYPES.register(
                        "necro_fire",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NECRO_FIRE_DROP = PARTICLE_TYPES
                        .register("necro_fire_drop",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_NECRO_FIRE = PARTICLE_TYPES
                        .register("small_necro_fire",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NECRO_FLAME = PARTICLE_TYPES.register(
                        "necro_flame",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DRAGON_FLAME = PARTICLE_TYPES.register(
                        "dragon_flame",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DRAGON_FLAME_DROP = PARTICLE_TYPES
                        .register("dragon_flame_drop",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_DRAGON_FLAME = PARTICLE_TYPES
                        .register("small_dragon_flame",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_DRAGON_FLAME_GROUND = PARTICLE_TYPES
                        .register("small_dragon_flame_ground",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> END_FIRE = PARTICLE_TYPES.register(
                        "end_fire",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> END_FIRE_DROP = PARTICLE_TYPES.register(
                        "end_fire_drop",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_END_FIRE = PARTICLE_TYPES
                        .register("small_end_fire",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FROST = PARTICLE_TYPES.register("frost",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FROST_NOVA = PARTICLE_TYPES.register(
                        "frost_nova",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLY = PARTICLE_TYPES.register("fly",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BONE = PARTICLE_TYPES.register("bone",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LEECH = PARTICLE_TYPES.register("leech",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CHANT = PARTICLE_TYPES.register("chant",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ELECTRIC = PARTICLE_TYPES.register(
                        "electric",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_ELECTRIC = PARTICLE_TYPES.register(
                        "big_electric",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPELL_ELECTRIC = PARTICLE_TYPES
                        .register("spell_electric",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BUBBLE_STREAM = PARTICLE_TYPES.register(
                        "bubble_stream",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BREW_BUBBLE = PARTICLE_TYPES.register(
                        "brew_bubble",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WIND_BLAST = PARTICLE_TYPES.register(
                        "wind_blast",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WARLOCK = PARTICLE_TYPES.register(
                        "warlock",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FUNGUS_EXPLOSION = PARTICLE_TYPES
                        .register("fungus_explosion",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FUNGUS_EXPLOSION_EMITTER = PARTICLE_TYPES
                        .register("fungus_explosion_emitter",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOUL_EXPLODE = PARTICLE_TYPES.register(
                        "soul_explode",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SUMMON = PARTICLE_TYPES.register(
                        "summon",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> VOID_SPAWNER_DETECTION = PARTICLE_TYPES
                        .register("void_spawner_detection",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> VOID_VAULT_CONNECT = PARTICLE_TYPES
                        .register("void_vault_connect",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPELL_SQUARE = PARTICLE_TYPES.register(
                        "spell_square",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_SPELL_SQUARE = PARTICLE_TYPES
                        .register("small_spell_square",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TRAIL = PARTICLE_TYPES.register("trail",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SUMMON_TRAIL = PARTICLE_TYPES.register(
                        "summon_trail",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPELL_CLOUD = PARTICLE_TYPES.register(
                        "spell_cloud",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DROPLET = PARTICLE_TYPES.register(
                        "droplet",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GO = PARTICLE_TYPES.register("go",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STOP = PARTICLE_TYPES.register("stop",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FANG_RAIN = PARTICLE_TYPES.register(
                        "fang_rain",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MAGIC_BOLT = PARTICLE_TYPES.register(
                        "magic_bolt",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RISING_SPIRAL = PARTICLE_TYPES.register(
                        "rising_spiral",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RISING_ENCHANT = PARTICLE_TYPES
                        .register("rising_enchant",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ROLLING_SPIRAL = PARTICLE_TYPES
                        .register("rolling_spiral",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ROLLING_ENCHANT = PARTICLE_TYPES
                        .register("rolling_enchant",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> REDSTONE_EXPLODE = PARTICLE_TYPES
                        .register("redstone_explode",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ELECTRIC_EXPLODE = PARTICLE_TYPES
                        .register("electric_explode",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FAN_CLOUD = PARTICLE_TYPES.register(
                        "fan_cloud",
                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> REDSTONE_DEBRIS = PARTICLE_TYPES
                        .register("redstone_debris",
                                        () -> new SimpleParticleType(false));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GOO_STAIN = PARTICLE_TYPES.register(
                        "goo_stain",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CHORUS_LEAVES = PARTICLE_TYPES.register(
                        "chorus_leaves",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CHORUS_BLOSSOM_LEAVES = PARTICLE_TYPES
                        .register("chorus_blossom_leaves",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GOD_RAY = PARTICLE_TYPES.register(
                        "god_ray",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STRETCHED_GOD_RAY = PARTICLE_TYPES
                        .register("stretched_god_ray",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WATER_STREAM = PARTICLE_TYPES.register(
                        "water_stream",
                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOSSOM_THORN_INDICATOR = PARTICLE_TYPES
                        .register("blossom_thorn_indicator",
                                        () -> new SimpleParticleType(true));

        public static final DeferredHolder<ParticleType<?>, ParticleType<SparkleParticleOption>> SPARKLE = PARTICLE_TYPES
                        .register("sparkle",
                                        () -> new GoetyParticleType<>(false, SparkleParticleOption.CODEC,
                                                        SparkleParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<DustCloudParticleOption>> DUST_CLOUD = PARTICLE_TYPES
                        .register("dust_cloud",
                                        () -> new GoetyParticleType<>(false, DustCloudParticleOption.CODEC,
                                                        DustCloudParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<BlockParticleOption>> FAST_DUST = PARTICLE_TYPES
                        .register("fast_dust",
                                        () -> new ParticleType<>(false) {
                                                @Override
                                                public MapCodec<BlockParticleOption> codec() {
                                                        return BlockParticleOption.codec(this);
                                                }

                                                @Override
                                                public StreamCodec<? super RegistryFriendlyByteBuf, BlockParticleOption> streamCodec() {
                                                        return BlockParticleOption.streamCodec(this);
                                                }
                                        });

        public static final DeferredHolder<ParticleType<?>, ParticleType<ShockwaveParticleOption>> SHOCKWAVE = PARTICLE_TYPES
                        .register("shockwave",
                                        () -> new GoetyParticleType<>(false, ShockwaveParticleOption.CODEC,
                                                        ShockwaveParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<ShockwaveParticleOption>> REVERSE_SHOCKWAVE = PARTICLE_TYPES
                        .register("reverse_shockwave",
                                        () -> new GoetyParticleType<>(false, ShockwaveParticleOption.CODEC,
                                                        ShockwaveParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<ShockwaveParticleOption>> LICH_DEATH = PARTICLE_TYPES
                        .register("lich_death",
                                        () -> new GoetyParticleType<>(false, ShockwaveParticleOption.CODEC,
                                                        ShockwaveParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<CircleExplodeParticleOption>> CIRCLE_EXPLODE = PARTICLE_TYPES
                        .register("circle_explode",
                                        () -> new GoetyParticleType<>(false, CircleExplodeParticleOption.CODEC,
                                                        CircleExplodeParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<AoEParticleOption>> AOE_INDICATOR = PARTICLE_TYPES
                        .register("aoe_indicator",
                                        () -> new GoetyParticleType<>(false, AoEParticleOption.CODEC,
                                                        AoEParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<FoggyCloudParticleOption>> FOG_CLOUD = PARTICLE_TYPES
                        .register("fog_cloud",
                                        () -> new GoetyParticleType<>(false, FoggyCloudParticleOption.CODEC,
                                                        FoggyCloudParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<PulsatingCircleParticleOption>> MINE_PULSE = PARTICLE_TYPES
                        .register("mine_pulse",
                                        () -> new GoetyParticleType<>(false, PulsatingCircleParticleOption.CODEC,
                                                        PulsatingCircleParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<RisingCircleParticleOption>> SOUL_HEAL = PARTICLE_TYPES
                        .register("soul_heal",
                                        () -> new GoetyParticleType<>(false, RisingCircleParticleOption.CODEC,
                                                        RisingCircleParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<ModShriekParticleOption>> MOD_SHRIEK = PARTICLE_TYPES
                        .register("mod_shriek",
                                        () -> new GoetyParticleType<>(false, ModShriekParticleOption.CODEC,
                                                        ModShriekParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<SculkBubbleParticleOption>> SCULK_BUBBLE = PARTICLE_TYPES
                        .register("sculk_bubble",
                                        () -> new GoetyParticleType<>(false, SculkBubbleParticleOption.CODEC,
                                                        SculkBubbleParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<WindParticleOption>> WIND = PARTICLE_TYPES
                        .register("wind",
                                        () -> new GoetyParticleType<>(false, WindParticleOption.CODEC,
                                                        WindParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<WindBlowParticle.Option>> WIND_BLOW = PARTICLE_TYPES
                        .register("wind_blow",
                                        () -> new GoetyParticleType<>(false, WindBlowParticle.Option.CODEC,
                                                        WindBlowParticle.Option.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<WindShockwaveParticle.Option>> WIND_SHOCKWAVE = PARTICLE_TYPES
                        .register("wind_shockwave",
                                        () -> new GoetyParticleType<>(false, WindShockwaveParticle.Option.CODEC,
                                                        WindShockwaveParticle.Option.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<GatherTrailParticle.Option>> GATHER_TRAIL = PARTICLE_TYPES
                        .register("gather_trail",
                                        () -> new GoetyParticleType<>(false, GatherTrailParticle.Option.CODEC,
                                                        GatherTrailParticle.Option.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<GatherFrostParticle.Option>> FROST_GATHER = PARTICLE_TYPES
                        .register("frost_gather",
                                        () -> new GoetyParticleType<>(false, GatherFrostParticle.Option.CODEC,
                                                        GatherFrostParticle.Option.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<AuraParticle.Option>> AURA = PARTICLE_TYPES
                        .register("aura",
                                        () -> new GoetyParticleType<>(false, AuraParticle.Option.CODEC,
                                                        AuraParticle.Option.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<GroundAuraParticle.Option>> GROUND_AURA = PARTICLE_TYPES
                        .register("ground_aura",
                                        () -> new GoetyParticleType<>(false, GroundAuraParticle.Option.CODEC,
                                                        GroundAuraParticle.Option.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<VerticalCircleExplodeParticleOption>> VERTICAL_CIRCLE_EXPLODE = PARTICLE_TYPES
                        .register("vertical_circle_explode",
                                        () -> new GoetyParticleType<>(false, VerticalCircleExplodeParticleOption.CODEC,
                                                        VerticalCircleExplodeParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<AbsorbTrailParticleOption>> ABSORB_TRAIL = PARTICLE_TYPES
                        .register("absorb_trail",
                                        () -> new GoetyParticleType<>(false, AbsorbTrailParticleOption.CODEC,
                                                        AbsorbTrailParticleOption.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<MagicSmokeParticle.Option>> MAGIC_SMOKE = PARTICLE_TYPES
                        .register("magic_smoke",
                                        () -> new GoetyParticleType<>(false, MagicSmokeParticle.Option.CODEC,
                                                        MagicSmokeParticle.Option.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<MagicAshSmokeParticle.Option>> MAGIC_ASH_SMOKE = PARTICLE_TYPES
                        .register("magic_ash_smoke",
                                        () -> new GoetyParticleType<>(false, MagicAshSmokeParticle.Option.CODEC,
                                                        MagicAshSmokeParticle.Option.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<FollowFireParticle.Option>> FOLLOW_CULT_SPELL = PARTICLE_TYPES
                        .register("follow_cult_spell",
                                        () -> new GoetyParticleType<>(false, FollowFireParticle.Option.CODEC,
                                                        FollowFireParticle.Option.STREAM_CODEC));

        public static final DeferredHolder<ParticleType<?>, ParticleType<SpirallingParticleOption>> SPIRALLING = PARTICLE_TYPES
                        .register("spiralling",
                                        () -> new GoetyParticleType<>(false, SpirallingParticleOption.CODEC,
                                                        SpirallingParticleOption.STREAM_CODEC));
}
