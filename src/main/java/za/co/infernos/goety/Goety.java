package za.co.infernos.goety;

import java.util.function.Supplier;
import za.co.infernos.goety.client.ClientProxy;
import za.co.infernos.goety.client.inventory.container.ModContainerType;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.CommonProxy;
import za.co.infernos.goety.common.advancements.ModCriteriaTriggers;
import za.co.infernos.goety.common.blocks.*;
import za.co.infernos.goety.common.blocks.entities.BrewCauldronBlockEntity;
import za.co.infernos.goety.common.blocks.entities.ModBlockEntities;
import za.co.infernos.goety.common.blocks.fluids.ModFluids;
import za.co.infernos.goety.common.crafting.ModRecipeSerializer;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.effects.BrewMobEffect;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.*;
import za.co.infernos.goety.common.entities.ally.ender.BlastlingServant;
import za.co.infernos.goety.common.entities.ally.ender.SnarelingServant;
import za.co.infernos.goety.common.entities.ally.ender.WatchlingServant;
import za.co.infernos.goety.common.entities.ally.golem.*;
import za.co.infernos.goety.common.entities.ally.illager.*;
import za.co.infernos.goety.common.entities.ally.spider.*;
import za.co.infernos.goety.common.entities.ally.undead.*;
import za.co.infernos.goety.common.entities.ally.undead.bound.*;
import za.co.infernos.goety.common.entities.ally.undead.skeleton.*;
import za.co.infernos.goety.common.entities.ally.undead.zombie.*;
import za.co.infernos.goety.common.entities.boss.Apostle;
import za.co.infernos.goety.common.entities.boss.EnderKeeper;
import za.co.infernos.goety.common.entities.boss.Vizier;
import za.co.infernos.goety.common.entities.deco.HauntedArmorStand;
import za.co.infernos.goety.common.entities.hostile.*;
import za.co.infernos.goety.common.entities.hostile.cultists.Crone;
import za.co.infernos.goety.common.entities.hostile.cultists.Heretic;
import za.co.infernos.goety.common.entities.hostile.cultists.Maverick;
import za.co.infernos.goety.common.entities.hostile.cultists.Warlock;
import za.co.infernos.goety.common.entities.hostile.ender.Blastling;
import za.co.infernos.goety.common.entities.hostile.ender.Endersent;
import za.co.infernos.goety.common.entities.hostile.ender.Snareling;
import za.co.infernos.goety.common.entities.hostile.ender.Watchling;
import za.co.infernos.goety.common.entities.hostile.illagers.*;
import za.co.infernos.goety.common.entities.hostile.servants.*;
import za.co.infernos.goety.common.entities.neutral.*;
import za.co.infernos.goety.common.entities.projectiles.*;
import za.co.infernos.goety.common.inventory.ModSaveInventory;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.ModPotions;
import za.co.infernos.goety.common.items.ModSpawnEggs;
import za.co.infernos.goety.common.items.ServantSpawnEggs;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.ritual.ModRituals;
import za.co.infernos.goety.common.world.ModMobSpawnBiomeModifier;
import za.co.infernos.goety.common.world.ModMobSpawnStructureModifier;
import za.co.infernos.goety.common.world.features.ModFeatures;
import za.co.infernos.goety.common.world.features.trees.trunkplacers.ModTrunkPlacerTypes;
import za.co.infernos.goety.common.world.placements.ModPlacementType;
import za.co.infernos.goety.common.world.processors.ModProcessors;
import za.co.infernos.goety.common.world.structures.ModStructureTypes;
import za.co.infernos.goety.compat.OtherModCompat;
import za.co.infernos.goety.compat.fml.FMLJavaModLoadingContext;
import za.co.infernos.goety.config.*;
import za.co.infernos.goety.init.*;
import za.co.infernos.goety.init.ModAttachments;
import za.co.infernos.goety.mixin.AxeItemAccessor;
import za.co.infernos.goety.mixin.FireBlockAccessor;
import za.co.infernos.goety.utils.ModPotionUtil;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.StructureModifier;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import za.co.infernos.goety.compat.legacy.neoforge.registries.RegistryObject;
import org.slf4j.Logger;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.neoforged.fml.loading.LogMarkers.CORE;

@Mod(Goety.MOD_ID)
public class Goety {
        public static final String MOD_ID = "goety";
        public static final Logger LOGGER = LogUtils.getLogger();
        public static ModProxy PROXY = createProxy();
        public static SidedInit SIDED_INIT = createSidedInit();

        private static ModProxy createProxy() {
                return net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT
                                ? new ClientProxy()
                                : new CommonProxy();
        }

        private static SidedInit createSidedInit() {
                return net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT
                                ? new za.co.infernos.goety.init.ClientSideInit()
                                : new SidedInit();
        }

        public static ResourceLocation location(String path) {
                return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
        }

        public Goety(IEventBus modEventBus) {
                FMLJavaModLoadingContext.setModEventBus(modEventBus);

                // Register in order: fluids -> blocks -> block entities -> items
                // This ensures dependencies are available when needed
                ModFluids.FLUID_TYPES.register(modEventBus);
                ModFluids.FLUIDS.register(modEventBus);
                ModBlocks.BLOCKS.register(modEventBus);
                ModBlockEntities.BLOCK_ENTITY.register(modEventBus);
                ModEntityType.ENTITY_TYPE.register(modEventBus);
                ModFeatures.FEATURES.register(modEventBus);
                ModTrunkPlacerTypes.TRUNK_PLACER_TYPES.register(modEventBus);
                ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
                ModContainerType.CONTAINER_TYPE.register(modEventBus);
                ModEnchantments.ENCHANTMENTS.register(modEventBus);
                ModLootModifier.GLOBAL_LOOT_MODIFIER.register(modEventBus);
                ModRituals.RITUALS.register(modEventBus);
                ModStructureTypes.STRUCTURE_TYPE.register(modEventBus);
                ModPlacementType.STRUCTURE_PLACEMENT_TYPE.register(modEventBus);
                ModProcessors.STRUCTURE_PROCESSOR.register(modEventBus);
                ModCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
                ModAttachments.init(modEventBus);
                ModRecipeSerializer.init();

                modEventBus.addListener(this::registerCriteriaTriggers);
                modEventBus.addListener(this::commonSetup);
                modEventBus.addListener(this::setupEntityAttributeCreation);
                modEventBus.addListener(this::SpawnPlacementEvent);
                modEventBus.addListener(this::enqueueIMC);
                modEventBus.addListener(EventPriority.LOWEST, this::finalLoad);
                modEventBus.addListener(ModNetwork::registerPayloadHandlers);

                net.neoforged.fml.ModContainer container = ModLoadingContext.get().getActiveContainer();
                container.registerConfig(ModConfig.Type.COMMON, za.co.infernos.goety.config.MainConfig.SPEC, "goety/main.toml");
                container.registerConfig(ModConfig.Type.COMMON, za.co.infernos.goety.config.MobsConfig.SPEC, "goety/mobs.toml");
                container.registerConfig(ModConfig.Type.COMMON, za.co.infernos.goety.config.SpellConfig.SPEC, "goety/spells.toml");
                container.registerConfig(ModConfig.Type.COMMON, za.co.infernos.goety.config.ItemConfig.SPEC, "goety/items.toml");
                container.registerConfig(ModConfig.Type.COMMON, za.co.infernos.goety.config.BrewConfig.SPEC, "goety/brews.toml");
                container.registerConfig(ModConfig.Type.COMMON, za.co.infernos.goety.config.AttributesConfig.SPEC, "goety/attributes.toml");

                final DeferredRegister<MapCodec<? extends BiomeModifier>> biomeModifiers = DeferredRegister
                                .create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Goety.MOD_ID);
                biomeModifiers.register(modEventBus);
                var mobSpawnsSerializer = biomeModifiers.register("mob_spawns", ModMobSpawnBiomeModifier::makeCodec);
                LOGGER.info("Goety: Registered biome modifier serializer 'goety:mob_spawns' = {}", mobSpawnsSerializer.getId());
                final DeferredRegister<MapCodec<? extends StructureModifier>> structureModifiers = DeferredRegister
                                .create(NeoForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, Goety.MOD_ID);
                structureModifiers.register(modEventBus);
                structureModifiers.register("mob_structure_spawns", ModMobSpawnStructureModifier::makeCodec);

