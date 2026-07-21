package za.co.infernos.goety.init;

import za.co.infernos.goety.Goety;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import za.co.infernos.goety.client.render.*;
import za.co.infernos.goety.client.render.block.ArcaRenderer;
import za.co.infernos.goety.client.render.block.CursedInfuserRenderer;
import za.co.infernos.goety.common.blocks.entities.ModBlockEntities;
import za.co.infernos.goety.common.items.FlameCaptureItem;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.client.render.block.BlackCrystalRenderer;
import za.co.infernos.goety.client.render.block.LoftyChestRenderer;
import za.co.infernos.goety.client.render.block.ModBlockLayer;
import za.co.infernos.goety.client.render.model.*;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import za.co.infernos.goety.common.entities.ModEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import za.co.infernos.goety.client.gui.overlay.CurrentFocusGui;
import za.co.infernos.goety.client.gui.screen.inventory.BrewBagScreen;
import za.co.infernos.goety.client.gui.screen.inventory.DarkAnvilScreen;
import za.co.infernos.goety.client.gui.screen.inventory.FocusBagScreen;
import za.co.infernos.goety.client.gui.screen.inventory.FocusPackScreen;
import za.co.infernos.goety.client.gui.screen.inventory.SoulItemScreen;
import za.co.infernos.goety.client.inventory.container.ModContainerType;

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInitEvents {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CuriosRenderer.register();
            ItemProperties.register(
                    ModItems.FLAME_CAPTURE.get(),
                    ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "capture"),
                    (stack, level, entity, seed) -> FlameCaptureItem.hasEntity(stack) ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        ModKeybindings.register(event);
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, CurrentFocusGui.LAYER_ID, CurrentFocusGui.LAYER);
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModContainerType.WAND.get(), SoulItemScreen::new);
        event.register(ModContainerType.FOCUS_BAG.get(), FocusBagScreen::new);
        event.register(ModContainerType.FOCUS_PACK.get(), FocusPackScreen::new);
        event.register(ModContainerType.BREW_BAG.get(), BrewBagScreen::new);
        event.register(ModContainerType.DARK_ANVIL.get(), DarkAnvilScreen::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayer.LICH, LichModeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.WRAITH, WraithModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ICE_BOUQUET, IceBouquetModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ZPIGLIN_SERVANT, ZPiglinModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.REDSTONE_GOLEM, RedstoneGolemModel::createBodyLayer);
        // Register ModBlockLayer model layers
        event.registerLayerDefinition(ModBlockLayer.TALL_SKULL, TallSkullModel::createBodyLayer);
        event.registerLayerDefinition(ModBlockLayer.REDSTONE_GOLEM_SKULL, RedstoneGolemSkullModel::createBodyLayer);
        event.registerLayerDefinition(ModBlockLayer.GRAVE_GOLEM_SKULL, GraveGolemSkullModel::createBodyLayer);
        event.registerLayerDefinition(ModBlockLayer.REDSTONE_MONSTROSITY_HEAD, RedstoneMonstrosityHeadModel::createBodyLayer);
        event.registerLayerDefinition(ModBlockLayer.LOFTY_CHEST, LoftyChestRenderer::createBodyLayer);
        event.registerLayerDefinition(ModBlockLayer.BLACK_CRYSTAL, BlackCrystalRenderer::createBodyLayer);
        event.registerLayerDefinition(ModBlockLayer.ARCA, ArcaRenderer::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.APOSTLE, ApostleModel::createBodyLayer);
        // BearModel doesn't exist - removed
        event.registerLayerDefinition(ModModelLayer.BEAST_HEAD, BeastHeadModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BIOMINE, BioMineModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BLACKGUARD, BlackguardModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BLACK_BEAST, BlackBeastModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BLACK_WOLF, BlackWolfModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BLASTLING, BlastlingModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BLAST_FUNGUS, BlastFungusModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BROOD_MOTHER, BroodMotherModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.CONQUILLAGER, ConquillagerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.CRUSHER, CrusherModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.CRYOLOGER, CryologerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.DAMNED, DamnedModel::createBodyLayer);
        // DamnedHumanModel doesn't exist - removed
        event.registerLayerDefinition(ModModelLayer.DROWNED_NECROMANCER, DrownedNecromancerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.EARTH_FIST, EarthFistModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ENDERSENT, EndersentModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ENDER_KEEPER, EnderKeeperModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ENTANGLE_VINES, EntangleVinesModel::createBodyLayer);
        // FireTornadoModel doesn't exist - removed
        event.registerLayerDefinition(ModModelLayer.GEOMANCER, GeomancerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.GNASHER, GnasherModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.GRAVE_GOLEM, GraveGolemModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.GULF_TENTACLE, GulfTentacleModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.HARPOON, HarpoonModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.HAUNT, HauntModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.HAUNTED_SKULL, HauntedSkullModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.HAUNTED_SKULL_FIRELESS, HauntedSkullModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.HELL_BLAST, HellBlastModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.HERETIC, HereticModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ICEOLOGER, IceologerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ICE_CHUNK, IceChunkModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ICE_GOLEM, IceGolemModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ICY_SPIDER, ModSpiderModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ILLAGER_SERVANT, IllagerServantModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.INFERNO, InfernoModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.INQUILLAGER, InquillagerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.IRK, IrkModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.LEAPLEAF, LeapleafModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MAGMA_CUBE, MagmaCubeServantModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MALGHAST, ModGhastModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MAVERICK, MaverickModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MINION, MinionModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MINISTER, MinisterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MINI_GHAST, MiniGhastModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MOD_SPIDER, ModSpiderModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MOD_WITCH, ModWitchModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MONOLITH, MonolithModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MOUNTAINEER, MountaineerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.NECROMANCER, NecromancerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.PIKER, PikerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.POISON_QUILL, PoisonQuillModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.POISON_QUILL_VINE, PoisonQuillVineModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.PREACHER, PreacherModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.PRISONER, PrisonerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.QUICK_GROWING_VINE, QuickGrowingVineModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.RAVAGED, RavagedModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.RAVAGER, ModRavagerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.REAPER, ReaperModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.REDSTONE_CUBE, RedstoneCubeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.REDSTONE_MONSTROSITY, RedstoneMonstrosityModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.RIPPER, RipperModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SCATTER_MINE, ScatterMineModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SHIELD_DEBRIS, ShieldDebrisModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SKELETON_VILLAGER_SERVANT, SkeletonVillagerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SKULL_LORD, SkullLordModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SNAPPER, SnapperModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SNARELING, SnarelingModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SORCERER, SorcererModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SOUL_BOLT, SoulBoltModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SOUL_BOMB, SoulBombModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SPECTER, SpecterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SPIDER_EGG, SpiderEggModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SPIKE, SpikeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SQUALL_GOLEM, SquallGolemModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.STORM_CASTER, StormCasterModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SUMMON_CIRCLE, SummonCircleModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SUMMON_CIRCLE_BOSS, SummonCircleBossModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SUNKEN_SKELETON, SunkenSkeletonModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.TIDAL_SURGE, TidalSurgeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.TORMENTOR, TormentorModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.TRAMPLER, TramplerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.TRIDENT_STORM, TridentStormModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.TROPICAL_SLIME_INNER, TropicalSlimeModel::createInnerBodyLayer);
        event.registerLayerDefinition(ModModelLayer.TROPICAL_SLIME_OUTER, TropicalSlimeModel::createOuterBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VANGUARD, VanguardModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VICIOUS_PIKE, ViciousPikeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VICIOUS_TOOTH, ViciousToothModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VILLAGER_ARMOR_INNER, VillagerArmorModel::createInnerArmorLayer);
        event.registerLayerDefinition(ModModelLayer.VILLAGER_ARMOR_OUTER, VillagerArmorModel::createOuterArmorLayer);
        event.registerLayerDefinition(ModModelLayer.VINDICATOR_CHEF, VindicatorChefModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VIZIER, VizierModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VIZIER_ARMOR, VizierModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VIZIER_CLONE, VizierCloneModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VOID_SHOCK, VoidShockModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VOID_SHOCK_BOMB, VoidShockBombModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.VOLCANO, VolcanoModel::createBodyLayer);
        // Missing model layers
        event.registerLayerDefinition(ModModelLayer.HAUNTED_ARMOR_STAND, HauntedArmorStandModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.HAS_INNER, () -> HauntedArmorStandArmorModel.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(ModModelLayer.HAS_OUTER, () -> HauntedArmorStandArmorModel.createBodyLayer(new CubeDeformation(1.0F)));
        event.registerLayerDefinition(ModModelLayer.BEAR, BearServantModel::createBodyLayer);
        
        // Register boat model layers for all boat types
        event.registerLayerDefinition(ModModelLayer.createBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.HAUNTED), net.minecraft.client.model.BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.ROTTEN), net.minecraft.client.model.BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.WINDSWEPT), net.minecraft.client.model.BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.PINE), net.minecraft.client.model.BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.CHORUS), net.minecraft.client.model.BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.CORRUPT_CHORUS), net.minecraft.client.model.BoatModel::createBodyModel);
        
        // Register chest boat model layers for all boat types
        event.registerLayerDefinition(ModModelLayer.createChestBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.HAUNTED), net.minecraft.client.model.ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createChestBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.ROTTEN), net.minecraft.client.model.ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createChestBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.WINDSWEPT), net.minecraft.client.model.ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createChestBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.PINE), net.minecraft.client.model.ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createChestBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.CHORUS), net.minecraft.client.model.ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.createChestBoatModelName(za.co.infernos.goety.common.entities.vehicle.ModBoat.Type.CORRUPT_CHORUS), net.minecraft.client.model.ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayer.BLOCK, BlockModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BOUND_ILLAGER, BoundIllagerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.CRONE, CroneModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.FIRE_TORNADO, CycloneModel::createBodyLayer);
        // Note: FIRE_TORNADO uses CycloneModel, not FireTornadoModel
        event.registerLayerDefinition(ModModelLayer.FLY, CarrionFlyModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MAGGOT, CarrionMaggotModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SCREAM, HellChantModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.NAMELESS_STAFF, NamelessStaffModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.DAMNED_HUMAN, DamnedModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.SMALL_PAINTING, HauntedPaintingModel::createSmallFrameLayer);
        event.registerLayerDefinition(ModModelLayer.MEDIUM_PAINTING, HauntedPaintingModel::createMediumFrameLayer);
        event.registerLayerDefinition(ModModelLayer.LARGE_PAINTING, HauntedPaintingModel::createLargeFrameLayer);
        event.registerLayerDefinition(ModModelLayer.TALL_PAINTING, HauntedPaintingModel::createTallFrameLayer);
        event.registerLayerDefinition(ModModelLayer.WIDE_PAINTING, HauntedPaintingModel::createWideFrameLayer);
        // Missing critical layers
        event.registerLayerDefinition(ModModelLayer.RAVAGER_ARMOR, ModRavagerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.APOSTLE_SHADE, ApostleShadeRenderer.ApostleShadeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.NAMELESS_SET, NecroCapeModel::createNamelessLayer);
        event.registerLayerDefinition(ModModelLayer.SOUL_ARMOR, () -> LayerDefinition.create(PlayerModel.createMesh(CubeDeformation.NONE, false), 64, 64));
        event.registerLayerDefinition(ModModelLayer.SOUL_SHIELD, () -> LayerDefinition.create(PlayerModel.createMesh(CubeDeformation.NONE, false), 64, 64));
        event.registerLayerDefinition(ModModelLayer.WARLOCK, WarlockModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.WATCHLING, WatchlingModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.WEB_SHOT, WebShotModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.WEB_SPIDER, WebSpiderModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.WHISPERER, WhispererModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.WIGHT, WightModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.WILDFIRE, WildfireModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.WIND_CALLER, WindCallerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.WITHER_NECROMANCER, WitherNecromancerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ZOMBIE_VILLAGER_SERVANT, VillagerServantModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Block entity renderers
        try {
            event.registerBlockEntityRenderer(ModBlockEntities.CURSED_INFUSER.get(), CursedInfuserRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CursedInfuser renderer", e);
        }

        // Register all entity renderers - using try-catch for each to handle missing renderers gracefully

        // Register Wraith and IceBouquet (already working)
        try {
            event.registerEntityRenderer(ModEntityType.WRAITH.get(), WraithRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Wraith renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ICE_BOUQUET.get(), IceBouquetRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IceBouquet renderer", e);
        }
        
        // Register all other entity renderers - only register ones that exist
        // Bosses
        try {
            event.registerEntityRenderer(ModEntityType.APOSTLE.get(), ApostleRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Apostle renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VIZIER.get(), VizierRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Vizier renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ENDER_KEEPER.get(), EnderKeeperRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register EnderKeeper renderer", e);
        }
        
        // Hostile Mobs - Wraiths
        try {
            event.registerEntityRenderer(ModEntityType.BORDER_WRAITH.get(), BorderWraithRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BorderWraith renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MUCK_WRAITH.get(), MuckWraithRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MuckWraith renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.REAPER.get(), ReaperRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Reaper renderer", e);
        }
        
        // Hostile Mobs - Necromancers  
        try {
            event.registerEntityRenderer(ModEntityType.NECROMANCER.get(), NecromancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Necromancer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CAIRN_NECROMANCER.get(), AbstractCairnNecromancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CairnNecromancer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MOSSY_NECROMANCER.get(), MossyNecromancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MossyNecromancer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.DROWNED_NECROMANCER_SERVANT.get(), DrownedNecromancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register DrownedNecromancerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WITHER_NECROMANCER_SERVANT.get(), WitherNecromancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WitherNecromancerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WITHER_NECROMANCER.get(), WitherNecromancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WitherNecromancer renderer", e);
        }
        
        // Hostile Mobs - Cultists
        try {
            event.registerEntityRenderer(ModEntityType.WARLOCK.get(), WarlockRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Warlock renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HERETIC.get(), HereticRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Heretic renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MAVERICK.get(), MaverickRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Maverick renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CRONE.get(), CroneRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Crone renderer", e);
        }
        
        // Hostile Mobs - Illagers
        try {
            event.registerEntityRenderer(ModEntityType.SORCERER.get(), SorcererRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Sorcerer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ENVIOKER.get(), EnviokerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Envioker renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.TORMENTOR.get(), TormentorRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Tormentor renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.INQUILLAGER.get(), InquillagerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Inquillager renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CONQUILLAGER.get(), ConquillagerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Conquillager renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.PIKER.get(), PikerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Piker renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.RIPPER.get(), RipperRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Ripper renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.TRAMPLER.get(), TramplerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Trampler renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CRUSHER.get(), CrusherRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Crusher renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.STORM_CASTER.get(), StormCasterRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register StormCaster renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CRYOLOGER.get(), CryologerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Cryologer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.PREACHER.get(), PreacherRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Preacher renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MINISTER.get(), MinisterRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Minister renderer", e);
        }
        
        // Hostile Mobs - Spiders
        try {
            event.registerEntityRenderer(ModEntityType.WEB_SPIDER.get(), WebSpiderRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WebSpider renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ICY_SPIDER.get(), IcySpiderRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IcySpider renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BONE_SPIDER.get(), BoneSpiderRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoneSpider renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BROOD_MOTHER.get(), BroodMotherRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BroodMother renderer", e);
        }
        
        // Hostile Mobs - Ender
        try {
            event.registerEntityRenderer(ModEntityType.WATCHLING.get(), WatchlingRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Watchling renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BLASTLING.get(), BlastlingRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Blastling renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SNARELING.get(), SnarelingRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Snareling renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ENDERSENT.get(), EndersentRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Endersent renderer", e);
        }
        
        // Hostile Mobs - Golems
        try {
            event.registerEntityRenderer(ModEntityType.REDSTONE_GOLEM.get(), RedstoneGolemRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register RedstoneGolem renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.GRAVE_GOLEM.get(), GraveGolemRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register GraveGolem renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SQUALL_GOLEM.get(), SquallGolemRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SquallGolem renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ICE_GOLEM.get(), IceGolemRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IceGolem renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.REDSTONE_MONSTROSITY.get(), RedstoneMonstrosityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register RedstoneMonstrosity renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.REDSTONE_CUBE.get(), RedstoneCubeRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register RedstoneCube renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HOSTILE_REDSTONE_GOLEM.get(), HostileRedstoneGolemRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HostileRedstoneGolem renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HOSTILE_REDSTONE_MONSTROSITY.get(), RedstoneMonstrosityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HostileRedstoneMonstrosity renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.OBSIDIAN_MONOLITH.get(), ObsidianMonolithRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ObsidianMonolith renderer", e);
        }
        
        // Hostile Mobs - Other
        try {
            event.registerEntityRenderer(ModEntityType.CRYPT_SLIME.get(), CryptSlimeRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CryptSlime renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WHISPERER.get(), WhispererRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Whisperer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WAVEWHISPERER.get(), WhispererRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Wavewhisperer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.LEAPLEAF.get(), LeapleafRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Leapleaf renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HAUNT.get(), HauntRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Haunt renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SKULL_LORD.get(), SkullLordRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SkullLord renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BONE_LORD.get(), BoneLordRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoneLord renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WIGHT.get(), WightRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Wight renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WILDFIRE.get(), WildfireRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Wildfire renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.INFERNO.get(), InfernoRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Inferno renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MALGHAST.get(), MalghastRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Malghast renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.DAMNED.get(), DamnedRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Damned renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HOSTILE_BLACK_WOLF.get(), BlackWolfRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HostileBlackWolf renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BLACK_WOLF.get(), BlackWolfRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BlackWolf renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SKELETON_WOLF.get(), SkeletonWolfRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SkeletonWolf renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HELLHOUND.get(), HellhoundRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Hellhound renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.TWILIGHT_GOAT.get(), TwilightGoatRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register TwilightGoat renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SNAPPER.get(), SnapperRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Snapper renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.GNASHER.get(), GnasherRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Gnasher renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BLACK_BEAST.get(), BlackBeastRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BlackBeast renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VAMPIRE_BAT.get(), VampireBatRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VampireBat renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WARTLING.get(), WartlingRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Wartling renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.IRK.get(), IrkRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Irk renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VIZIER_CLONE.get(), VizierCloneRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VizierClone renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CARRION_MAGGOT.get(), CarrionMaggotRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CarrionMaggot renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CARRION_FLY.get(), CarrionFlyRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CarrionFly renderer", e);
        }
        
        // Servants - Zombies
        try {
            event.registerEntityRenderer(ModEntityType.ZOMBIE_SERVANT.get(), ZombieServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ZombieServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ZOMBIE_VILLAGER_SERVANT.get(), ZombieVillagerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ZombieVillagerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HUSK_SERVANT.get(), HuskServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HuskServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.DROWNED_SERVANT.get(), DrownedServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register DrownedServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.FROZEN_ZOMBIE_SERVANT.get(), FrozenZombieRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register FrozenZombieServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.JUNGLE_ZOMBIE_SERVANT.get(), JungleZombieRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register JungleZombieServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BLACKGUARD_SERVANT.get(), BlackguardRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BlackguardServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ZOMBIE_VINDICATOR_SERVANT.get(), ZombieVindicatorRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ZombieVindicatorServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ZOMBIE_RAVAGER.get(), ZombieRavagerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ZombieRavager renderer", e);
        }
        
        // Servants - Skeletons
        try {
            event.registerEntityRenderer(ModEntityType.SKELETON_SERVANT.get(), SkeletonServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SkeletonServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SKELETON_VILLAGER_SERVANT.get(), SkeletonVillagerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SkeletonVillagerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.STRAY_SERVANT.get(), SkeletonServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register StrayServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WITHER_SKELETON_SERVANT.get(), WitherSkeletonServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WitherSkeletonServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MOSSY_SKELETON_SERVANT.get(), SkeletonServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MossySkeletonServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SUNKEN_SKELETON_SERVANT.get(), SunkenSkeletonServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SunkenSkeletonServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SKELETON_PILLAGER_SERVANT.get(), SkeletonPillagerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SkeletonPillagerServant renderer", e);
        }
        
        // Servants - Necromancers
        try {
            event.registerEntityRenderer(ModEntityType.NECROMANCER_SERVANT.get(), NecromancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register NecromancerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CAIRN_NECROMANCER_SERVANT.get(), AbstractCairnNecromancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CairnNecromancerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MOSSY_NECROMANCER_SERVANT.get(), MossyNecromancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MossyNecromancerServant renderer", e);
        }
        
        // Servants - Wraiths
        try {
            event.registerEntityRenderer(ModEntityType.WRAITH_SERVANT.get(), WraithServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WraithServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BORDER_WRAITH_SERVANT.get(), BorderWraithServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BorderWraithServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MUCK_WRAITH_SERVANT.get(), MuckWraithServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MuckWraithServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.REAPER_SERVANT.get(), ReaperRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ReaperServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VANGUARD_SERVANT.get(), VanguardRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VanguardServant renderer", e);
        }
        
        // Servants - Illagers
        try {
            event.registerEntityRenderer(ModEntityType.PILLAGER_SERVANT.get(), PillagerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register PillagerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.PIKER_SERVANT.get(), PikerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register PikerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SIGNALER_SERVANT.get(), SignalerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SignalerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VINDICATOR_SERVANT.get(), VindicatorServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VindicatorServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VINDICATOR_CHEF_SERVANT.get(), VindicatorChefServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VindicatorChefServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MOUNTAINEER_SERVANT.get(), MountaineerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MountaineerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CRUSHER_SERVANT.get(), CrusherServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CrusherServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.EVOKER_SERVANT.get(), EvokerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register EvokerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.GEOMANCER_SERVANT.get(), GeomancerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register GeomancerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ICEOLOGER_SERVANT.get(), IceologerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IceologerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CRYOLOGER_SERVANT.get(), CryologerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CryologerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WIND_CALLER_SERVANT.get(), WindCallerServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WindCallerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.STORM_CASTER_SERVANT.get(), StormCasterServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register StormCasterServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WARLOCK_SERVANT.get(), WarlockServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WarlockServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MAVERICK_SERVANT.get(), MaverickServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MaverickServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WITCH_SERVANT.get(), WitchServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WitchServant renderer", e);
        }
        
        // Servants - Bound
        try {
            event.registerEntityRenderer(ModEntityType.BOUND_EVOKER.get(), BoundEvokerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoundEvoker renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BOUND_GEOMANCER.get(), BoundGeomancerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoundGeomancer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BOUND_ICEOLOGER.get(), BoundIceologerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoundIceologer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BOUND_CRYOLOGER.get(), BoundCryologerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoundCryologer renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BOUND_WIND_CALLER.get(), BoundWindCallerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoundWindCaller renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BOUND_STORM_CASTER.get(), BoundStormCasterRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoundStormCaster renderer", e);
        }
        
        // Servants - Spiders
        try {
            event.registerEntityRenderer(ModEntityType.SPIDER_SERVANT.get(), SpiderServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SpiderServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CAVE_SPIDER_SERVANT.get(), CaveSpiderServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CaveSpiderServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WEB_SPIDER_SERVANT.get(), WebSpiderServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WebSpiderServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ICY_SPIDER_SERVANT.get(), IcySpiderServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IcySpiderServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BONE_SPIDER_SERVANT.get(), BoneSpiderServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoneSpiderServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BROOD_MOTHER_SERVANT.get(), BroodMotherRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BroodMotherServant renderer", e);
        }
        
        // Servants - Slimes
        try {
            event.registerEntityRenderer(ModEntityType.SLIME_SERVANT.get(), SlimeServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SlimeServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MAGMA_CUBE_SERVANT.get(), MagmaCubeServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MagmaCubeServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CRYPT_SLIME_SERVANT.get(), CryptSlimeServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CryptSlimeServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.TROPICAL_SLIME_SERVANT.get(), TropicalSlimeServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register TropicalSlimeServant renderer", e);
        }
        
        // Servants - Other
        try {
            event.registerEntityRenderer(ModEntityType.PHANTOM_SERVANT.get(), PhantomServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register PhantomServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.GHAST_SERVANT.get(), GhastServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register GhastServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BLAZE_SERVANT.get(), BlazeServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BlazeServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.GUARDIAN_SERVANT.get(), GuardianServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register GuardianServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BEAR_SERVANT.get(), BearServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BearServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.POLAR_BEAR_SERVANT.get(), BearServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register PolarBearServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HOGLIN_SERVANT.get(), HoglinServantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HoglinServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.TRAMPLER_SERVANT.get(), AllyTramplerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register TramplerServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ZPIGLIN_SERVANT.get(), ZPiglinRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ZpiglinServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ZPIGLIN_BRUTE_SERVANT.get(), ZPiglinRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ZpiglinBruteServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VEX_SERVANT.get(), AllyVexRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VexServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.IRK_SERVANT.get(), IrkRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IrkServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WATCHLING_SERVANT.get(), WatchlingRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WatchlingServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BLASTLING_SERVANT.get(), BlastlingRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BlastlingServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SNARELING_SERVANT.get(), SnarelingRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SnarelingServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HAUNTED_ARMOR.get(), HauntedArmorRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HauntedArmor renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HAUNTED_ARMOR_SERVANT.get(), HauntedArmorRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HauntedArmorServant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HAUNTED_SKULL.get(), HauntedSkullRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HauntedSkull renderer", e);
        }
        // DoppelgangerRenderer requires (Context, boolean) constructor - using default false for slim model
        try {
            event.registerEntityRenderer(ModEntityType.DOPPELGANGER.get(), (ctx) -> new DoppelgangerRenderer(ctx, false));
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Doppelganger renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.PRISONER.get(), PrisonerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Prisoner renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.NEOLLAGER.get(), NeollagerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Neollager renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.RAVAGED.get(), RavagedRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Ravaged renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MOD_RAVAGER.get(), ModRavagerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ModRavager renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ARMORED_RAVAGER.get(), ModRavagerRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ArmoredRavager renderer", e);
        }
        
        // Projectiles and Spells
        try {
            event.registerEntityRenderer(ModEntityType.MAGIC_FIRE.get(), MagicFireRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MagicFire renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HELLFIRE.get(), HellfireRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Hellfire renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ICE_CHUNK.get(), IceChunkRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IceChunk renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VICIOUS_TOOTH.get(), ViciousToothRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ViciousTooth renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VICIOUS_PIKE.get(), ViciousPikeRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ViciousPike renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.EARTH_FIST.get(), EarthFistRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register EarthFist renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BLOSSOM_THORN.get(), BlossomThornRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BlossomThorn renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CORRUPTED_BEAM.get(), CorruptedBeamRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CorruptedBeam renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SCATTER_MINE.get(), ScatterMineRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ScatterMine renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SCATTER_BOMB.get(), ScatterBombRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ScatterBomb renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SOUL_BOMB.get(), SoulBombRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SoulBomb renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SNAP_FUNGUS.get(), SnapFungusRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SnapFungus renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BLAST_FUNGUS.get(), BlastFungusRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BlastFungus renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.PYROCLAST.get(), PyroclastRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Pyroclast renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MAGMA_BOMB.get(), MagmaBombRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MagmaBomb renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BLOSSOM_BALL.get(), BlossomBallRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BlossomBall renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WEB_SHOT.get(), WebShotRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WebShot renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SNARELING_SHOT.get(), SnarelingShotRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SnarelingShot renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ENDER_GOO.get(), EnderGooRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register EnderGoo renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ENTANGLE_VINES.get(), EntangleVinesRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register EntangleVines renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SPIDER_WEB.get(), SpiderWebRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SpiderWeb renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SNARELING_GOOP.get(), SnarelingGoopRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SnarelingGoop renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.QUICK_GROWING_VINE.get(), QuickGrowingVineRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register QuickGrowingVine renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.QUICK_GROWING_KELP.get(), QuickGrowingVineRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register QuickGrowingKelp renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.POISON_QUILL_VINE.get(), PoisonQuillVineRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register PoisonQuillVine renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.POISON_ANEMONE.get(), PoisonQuillVineRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register PoisonAnemone renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BIOMINE.get(), BioMineRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Biomine renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SPIDER_EGG.get(), SpiderEggRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SpiderEgg renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.INSECT_SWARM.get(), InsectSwarmRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register InsectSwarm renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BEAST_HEAD.get(), BeastHeadRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BeastHead renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.GULF_TENTACLE.get(), GulfTentacleRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register GulfTentacle renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VOLCANO.get(), VolcanoRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Volcano renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.FIRE_TORNADO.get(), FireTornadoRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register FireTornado renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CYCLONE.get(), CycloneRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Cyclone renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.TIDAL_SURGE.get(), TidalSurgeRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register TidalSurge renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.RAZOR_WIND.get(), RazorWindRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register RazorWind renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VOID_SLASH.get(), VoidSlashRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VoidSlash renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VOID_SHOCK.get(), VoidShockRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VoidShock renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VOID_SHOCK_BOMB.get(), VoidShockBombRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VoidShockBomb renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VOID_RIFT.get(), VoidRiftRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VoidRift renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.TRIDENT_STORM.get(), TridentStormRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register TridentStorm renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SUMMON_CIRCLE.get(), SummonCircleRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SummonCircle renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SUMMON_CIRCLE_BOSS.get(), SummonCircleBossRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SummonCircleBoss renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SUMMON_FIERY.get(), SummonCircleVariantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SummonFiery renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SUMMON_APOSTLE.get(), SummonApostleRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SummonApostle renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.TOTEMIC_WALL.get(), TotemicWallRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register TotemicWall renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.TOTEMIC_BOMB.get(), TotemicBombRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register TotemicBomb renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.GLACIAL_WALL.get(), GlacialWallRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register GlacialWall renderer", e);
        }
        
        // Projectiles - Arrows and Bolts
        try {
            event.registerEntityRenderer(ModEntityType.RAIN_ARROW.get(), RainArrowRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register RainArrow renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.DEATH_ARROW.get(), DeathArrowRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register DeathArrow renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HARPOON.get(), HarpoonRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Harpoon renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.POISON_QUILL.get(), PoisonQuillRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register PoisonQuill renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SOUL_BOLT.get(), SoulBoltRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SoulBolt renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.POISON_BOLT.get(), PoisonBoltRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register PoisonBolt renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.STEAM_MISSILE.get(), SteamMissileRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SteamMissile renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.WITHER_BOLT.get(), WitherBoltRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register WitherBolt renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.NECRO_BOLT.get(), NecroBoltRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register NecroBolt renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SHIELD_DEBRIS.get(), ShieldDebrisRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ShieldDebris renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SOUL_BULLET.get(), SoulBulletRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SoulBullet renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SOUL_LIGHT.get(), SoulBulletRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SoulLight renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.GLOW_LIGHT.get(), SoulBulletRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register GlowLight renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BONE_SHARD.get(), (ctx) -> new BoneShardRenderer<>(ctx, ctx.getItemRenderer()));
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BoneShard renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SWORD.get(), (ctx) -> new SwordProjectileRenderer<>(ctx, ctx.getItemRenderer()));
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Sword renderer", e);
        }
        
        // Projectiles - Fireballs and Explosives
        try {
            event.registerEntityRenderer(ModEntityType.MOD_FIREBALL.get(), ModFireballRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ModFireball renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.LAVABALL.get(), ModFireballRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Lavaball renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HELL_BOLT.get(), HellBoltRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HellBolt renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HELL_BLAST.get(), HellBlastRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HellBlast renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HELL_CHANT.get(), HellChantRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HellChant renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MOD_DRAGON_FIREBALL.get(), ModDragonFireballRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ModDragonFireball renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MOD_WITHER_SKULL.get(), ModWitherSkullRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ModWitherSkull renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HAUNTED_SKULL_SHOT.get(), HauntedSkullProjectileRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HauntedSkullShot renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ILL_BOMB.get(), IllBombRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IllBomb renderer", e);
        }
        
        // Projectiles - Other
        try {
            event.registerEntityRenderer(ModEntityType.ICE_SPIKE.get(), IceSpikeRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IceSpike renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ICE_SPEAR.get(), IceSpearRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IceSpear renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ICE_STORM.get(), IceStormRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register IceStorm renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SCYTHE.get(), ScytheSlashRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Scythe renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SPIKE.get(), SpikeRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Spike renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.FANG.get(), FangsRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Fang renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ELECTRO_ORB.get(), ElectroOrbRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ElectroOrb renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MINI_ELECTRO_ORB.get(), MiniElectroOrbRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MiniElectroOrb renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BOUNCY_BUBBLE.get(), BouncyBubbleRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BouncyBubble renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VINE_HOOK.get(), VineHookRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VineHook renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SPELL_LIGHTNING_BOLT.get(), SpellLightningBoltRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SpellLightningBolt renderer", e);
        }
        
        // Decorative
        try {
            event.registerEntityRenderer(ModEntityType.MOD_PAINTING.get(), HauntedPaintingRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ModPainting renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HAUNTED_ARMOR_STAND.get(), HauntedArmorStandRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HauntedArmorStand renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.FALLING_BLOCK.get(), ModFallingBlockRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register FallingBlock renderer", e);
        }
        
        // Vehicles
        try {
            event.registerEntityRenderer(ModEntityType.MOD_BOAT.get(), (ctx) -> new ModBoatRenderer(ctx, false));
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ModBoat renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MOD_CHEST_BOAT.get(), (ctx) -> new ModBoatRenderer(ctx, true));
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ModChestBoat renderer", e);
        }
        
        // Servants - Ghast
        try {
            event.registerEntityRenderer(ModEntityType.MINI_GHAST.get(), MiniGhastRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MiniGhast renderer", e);
        }
        
        // Traps and Utilities
        try {
            event.registerEntityRenderer(ModEntityType.LIGHTNING_TRAP.get(), TrapRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register LightningTrap renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MAGIC_LIGHTNING_TRAP.get(), TrapRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MagicLightningTrap renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VOID_LIGHTNING_TRAP.get(), TrapRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VoidLightningTrap renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.FIRE_RAIN_TRAP.get(), TrapRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register FireRainTrap renderer", e);
        }
        
        // Utility entities that need simple empty renderers
        try {
            event.registerEntityRenderer(ModEntityType.GHOST_ARROW.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register GhostArrow renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MAGIC_BOLT.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MagicBolt renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BREW.get(), (ctx) -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(ctx));
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Brew renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.FLYING_ITEM.get(), (ctx) -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(ctx));
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register FlyingItem renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ACID_POOL.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register AcidPool renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.FIRE_PILLAR.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register FirePillar renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.SURVEY_EYE.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register SurveyEye renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.VOID_EYE.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register VoidEye renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CRYPTIC_EYE.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CrypticEye renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MONSOON_CLOUD.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MonsoonCloud renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HAIL_CLOUD.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HailCloud renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.HELL_CLOUD.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register HellCloud renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.DRAGON_BREATH_CLOUD.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register DragonBreathCloud renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BREW_EFFECT_CLOUD.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BrewEffectCloud renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BREW_EFFECT_GAS.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BrewEffectGas renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.BERSERK_FUNGUS.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register BerserkFungus renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CUSHION.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register Cushion renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.MAGIC_GROUND.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register MagicGround renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.STORM_UTIL.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register StormUtil renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.DELAYED_SUMMON.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register DelayedSummon renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.RAID_BOSS_SUMMON.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register RaidBossSummon renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.CAMERA_SHAKE.get(), EmptyEntityRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register CameraShake renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.ARROW_RAIN_TRAP.get(), TrapRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register ArrowRainTrap renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.FIRE_TORNADO_TRAP.get(), TrapRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register FireTornadoTrap renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.FIRE_BLAST_TRAP.get(), TrapRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register FireBlastTrap renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.UPDRAFT_BLAST.get(), TrapRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register UpdraftBlast renderer", e);
        }
        try {
            event.registerEntityRenderer(ModEntityType.NETHER_METEOR.get(), NetherMeteorRenderer::new);
        } catch (Exception e) {
            Goety.LOGGER.error("Failed to register NetherMeteor renderer", e);
        }
        
        Goety.LOGGER.info("Registered all entity renderers");
    }
}