                NeoForge.EVENT_BUS.register(this);
                ModItems.init();
                ModAttributes.init();
                ModBlocks.init();
                // ModFluids.init();
                ModSpawnEggs.init();
                ServantSpawnEggs.init();
                GoetyEffects.init();
                ModPotions.init();
                ModPaintings.init();
                ModPotPatterns.init();
                ModBanners.init();
                ModSounds.init();
                SIDED_INIT.init();
        }

        public static Path getOrCreateDirectory(Path dirPath, String dirLabel) {
                if (!Files.isDirectory(dirPath.getParent())) {
                        getOrCreateDirectory(dirPath.getParent(), "parent of " + dirLabel);
                }
                if (!Files.isDirectory(dirPath)) {
                        LOGGER.debug(CORE, "Making {} directory : {}", dirLabel, dirPath);
                        try {
                                Files.createDirectory(dirPath);
                        } catch (IOException e) {
                                if (e instanceof FileAlreadyExistsException) {
                                        LOGGER.error(CORE, "Failed to create {} directory - there is a file in the way",
                                                        dirLabel);
                                } else {
                                        LOGGER.error(CORE, "Problem with creating {} directory (Permissions?)",
                                                        dirLabel, e);
                                }
                                throw new RuntimeException("Problem creating directory", e);
                        }
                        LOGGER.debug(CORE, "Created {} directory : {}", dirLabel, dirPath);
                } else {
                        LOGGER.debug(CORE, "Found existing {} directory : {}", dirLabel, dirPath);
                }
                return dirPath;
        }

        private void registerCriteriaTriggers(final net.neoforged.neoforge.registries.RegisterEvent event) {
                ModCriteriaTriggers.register(event);
        }

        @FunctionalInterface
        private interface ProjectileFactory {
                Projectile create(Level level, Position pos, ItemStack stack);
        }

        private static DispenseItemBehavior projectileDispense(ProjectileFactory factory, float power, float uncertainty) {
                return new DefaultDispenseItemBehavior() {
                        @Override
                        protected ItemStack execute(BlockSource source, ItemStack stack) {
                                Level level = source.level();
                                Direction facing = source.state().getValue(DispenserBlock.FACING);
                                Position pos = DispenserBlock.getDispensePosition(source);
                                Projectile projectile = factory.create(level, pos, stack);
                                projectile.shoot(facing.getStepX(), facing.getStepY() + 0.1F, facing.getStepZ(), power, uncertainty);
                                level.addFreshEntity(projectile);
                                stack.shrink(1);
                                return stack;
                        }

                        @Override
                        protected void playSound(BlockSource source) {
                                source.level().levelEvent(1002, source.pos(), 0);
                        }
                };
        }

        private void commonSetup(final FMLCommonSetupEvent event) {
                // Update effect curable values from configs now that configs are loaded
                event.enqueueWork(() -> {
                        if (GoetyEffects.PRESSURE.get() instanceof BrewMobEffect pressure) {
                                pressure.curable = za.co.infernos.goety.utils.ConfigHelper.getBoolean(BrewConfig.PressureCurable, false);
                        }
                        if (GoetyEffects.NYCTOPHOBIA.get() instanceof BrewMobEffect nyctophobia) {
                                nyctophobia.curable = za.co.infernos.goety.utils.ConfigHelper.getBoolean(BrewConfig.NyctophobiaCurable, false);
                        }
                        if (GoetyEffects.SUN_ALLERGY.get() instanceof BrewMobEffect sunAllergy) {
                                sunAllergy.curable = za.co.infernos.goety.utils.ConfigHelper.getBoolean(BrewConfig.SunAllergyCurable, false);
                        }
                        if (GoetyEffects.SNOW_SKIN.get() instanceof BrewMobEffect snowSkin) {
                                snowSkin.curable = za.co.infernos.goety.utils.ConfigHelper.getBoolean(BrewConfig.SnowSkinCurable, false);
                        }
                        if (GoetyEffects.EVIL_EYE.get() instanceof za.co.infernos.goety.common.effects.EvilEyeEffect evilEye) {
                                evilEye.curable = za.co.infernos.goety.utils.ConfigHelper.getBoolean(BrewConfig.EvilEyeCurable, false);
                        }
                });
                
                OtherModCompat.setup(event);
                event.enqueueWork(() -> {
                        ModCauldronInteraction.init();
                        DispenserBlock.registerBehavior(ModBlocks.TALL_SKULL_ITEM.get(),
                                        new OptionalDispenseItemBehavior() {
                                                protected ItemStack execute(BlockSource source, ItemStack stack) {
                                                        this.setSuccess(ArmorItem.dispenseArmor(source, stack));
                                                        return stack;
                                                }
                                        });
                        DispenserBlock.registerBehavior(ModItems.OMINOUS_SADDLE.get(),
                                        new OptionalDispenseItemBehavior() {
                                                protected ItemStack execute(BlockSource source, ItemStack stack) {
                                                        boolean flag = false;
                                                        BlockPos blockpos = source.pos()
                                                                        .relative(source.state().getValue(
                                                                                        DispenserBlock.FACING));
                                                        List<LivingEntity> list = source.level().getEntitiesOfClass(
                                                                        LivingEntity.class,
                                                                        new AABB(blockpos),
                                                                        EntitySelector.NO_SPECTATORS);
                                                        if (!list.isEmpty()) {
                                                                LivingEntity livingentity = list.get(0);
                                                                if (livingentity instanceof ModRavager ravager) {
                                                                        if (!ravager.hasSaddle()) {
                                                                                ravager.equipSaddle(true);
                                                                                flag = true;
                                                                        }
                                                                }
                                                        }
                                                        this.setSuccess(flag);
                                                        return stack;
                                                }
                                        });
                        DispenserBlock.registerBehavior(ModItems.ILL_BOMB.get(),
                                        projectileDispense((level, pos, stack) -> new IllBomb(pos.x(), pos.y(), pos.z(), level), 1.1F, 6.0F));
                        DispenserBlock.registerBehavior(ModItems.SNAP_FUNGUS.get(),
                                        projectileDispense((level, pos, stack) -> new SnapFungus(pos.x(), pos.y(), pos.z(), level), 1.1F, 6.0F));
                        DispenserBlock.registerBehavior(ModItems.BLAST_FUNGUS.get(),
                                        projectileDispense((level, pos, stack) -> new BlastFungus(pos.x(), pos.y(), pos.z(), level), 1.1F, 6.0F));
                        DispenserBlock.registerBehavior(ModItems.BERSERK_FUNGUS.get(),
                                        projectileDispense((level, pos, stack) -> new BerserkFungus(pos.x(), pos.y(), pos.z(), level), 1.1F, 6.0F));
                        DispenserBlock.registerBehavior(ModItems.SPLASH_BREW.get(),
                                        projectileDispense((level, pos, stack) -> Util.make(new ThrownBrew(level, pos.x(), pos.y(), pos.z()), b -> b.setItem(stack)), 1.375F, 3.0F));
                        DispenserBlock.registerBehavior(ModItems.LINGERING_BREW.get(),
                                        projectileDispense((level, pos, stack) -> Util.make(new ThrownBrew(level, pos.x(), pos.y(), pos.z()), b -> b.setItem(stack)), 1.375F, 3.0F));
                        DispenserBlock.registerBehavior(ModItems.GAS_BREW.get(),
                                        projectileDispense((level, pos, stack) -> Util.make(new ThrownBrew(level, pos.x(), pos.y(), pos.z()), b -> b.setItem(stack)), 1.375F, 3.0F));
                        DispenserBlock.registerBehavior(ModItems.HAUNTED_ARMOR_STAND.get(),
                                        new DefaultDispenseItemBehavior() {
                                                public ItemStack execute(BlockSource p_123461_, ItemStack p_123462_) {
                                                        Direction direction = p_123461_.state()
                                                                        .getValue(DispenserBlock.FACING);
                                                        BlockPos blockpos = p_123461_.pos().relative(direction);
                                                        Level level = p_123461_.level();
                                                        HauntedArmorStand armorstand = new HauntedArmorStand(level,
                                                                        (double) blockpos.getX() + 0.5D,
                                                                        (double) blockpos.getY(),
                                                                        (double) blockpos.getZ() + 0.5D);
                                                        EntityType.updateCustomEntityTag(level, (Player) null,
                                                                        armorstand,
                                                                        p_123462_.getOrDefault(
                                                                                        DataComponents.CUSTOM_DATA,
                                                                                        CustomData.EMPTY));
                                                        armorstand.setYRot(direction.toYRot());
                                                        level.addFreshEntity(armorstand);
                                                        p_123462_.shrink(1);
                                                        return p_123462_;
                                                }
                                        });
                        DispenserBlock.registerBehavior(ModItems.QUICK_GROWING_SEED.get(),
                                        new DefaultDispenseItemBehavior() {
                                                public ItemStack execute(BlockSource p_123461_, ItemStack p_123462_) {
                                                        Direction direction = p_123461_.state()
                                                                        .getValue(DispenserBlock.FACING);
                                                        BlockPos blockpos = p_123461_.pos().relative(direction);
                                                        Level level = p_123461_.level();
                                                        AbstractVine vine = ModEntityType.QUICK_GROWING_VINE.get()
                                                                        .create(level);
                                                        if (vine != null) {
                                                                EntityType<?> entityType = vine.getVariant(null, level,
                                                                                blockpos);
                                                                if (entityType != null) {
                                                                        vine = (AbstractVine) entityType.create(level);
                                                                }
                                                                if (vine != null) {
                                                                        Vec3 vec3 = new Vec3(
                                                                                        (double) blockpos.getX() + 0.5D,
                                                                                        (double) blockpos.getY(),
                                                                                        (double) blockpos.getZ()
                                                                                                        + 0.5D);
                                                                        vine.setPos(vec3);
                                                                        if (level instanceof ServerLevel serverLevel) {
                                                                                vine.finalizeSpawn(serverLevel,
                                                                                                serverLevel.getCurrentDifficultyAt(
                                                                                                                blockpos),
                                                                                                MobSpawnType.MOB_SUMMONED,
                                                                                                null);
                                                                        }
                                                                        vine.setPerpetual(true);
                                                                        if (level.addFreshEntity(vine)) {
                                                                                p_123462_.shrink(1);
                                                                        }
                                                                }
                                                        }
                                                        return p_123462_;
                                                }
                                        });
                        DispenserBlock.registerBehavior(ModItems.POISON_QUILL_SEED.get(),
                                        new DefaultDispenseItemBehavior() {
                                                public ItemStack execute(BlockSource p_123461_, ItemStack p_123462_) {
                                                        Direction direction = p_123461_.state()
                                                                        .getValue(DispenserBlock.FACING);
                                                        BlockPos blockpos = p_123461_.pos().relative(direction);
                                                        Level level = p_123461_.level();
                                                        AbstractVine vine = ModEntityType.POISON_QUILL_VINE.get()
                                                                        .create(level);
                                                        if (vine != null) {
                                                                EntityType<?> entityType = vine.getVariant(null, level,
                                                                                blockpos);
                                                                if (entityType != null) {
                                                                        vine = (AbstractVine) entityType.create(level);
                                                                }
                                                                if (vine != null) {
                                                                        Vec3 vec3 = new Vec3(
                                                                                        (double) blockpos.getX() + 0.5D,
                                                                                        (double) blockpos.getY(),
                                                                                        (double) blockpos.getZ()
                                                                                                        + 0.5D);
                                                                        vine.setPos(vec3);
                                                                        if (level instanceof ServerLevel serverLevel) {
                                                                                vine.finalizeSpawn(serverLevel,
                                                                                                serverLevel.getCurrentDifficultyAt(
                                                                                                                blockpos),
                                                                                                MobSpawnType.MOB_SUMMONED,
                                                                                                null);
                                                                        }
                                                                        vine.setPerpetual(true);
                                                                        if (level.addFreshEntity(vine)) {
                                                                                p_123462_.shrink(1);
                                                                        }
                                                                }
                                                        }
                                                        return p_123462_;
                                                }
                                        });
                        DispenserBlock.registerBehavior(ModItems.VOID_BUCKET.get(), new DefaultDispenseItemBehavior() {
                                private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

                                public ItemStack execute(BlockSource p_123561_, ItemStack p_123562_) {
                                        DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem) p_123562_
                                                        .getItem();
                                        BlockPos blockpos = p_123561_.pos()
                                                        .relative(p_123561_.state().getValue(DispenserBlock.FACING));
                                        Level level = p_123561_.level();
                                        if (dispensiblecontaineritem.emptyContents((Player) null, level, blockpos,
                                                        (BlockHitResult) null,
                                                        p_123562_)) {
                                                dispensiblecontaineritem.checkExtraContent((Player) null, level,
                                                                p_123562_, blockpos);
                                                return new ItemStack(Items.BUCKET);
                                        } else {
                                                return this.defaultDispenseItemBehavior.dispense(p_123561_, p_123562_);
                                        }
                                }
                        });
                        ModDispenserRegister
                                        .registerAlternativeDispenseBehavior(
                                                        new ModDispenserRegister.AlternativeDispenseBehavior(
                                                                        Goety.MOD_ID, Items.WATER_BUCKET,
                                                                        (blockSource, itemStack) -> blockSource.level()
                                                                                        .getBlockState(ModDispenserRegister
                                                                                                        .offsetPos(blockSource))
                                                                                        .is(ModBlocks.BREWING_CAULDRON
                                                                                                        .get()),
                                                                        new OptionalDispenseItemBehavior() {
                                                                                protected ItemStack execute(
                                                                                                BlockSource source,
                                                                                                ItemStack stack) {
                                                                                        BlockPos blockpos = source.pos()
                                                                                                        .relative(source.state()
                                                                                                                        .getValue(DispenserBlock.FACING));
                                                                                        BlockState blockState = source
                                                                                                        .level()
                                                                                                        .getBlockState(blockpos);
                                                                                        if (blockState.is(
                                                                                                        ModBlocks.BREWING_CAULDRON
                                                                                                                        .get())) {
                                                                                                if (blockState.getValue(
                                                                                                                BrewCauldronBlock.LEVEL) < 3) {
                                                                                                        this.setSuccess(source
                                                                                                                        .level()
                                                                                                                        .setBlockAndUpdate(
                                                                                                                                        blockpos,
                                                                                                                                        blockState.setValue(
                                                                                                                                                        BrewCauldronBlock.LEVEL,
                                                                                                                                                        3)));
                                                                                                        return new ItemStack(
                                                                                                                        Items.BUCKET);
                                                                                                }
                                                                                        }
                                                                                        return stack;
                                                                                }
                                                                        }));
                        ModDispenserRegister
                                        .registerAlternativeDispenseBehavior(
                                                        new ModDispenserRegister.AlternativeDispenseBehavior(
                                                                        Goety.MOD_ID, Items.BUCKET,
                                                                        (blockSource, itemStack) -> blockSource.level()
                                                                                        .getBlockState(ModDispenserRegister
                                                                                                        .offsetPos(blockSource))
                                                                                        .is(ModBlocks.BREWING_CAULDRON
                                                                                                        .get()),
                                                                        new OptionalDispenseItemBehavior() {
                                                                                protected ItemStack execute(
                                                                                                BlockSource source,
                                                                                                ItemStack stack) {
                                                                                        BlockPos blockpos = source.pos()
                                                                                                        .relative(source.state()
                                                                                                                        .getValue(DispenserBlock.FACING));
                                                                                        BlockState blockState = source
                                                                                                        .level()
                                                                                                        .getBlockState(blockpos);
                                                                                        if (blockState.is(
                                                                                                        ModBlocks.BREWING_CAULDRON
                                                                                                                        .get())) {
                                                                                                if (blockState.getValue(
                                                                                                                BrewCauldronBlock.LEVEL) == 3) {
                                                                                                        if (source.level()
                                                                                                                        .getBlockEntity(
                                                                                                                                        blockpos) instanceof BrewCauldronBlockEntity blockEntity) {
                                                                                                                blockEntity.reset();
                                                                                                        }
                                                                                                        this.setSuccess(source
                                                                                                                        .level()
                                                                                                                        .setBlockAndUpdate(
                                                                                                                                        blockpos,
                                                                                                                                        blockState.setValue(
                                                                                                                                                        BrewCauldronBlock.LEVEL,
                                                                                                                                                        0)));
                                                                                                        return new ItemStack(
                                                                                                                        Items.WATER_BUCKET);
                                                                                                }
                                                                                        }
                                                                                        return stack;
                                                                                }
                                                                        }));
                        java.util.Map<Block, Block> strippables = Maps.newHashMap(AxeItemAccessor.getStrippables());
                        strippables.put(ModBlocks.HAUNTED_LOG.get(), ModBlocks.STRIPPED_HAUNTED_LOG.get());
                        strippables.put(ModBlocks.HAUNTED_WOOD.get(), ModBlocks.STRIPPED_HAUNTED_WOOD.get());
                        strippables.put(ModBlocks.ROTTEN_LOG.get(), ModBlocks.STRIPPED_ROTTEN_LOG.get());
                        strippables.put(ModBlocks.ROTTEN_WOOD.get(), ModBlocks.STRIPPED_ROTTEN_WOOD.get());
                        strippables.put(ModBlocks.WINDSWEPT_LOG.get(), ModBlocks.STRIPPED_WINDSWEPT_LOG.get());
                        strippables.put(ModBlocks.WINDSWEPT_WOOD.get(), ModBlocks.STRIPPED_WINDSWEPT_WOOD.get());
                        strippables.put(ModBlocks.PINE_LOG.get(), ModBlocks.STRIPPED_PINE_LOG.get());
                        strippables.put(ModBlocks.PINE_WOOD.get(), ModBlocks.STRIPPED_PINE_WOOD.get());
                        AxeItemAccessor.setStrippables(strippables);
                        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.CHORUS_STALK.getId(),
                                        ModBlocks.POTTED_CHORUS_STALK);
                        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.CHORUS_FERN.getId(),
                                        ModBlocks.POTTED_CHORUS_FERN);
                        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.HAUNTED_SAPLING.getId(),
                                        ModBlocks.POTTED_HAUNTED_SAPLING);
                        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.ROTTEN_SAPLING.getId(),
                                        ModBlocks.POTTED_ROTTEN_SAPLING);
                        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.WINDSWEPT_SAPLING.getId(),
                                        ModBlocks.POTTED_WINDSWEPT_SAPLING);
                        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.PINE_SAPLING.getId(),
                                        ModBlocks.POTTED_PINE_SAPLING);
                        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.CHORUS_SAPLING.getId(),
                                        ModBlocks.POTTED_CHORUS_SAPLING);
                        WoodType.register(ModWoodType.PINE);
                        ModPotPatterns.addPatterns();

                        FireBlockAccessor fireBlockAccessor = (FireBlockAccessor) Blocks.FIRE;

                        Collection<Block> blocks = new ArrayList<>();
                        ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).forEach(block -> {
                                if (block.defaultBlockState().ignitedByLava()) {
                                        blocks.add(block);
                                }
                        });
                        for (Block block : blocks) {
                                if (block.defaultBlockState().ignitedByLava()) {
                                        if (!(block instanceof PressurePlateBlock)
                                                        && !(block instanceof AbstractChestBlock<?>)
                                                        && !(block instanceof DoorBlock)
                                                        && !(block instanceof SignBlock)
                                                        && !(block instanceof SpiderNestBlock)
                                                        && !(block instanceof WitchPoleBlock)) {
                                                fireBlockAccessor.callSetFlammable(block, 5, 20);
                                        }
                                }
                        }
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.END_GROWTH_VINES.get().asItem(), 0.3F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.HAUNTED_SAPLING.get().asItem(), 0.3F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.WINDSWEPT_SAPLING.get().asItem(), 0.3F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.PINE_SAPLING.get().asItem(), 0.3F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.WINDSWEPT_LEAVES.get().asItem(), 0.3F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.PINE_LEAVES.get().asItem(), 0.3F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_VINE.get().asItem(), 0.3F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_BLOSSOM_VINES_PRUNED.get().asItem(), 0.3F);
                        ComposterBlock.COMPOSTABLES.put(ModItems.SNAP_FUNGUS.get(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.ROTTEN_SAPLING.get().asItem(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_SAPLING.get().asItem(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_SPROUT.get().asItem(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.END_GRASS_SPROUT.get().asItem(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_FERN_SPROUT.get().asItem(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.ROTTEN_LEAVES.get().asItem(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_LEAVES.get().asItem(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_BLOSSOM_LEAVES.get().asItem(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_STALK.get().asItem(), 0.85F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.END_GRASS.get().asItem(), 0.85F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_TALL_GRASS.get().asItem(), 0.85F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_FERN.get().asItem(), 0.85F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.LARGE_CHORUS_STALK.get().asItem(), 1.0F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.TALL_END_GRASS.get().asItem(), 1.0F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.LARGE_CHORUS_FERN.get().asItem(), 1.0F);
                        ComposterBlock.COMPOSTABLES.put(ModBlocks.CHORUS_BLOSSOM_VINES.get().asItem(), 0.65F);
                        ComposterBlock.COMPOSTABLES.put(ModItems.CHORUS_GROWTH.get(), 1.0F);
                        ComposterBlock.COMPOSTABLES.put(ModItems.BLAST_FUNGUS.get(), 1.0F);
                        ComposterBlock.COMPOSTABLES.put(ModItems.BERSERK_FUNGUS.get(), 1.0F);
                        ComposterBlock.COMPOSTABLES.put(ModItems.QUICK_GROWING_SEED.get(), 1.0F);
                        ComposterBlock.COMPOSTABLES.put(ModItems.POISON_QUILL_SEED.get(), 1.0F);
                });
        }


        private void finalLoad(FMLLoadCompleteEvent event) {
                event.enqueueWork(() -> {
                        ModDispenserRegister.getSortedAlternativeDispenseBehaviors()
                                        .forEach(ModDispenserRegister.AlternativeDispenseBehavior::register);
                        ModFluids.interactionInit();
                });
        }

        private void registerBrewingRecipes(net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent event) {
                event.getBuilder().addRecipe(new ModPotionUtil(ModItems.SNAP_FUNGUS.get().getDefaultInstance(),
                                Ingredient.of(Items.LILY_OF_THE_VALLEY), new ItemStack(ModItems.BERSERK_FUNGUS.get())));
                event.getBuilder().addRecipe(new ModPotionUtil(ModPotionUtil.setPotion(Potions.AWKWARD),
                                Ingredient.of(ModItems.SPIDER_EGG.get()),
                                ModPotionUtil.setPotion(ModPotions.CLIMBING)));
                event.getBuilder().addRecipe(new ModPotionUtil(ModPotionUtil.setSplashPotion(Potions.AWKWARD),
                                Ingredient.of(ModItems.SPIDER_EGG.get()),
                                ModPotionUtil.setSplashPotion(ModPotions.CLIMBING)));
                event.getBuilder().addRecipe(new ModPotionUtil(ModPotionUtil.setSplashPotion(Potions.AWKWARD),
                                Ingredient.of(ModItems.SPIDER_EGG.get()),
                                ModPotionUtil.setLingeringPotion(ModPotions.CLIMBING)));
                event.getBuilder().addRecipe(new ModPotionUtil(ModPotionUtil.setPotion(ModPotions.CLIMBING),
                                Ingredient.of(Items.REDSTONE),
                                ModPotionUtil.setPotion(ModPotions.LONG_CLIMBING)));
                event.getBuilder().addRecipe(new ModPotionUtil(ModPotionUtil.setSplashPotion(ModPotions.CLIMBING),
                                Ingredient.of(Items.REDSTONE),
                                ModPotionUtil.setSplashPotion(ModPotions.LONG_CLIMBING)));
                event.getBuilder()
                                .addRecipe(new ModPotionUtil(
                                                ModPotionUtil.setLingeringPotion(ModPotions.CLIMBING),
                                                Ingredient.of(Items.REDSTONE),
                                                ModPotionUtil.setLingeringPotion(ModPotions.LONG_CLIMBING)));
                event.getBuilder().addRecipe(new ModPotionUtil(ModPotionUtil.setPotion(ModPotions.CLIMBING),
                                Ingredient.of(Items.GUNPOWDER),
                                ModPotionUtil.setSplashPotion(ModPotions.CLIMBING)));
                event.getBuilder().addRecipe(new ModPotionUtil(ModPotionUtil.setPotion(ModPotions.LONG_CLIMBING),
                                Ingredient.of(Items.GUNPOWDER),
                                ModPotionUtil.setSplashPotion(ModPotions.LONG_CLIMBING)));
                event.getBuilder().addRecipe(new ModPotionUtil(ModPotionUtil.setSplashPotion(ModPotions.CLIMBING),
                                Ingredient.of(Items.DRAGON_BREATH),
                                ModPotionUtil.setLingeringPotion(ModPotions.CLIMBING)));
                event.getBuilder()
                                .addRecipe(new ModPotionUtil(
                                                ModPotionUtil.setSplashPotion(ModPotions.LONG_CLIMBING),
                                                Ingredient.of(Items.DRAGON_BREATH),
                                                ModPotionUtil.setLingeringPotion(ModPotions.LONG_CLIMBING)));
        }

        private void setupEntityAttributeCreation(final EntityAttributeCreationEvent event) {
                event.put(ModEntityType.APOSTLE.get(), Apostle.setCustomAttributes().build());
                event.put(ModEntityType.OBSIDIAN_MONOLITH.get(), ObsidianMonolith.setCustomAttributes().build());
                event.put(ModEntityType.WARLOCK.get(), Warlock.setCustomAttributes().build());
                event.put(ModEntityType.WARTLING.get(), Wartling.setCustomAttributes().build());
                event.put(ModEntityType.HERETIC.get(), Heretic.setCustomAttributes().build());
                event.put(ModEntityType.MAVERICK.get(), Maverick.setCustomAttributes().build());
                event.put(ModEntityType.CRONE.get(), Crone.setCustomAttributes().build());
                event.put(ModEntityType.SKELETON_VILLAGER_SERVANT.get(),
                                SkeletonVillagerServant.setCustomAttributes().build());
                event.put(ModEntityType.ZPIGLIN_SERVANT.get(), ZPiglinServant.setCustomAttributes().build());
                event.put(ModEntityType.ZPIGLIN_BRUTE_SERVANT.get(), ZPiglinBruteServant.setCustomAttributes().build());
                event.put(ModEntityType.MALGHAST.get(), Malghast.setCustomAttributes().build());
                event.put(ModEntityType.INFERNO.get(), Inferno.setCustomAttributes().build());
                event.put(ModEntityType.DAMNED.get(), Damned.setCustomAttributes().build());
                event.put(ModEntityType.VAMPIRE_BAT.get(), VampireBat.setCustomAttributes().build());
                event.put(ModEntityType.HOSTILE_BLACK_WOLF.get(), HostileBlackWolf.setCustomAttributes().build());
                event.put(ModEntityType.REAPER.get(), Reaper.setCustomAttributes().build());
                event.put(ModEntityType.WRAITH.get(), Wraith.setCustomAttributes().build());
                event.put(ModEntityType.BORDER_WRAITH.get(), BorderWraith.setCustomAttributes().build());
                event.put(ModEntityType.MUCK_WRAITH.get(), MuckWraith.setCustomAttributes().build());
                event.put(ModEntityType.CRYPT_SLIME.get(), CryptSlime.setCustomAttributes().build());
                event.put(ModEntityType.WEB_SPIDER.get(), WebSpider.setCustomAttributes().build());
                event.put(ModEntityType.ICY_SPIDER.get(), IcySpider.setCustomAttributes().build());
                event.put(ModEntityType.BONE_SPIDER.get(), BoneSpider.setCustomAttributes().build());
                event.put(ModEntityType.BROOD_MOTHER.get(), AbstractBroodMother.setCustomAttributes().build());
                event.put(ModEntityType.NECROMANCER.get(), HostileNecromancer.setCustomAttributes().build());
                event.put(ModEntityType.CAIRN_NECROMANCER.get(), CairnNecromancer.setCustomAttributes().build());
                event.put(ModEntityType.MOSSY_NECROMANCER.get(), MossyNecromancer.setCustomAttributes().build());
                event.put(ModEntityType.HAUNTED_ARMOR.get(), HauntedArmor.setCustomAttributes().build());
                event.put(ModEntityType.WATCHLING.get(), Watchling.setCustomAttributes().build());
                event.put(ModEntityType.BLASTLING.get(), Blastling.setCustomAttributes().build());
                event.put(ModEntityType.SNARELING.get(), Snareling.setCustomAttributes().build());
                event.put(ModEntityType.ENDERSENT.get(), Endersent.setCustomAttributes().build());
                event.put(ModEntityType.ENDER_KEEPER.get(), EnderKeeper.setCustomAttributes().build());
                event.put(ModEntityType.VEX_SERVANT.get(), AllyVex.setCustomAttributes().build());
                event.put(ModEntityType.IRK_SERVANT.get(), AllyIrk.setCustomAttributes().build());
                event.put(ModEntityType.ZOMBIE_SERVANT.get(), ZombieServant.setCustomAttributes().build());
                event.put(ModEntityType.ZOMBIE_VILLAGER_SERVANT.get(),
                                ZombieVillagerServant.setCustomAttributes().build());
                event.put(ModEntityType.HUSK_SERVANT.get(), HuskServant.setCustomAttributes().build());
                event.put(ModEntityType.DROWNED_SERVANT.get(), DrownedServant.setCustomAttributes().build());
                event.put(ModEntityType.FROZEN_ZOMBIE_SERVANT.get(), FrozenZombieServant.setCustomAttributes().build());
                event.put(ModEntityType.JUNGLE_ZOMBIE_SERVANT.get(), JungleZombieServant.setCustomAttributes().build());
                event.put(ModEntityType.BLACKGUARD_SERVANT.get(), BlackguardServant.setCustomAttributes().build());
                event.put(ModEntityType.SKELETON_SERVANT.get(), SkeletonServant.setCustomAttributes().build());
                event.put(ModEntityType.STRAY_SERVANT.get(), StrayServant.setCustomAttributes().build());
                event.put(ModEntityType.WITHER_SKELETON_SERVANT.get(),
                                WitherSkeletonServant.setCustomAttributes().build());
                event.put(ModEntityType.MOSSY_SKELETON_SERVANT.get(),
                                MossySkeletonServant.setCustomAttributes().build());
                event.put(ModEntityType.SUNKEN_SKELETON_SERVANT.get(),
                                SunkenSkeletonServant.setCustomAttributes().build());
                event.put(ModEntityType.NECROMANCER_SERVANT.get(), NecromancerServant.setCustomAttributes().build());
                event.put(ModEntityType.CAIRN_NECROMANCER_SERVANT.get(),
                                CairnNecromancerServant.setCustomAttributes().build());
                event.put(ModEntityType.MOSSY_NECROMANCER_SERVANT.get(),
                                MossyNecromancerServant.setCustomAttributes().build());
                event.put(ModEntityType.DROWNED_NECROMANCER_SERVANT.get(),
                                DrownedNecromancer.setCustomAttributes().build());
                event.put(ModEntityType.WITHER_NECROMANCER_SERVANT.get(),
                                WitherNecromancerServant.setCustomAttributes().build());
                event.put(ModEntityType.REAPER_SERVANT.get(), ReaperServant.setCustomAttributes().build());
                event.put(ModEntityType.WRAITH_SERVANT.get(), WraithServant.setCustomAttributes().build());
                event.put(ModEntityType.BORDER_WRAITH_SERVANT.get(), BorderWraithServant.setCustomAttributes().build());
                event.put(ModEntityType.MUCK_WRAITH_SERVANT.get(), MuckWraithServant.setCustomAttributes().build());
                event.put(ModEntityType.PHANTOM_SERVANT.get(), PhantomServant.setCustomAttributes().build());
                event.put(ModEntityType.VANGUARD_SERVANT.get(), VanguardServant.setCustomAttributes().build());
                event.put(ModEntityType.SKELETON_PILLAGER_SERVANT.get(),
                                SkeletonPillagerServant.setCustomAttributes().build());
                event.put(ModEntityType.ZOMBIE_VINDICATOR_SERVANT.get(),
                                ZombieVindicatorServant.setCustomAttributes().build());
                event.put(ModEntityType.BOUND_EVOKER.get(), BoundEvoker.setCustomAttributes().build());
                event.put(ModEntityType.BOUND_GEOMANCER.get(), BoundGeomancer.setCustomAttributes().build());
                event.put(ModEntityType.BOUND_ICEOLOGER.get(), BoundIceologer.setCustomAttributes().build());
                event.put(ModEntityType.BOUND_CRYOLOGER.get(), BoundCryologer.setCustomAttributes().build());
                event.put(ModEntityType.BOUND_WIND_CALLER.get(), BoundWindCaller.setCustomAttributes().build());
                event.put(ModEntityType.BOUND_STORM_CASTER.get(), BoundStormCaster.setCustomAttributes().build());
                event.put(ModEntityType.HAUNTED_ARMOR_SERVANT.get(), HauntedArmorServant.setCustomAttributes().build());
                event.put(ModEntityType.HAUNTED_SKULL.get(), HauntedSkull.setCustomAttributes().build());
                event.put(ModEntityType.DOPPELGANGER.get(), Doppelganger.setCustomAttributes().build());
                event.put(ModEntityType.MINI_GHAST.get(), MiniGhast.setCustomAttributes().build());
                event.put(ModEntityType.GHAST_SERVANT.get(), GhastServant.setCustomAttributes().build());
                event.put(ModEntityType.BLAZE_SERVANT.get(), BlazeServant.setCustomAttributes().build());
                event.put(ModEntityType.WILDFIRE.get(), Wildfire.setCustomAttributes().build());
                event.put(ModEntityType.SLIME_SERVANT.get(), SlimeServant.setCustomAttributes().build());
                event.put(ModEntityType.MAGMA_CUBE_SERVANT.get(), MagmaCubeServant.setCustomAttributes().build());
                event.put(ModEntityType.CRYPT_SLIME_SERVANT.get(), CryptSlimeServant.setCustomAttributes().build());
                event.put(ModEntityType.TROPICAL_SLIME_SERVANT.get(),
                                TropicalSlimeServant.setCustomAttributes().build());
                event.put(ModEntityType.SPIDER_SERVANT.get(), SpiderServant.setCustomAttributes().build());
                event.put(ModEntityType.CAVE_SPIDER_SERVANT.get(), CaveSpiderServant.setCustomAttributes().build());
                event.put(ModEntityType.WEB_SPIDER_SERVANT.get(), WebSpiderServant.setCustomAttributes().build());
                event.put(ModEntityType.ICY_SPIDER_SERVANT.get(), IcySpiderServant.setCustomAttributes().build());
                event.put(ModEntityType.BONE_SPIDER_SERVANT.get(), BoneSpiderServant.setCustomAttributes().build());
                event.put(ModEntityType.BROOD_MOTHER_SERVANT.get(), AbstractBroodMother.setCustomAttributes().build());
                event.put(ModEntityType.PRISONER.get(), Villager.createAttributes().build());
                event.put(ModEntityType.NEOLLAGER.get(), Neollager.setCustomAttributes().build());
                event.put(ModEntityType.PILLAGER_SERVANT.get(), PillagerServant.setCustomAttributes().build());
                event.put(ModEntityType.PIKER_SERVANT.get(), PikerServant.setCustomAttributes().build());
                event.put(ModEntityType.SIGNALER_SERVANT.get(), SignalerServant.setCustomAttributes().build());
                event.put(ModEntityType.VINDICATOR_SERVANT.get(), VindicatorServant.setCustomAttributes().build());
                event.put(ModEntityType.VINDICATOR_CHEF_SERVANT.get(),
                                VindicatorChefServant.setCustomAttributes().build());
                event.put(ModEntityType.MOUNTAINEER_SERVANT.get(), MountaineerServant.setCustomAttributes().build());
                event.put(ModEntityType.CRUSHER_SERVANT.get(), CrusherServant.setCustomAttributes().build());
                event.put(ModEntityType.EVOKER_SERVANT.get(), EvokerServant.setCustomAttributes().build());
                event.put(ModEntityType.GEOMANCER_SERVANT.get(), GeomancerServant.setCustomAttributes().build());
                event.put(ModEntityType.ICEOLOGER_SERVANT.get(), IceologerServant.setCustomAttributes().build());
                event.put(ModEntityType.CRYOLOGER_SERVANT.get(), CryologerServant.setCustomAttributes().build());
                event.put(ModEntityType.WIND_CALLER_SERVANT.get(), WindCallerServant.setCustomAttributes().build());
                event.put(ModEntityType.STORM_CASTER_SERVANT.get(), StormCasterServant.setCustomAttributes().build());
                event.put(ModEntityType.TRAMPLER_SERVANT.get(), AllyTrampler.setCustomAttributes().build());
                event.put(ModEntityType.RAVAGED.get(), Ravaged.setCustomAttributes().build());
                event.put(ModEntityType.MOD_RAVAGER.get(), ModRavager.setCustomAttributes().build());
                event.put(ModEntityType.ARMORED_RAVAGER.get(), Ravager.createAttributes().build());
                event.put(ModEntityType.ZOMBIE_RAVAGER.get(), ZombieRavager.setCustomAttributes().build());
                event.put(ModEntityType.WITCH_SERVANT.get(), WitchServant.setCustomAttributes().build());
                event.put(ModEntityType.WARLOCK_SERVANT.get(), WarlockServant.setCustomAttributes().build());
                event.put(ModEntityType.MAVERICK_SERVANT.get(), MaverickServant.setCustomAttributes().build());
                event.put(ModEntityType.BLACK_WOLF.get(), BlackWolf.setCustomAttributes().build());
                event.put(ModEntityType.SKELETON_WOLF.get(), SkeletonWolf.setCustomAttributes().build());
                event.put(ModEntityType.HELLHOUND.get(), Hellhound.setCustomAttributes().build());
                event.put(ModEntityType.TWILIGHT_GOAT.get(), TwilightGoat.setCustomAttributes().build());
                event.put(ModEntityType.SNAPPER.get(), Snapper.setCustomAttributes().build());
                event.put(ModEntityType.GNASHER.get(), Gnasher.setCustomAttributes().build());
                event.put(ModEntityType.GUARDIAN_SERVANT.get(), GuardianServant.setCustomAttributes().build());
                event.put(ModEntityType.BEAR_SERVANT.get(), BearServant.setCustomAttributes().build());
                event.put(ModEntityType.POLAR_BEAR_SERVANT.get(), BearServant.setCustomAttributes().build());
                event.put(ModEntityType.HOGLIN_SERVANT.get(), HoglinServant.setCustomAttributes().build());
                event.put(ModEntityType.BLACK_BEAST.get(), BlackBeast.setCustomAttributes().build());
                event.put(ModEntityType.WHISPERER.get(), Whisperer.setCustomAttributes().build());
                event.put(ModEntityType.WAVEWHISPERER.get(), Wavewhisperer.setCustomAttributes().build());
                event.put(ModEntityType.LEAPLEAF.get(), Leapleaf.setCustomAttributes().build());
                event.put(ModEntityType.ICE_GOLEM.get(), IceGolem.setCustomAttributes().build());
                event.put(ModEntityType.SQUALL_GOLEM.get(), SquallGolem.setCustomAttributes().build());
                event.put(ModEntityType.REDSTONE_GOLEM.get(), RedstoneGolem.setCustomAttributes().build());
                event.put(ModEntityType.GRAVE_GOLEM.get(), GraveGolem.setCustomAttributes().build());
                event.put(ModEntityType.HAUNT.get(), Haunt.setCustomAttributes().build());
                event.put(ModEntityType.REDSTONE_MONSTROSITY.get(), RedstoneMonstrosity.setCustomAttributes().build());
                event.put(ModEntityType.REDSTONE_CUBE.get(), RedstoneCube.setCustomAttributes().build());
                event.put(ModEntityType.WATCHLING_SERVANT.get(), WatchlingServant.setCustomAttributes().build());
                event.put(ModEntityType.BLASTLING_SERVANT.get(), BlastlingServant.setCustomAttributes().build());
                event.put(ModEntityType.SNARELING_SERVANT.get(), SnarelingServant.setCustomAttributes().build());
                event.put(ModEntityType.TOTEMIC_WALL.get(), TotemicWall.setCustomAttributes().build());
                event.put(ModEntityType.TOTEMIC_BOMB.get(), TotemicBomb.setCustomAttributes().build());
                event.put(ModEntityType.GLACIAL_WALL.get(), GlacialWall.setCustomAttributes().build());
                event.put(ModEntityType.QUICK_GROWING_VINE.get(), QuickGrowingVine.setCustomAttributes().build());
                event.put(ModEntityType.QUICK_GROWING_KELP.get(), QuickGrowingKelp.setCustomAttributes().build());
                event.put(ModEntityType.POISON_QUILL_VINE.get(), PoisonQuillVine.setCustomAttributes().build());
                event.put(ModEntityType.POISON_ANEMONE.get(), PoisonAnemone.setCustomAttributes().build());
                event.put(ModEntityType.SPIDER_EGG.get(), SpiderEgg.setCustomAttributes().build());
                event.put(ModEntityType.INSECT_SWARM.get(), InsectSwarm.setCustomAttributes().build());
                event.put(ModEntityType.BEAST_HEAD.get(), BeastHead.setCustomAttributes().build());
                event.put(ModEntityType.GULF_TENTACLE.get(), GulfTentacle.setCustomAttributes().build());
                event.put(ModEntityType.VOLCANO.get(), Volcano.setCustomAttributes().build());
                event.put(ModEntityType.SORCERER.get(), Sorcerer.setCustomAttributes().build());
                event.put(ModEntityType.ENVIOKER.get(), Envioker.setCustomAttributes().build());
                event.put(ModEntityType.TORMENTOR.get(), Tormentor.setCustomAttributes().build());
                event.put(ModEntityType.INQUILLAGER.get(), Inquillager.setCustomAttributes().build());
                event.put(ModEntityType.CONQUILLAGER.get(), Conquillager.setCustomAttributes().build());
                event.put(ModEntityType.PIKER.get(), Piker.setCustomAttributes().build());
                event.put(ModEntityType.RIPPER.get(), Ripper.setCustomAttributes().build());
                event.put(ModEntityType.TRAMPLER.get(), Trampler.setCustomAttributes().build());
                event.put(ModEntityType.CRUSHER.get(), Crusher.setCustomAttributes().build());
                event.put(ModEntityType.STORM_CASTER.get(), StormCaster.setCustomAttributes().build());
                event.put(ModEntityType.CRYOLOGER.get(), Cryologer.setCustomAttributes().build());
                event.put(ModEntityType.PREACHER.get(), Preacher.setCustomAttributes().build());
                event.put(ModEntityType.MINISTER.get(), Minister.setCustomAttributes().build());
                event.put(ModEntityType.HOSTILE_REDSTONE_GOLEM.get(),
                                HostileRedstoneGolem.setCustomAttributes().build());
                event.put(ModEntityType.HOSTILE_REDSTONE_MONSTROSITY.get(),
                                HostileRedstoneMonstrosity.setCustomAttributes().build());
                event.put(ModEntityType.VIZIER.get(), Vizier.setCustomAttributes().build());
                event.put(ModEntityType.VIZIER_CLONE.get(), VizierClone.setCustomAttributes().build());
                event.put(ModEntityType.IRK.get(), Irk.setCustomAttributes().build());
                event.put(ModEntityType.WIGHT.get(), Wight.setCustomAttributes().build());
                event.put(ModEntityType.CARRION_MAGGOT.get(), CarrionMaggot.setCustomAttributes().build());
                event.put(ModEntityType.CARRION_FLY.get(), CarrionFly.setCustomAttributes().build());
                event.put(ModEntityType.SKULL_LORD.get(), SkullLord.setCustomAttributes().build());
                event.put(ModEntityType.BONE_LORD.get(), BoneLord.setCustomAttributes().build());
                event.put(ModEntityType.WITHER_NECROMANCER.get(), WitherNecromancer.setCustomAttributes().build());
                event.put(ModEntityType.RAID_BOSS_SUMMON.get(), Monster.createMobAttributes().build());
                event.put(ModEntityType.SURVEY_EYE.get(), Mob.createMobAttributes().build());
                event.put(ModEntityType.HAUNTED_ARMOR_STAND.get(), LivingEntity.createLivingAttributes().build());
        }

        private void SpawnPlacementEvent(RegisterSpawnPlacementsEvent event) {
                event.register(ModEntityType.WARLOCK.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.HERETIC.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.MAVERICK.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.OBSIDIAN_MONOLITH.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ObsidianMonolith::checkOMSpawnRules,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.HOSTILE_BLACK_WOLF.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> true,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.REAPER.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> {
                                        if (level.getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL) {
                                            return false;
                                        }
                                        // Check if we're in Nether/End by checking dimension type or biome
                                        Level actualLevel = level.getLevel();
                                        if (actualLevel != null) {
                                            // Check if biome is Soul Sand Valley (hardcoded Nether spawn)
                                            if (level.getBiome(pos).is(net.minecraft.world.level.biome.Biomes.SOUL_SAND_VALLEY)) {
                                                return true;
                                            }
                                            // If dimension type is not natural (Nether/End), allow spawning without darkness
                                            if (!actualLevel.dimensionType().natural()) {
                                                return true;
                                            }
                                        }
                                        // In Overworld, require darkness
                                        return Monster.isDarkEnoughToSpawn(level, pos, random);
                                },
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.WRAITH.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> {
                                        if (level.getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL) {
                                            return false;
                                        }
                                        // Check if we're in Nether/End by checking dimension type or biome
                                        Level actualLevel = level.getLevel();
                                        if (actualLevel != null) {
                                            // Check if biome is Soul Sand Valley (hardcoded Nether spawn)
                                            if (level.getBiome(pos).is(net.minecraft.world.level.biome.Biomes.SOUL_SAND_VALLEY)) {
                                                return true;
                                            }
                                            // If dimension type is not natural (Nether/End), allow spawning without darkness
                                            if (!actualLevel.dimensionType().natural()) {
                                                return true;
                                            }
                                        }
                                        // In Overworld, require darkness
                                        return Monster.isDarkEnoughToSpawn(level, pos, random);
                                },
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.BORDER_WRAITH.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> true,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.MUCK_WRAITH.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> true,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.CRYPT_SLIME.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CryptSlime::checkMonsterSpawnRules,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.WEB_SPIDER.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.ICY_SPIDER.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.BONE_SPIDER.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.NECROMANCER.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> true,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.CAIRN_NECROMANCER.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> true,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.MOSSY_NECROMANCER.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> true,
                                RegisterSpawnPlacementsEvent.Operation.AND);
                event.register(ModEntityType.HAUNTED_ARMOR.get(), SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, spawnType, pos, random) -> true,
                                RegisterSpawnPlacementsEvent.Operation.AND);
        }

        @SuppressWarnings("all")
        private void enqueueIMC(final InterModEnqueueEvent event) {
                // Curios IMC disabled for now (Curios dependency not pinned for 1.21.1 yet).
        }

        @SubscribeEvent
        public void onServerStarting(ServerStartingEvent event) {
                ModSaveInventory.resetInstance();
                ModSaveInventory.setInstance(event.getServer().overworld());
        }

        @SubscribeEvent
        public void onServerStopped(ServerStoppedEvent event) {
                ModSaveInventory.resetInstance();
        }
}



