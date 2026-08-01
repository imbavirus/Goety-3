package za.co.infernos.goety.client.events;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.blocks.entities.IBarrack;
import za.co.infernos.goety.api.blocks.entities.IOwnedBlock;
import za.co.infernos.goety.api.blocks.entities.ITrainingBlock;
import za.co.infernos.goety.api.blocks.entities.IWaystoneBlock;
import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.api.items.magic.IWand;
import za.co.infernos.goety.api.magic.ISpell;
import za.co.infernos.goety.client.audio.*;
import za.co.infernos.goety.client.gui.screen.inventory.BrewRadialMenuScreen;
import za.co.infernos.goety.client.gui.screen.inventory.FocusRadialMenuScreen;
import za.co.infernos.goety.client.render.BurrowingLaserRenderer;
import za.co.infernos.goety.client.render.GuardianLaserRenderer;
import za.co.infernos.goety.client.render.ModModelLayer;
import za.co.infernos.goety.client.render.WearRenderer;
import za.co.infernos.goety.client.render.item.CustomItemsRenderer;
import za.co.infernos.goety.client.render.model.LichModeModel;
import za.co.infernos.goety.common.blocks.entities.ArcaBlockEntity;
import za.co.infernos.goety.common.blocks.entities.BrewCauldronBlockEntity;
import za.co.infernos.goety.common.blocks.entities.CursedCageBlockEntity;
import za.co.infernos.goety.common.blocks.entities.OminousIdolBlockEntity;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.entities.ally.GuardianServant;
import za.co.infernos.goety.common.entities.ally.Leapleaf;
import za.co.infernos.goety.common.entities.ally.golem.SquallGolem;
import za.co.infernos.goety.common.entities.ally.illager.StormCasterServant;
import za.co.infernos.goety.common.entities.ally.illager.WindCallerServant;
import za.co.infernos.goety.common.entities.boss.Apostle;
import za.co.infernos.goety.common.entities.boss.EnderKeeper;
import za.co.infernos.goety.common.entities.boss.Vizier;
import za.co.infernos.goety.common.entities.hostile.Wight;
import za.co.infernos.goety.common.entities.hostile.ender.Endersent;
import za.co.infernos.goety.common.entities.hostile.illagers.HostileRedstoneGolem;
import za.co.infernos.goety.common.entities.hostile.illagers.HostileRedstoneMonstrosity;
import za.co.infernos.goety.common.entities.hostile.illagers.StormCaster;
import za.co.infernos.goety.common.entities.hostile.servants.Inferno;
import za.co.infernos.goety.common.entities.neutral.ApostleShade;
import za.co.infernos.goety.common.entities.neutral.CarrionFly;
import za.co.infernos.goety.common.entities.neutral.InsectSwarm;
import za.co.infernos.goety.common.entities.neutral.Wildfire;
import za.co.infernos.goety.common.entities.projectiles.CorruptedBeam;
import za.co.infernos.goety.common.entities.projectiles.IceStorm;
import za.co.infernos.goety.common.entities.util.CameraShake;
import za.co.infernos.goety.common.items.WaystoneItem;
import za.co.infernos.goety.common.items.curios.GloveItem;
import za.co.infernos.goety.common.magic.spells.abyss.PrismaBeamSpell;
import za.co.infernos.goety.common.magic.spells.geomancy.BurrowingSpell;
import net.neoforged.neoforge.network.PacketDistributor;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.client.*;
import za.co.infernos.goety.common.network.client.brew.CBrewBagKeyPacket;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModKeybindings;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.*;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.*;

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (event.getLevel() instanceof ClientLevel){
            Minecraft minecraft = Minecraft.getInstance();
            SoundManager soundHandler = minecraft.getSoundManager();
            if (entity instanceof CorruptedBeam){
                soundHandler.play(new LoopSound(ModSounds.CORRUPT_BEAM_LOOP.get(), entity));
                soundHandler.play(new LoopSound(ModSounds.CORRUPT_BEAM_SOUL.get(), entity));
            }
            if (entity instanceof ApostleShade){
                soundHandler.play(new LoopSound(ModSounds.APOSTLE_SHADE.get(), entity));
            }
            if (entity instanceof Inferno){
                soundHandler.play(new LoopSound(ModSounds.INFERNO_LOOP.get(), entity));
            }
            if (entity instanceof Wildfire){
                soundHandler.play(new LoopSound(ModSounds.WILDFIRE_LOOP.get(), entity));
            }
            if (entity instanceof WindCallerServant || entity instanceof StormCaster || entity instanceof StormCasterServant){
                soundHandler.play(new LoopSound(ModSounds.FLIGHT.get(), 1.0F, 1.3F, entity));
            }
            if (entity instanceof InsectSwarm){
                soundHandler.play(new LoopSound(ModSounds.INSECT_SWARM.get(), entity));
            }
            if (entity instanceof CarrionFly){
                soundHandler.play(new LoopSound(ModSounds.FLY_LOOP.get(), 0.4F, 2.0F, entity));
            }
            if (entity instanceof Wight wight && !wight.isHallucination()){
                soundHandler.play(new WightLoopSound(wight));
            }
            if (entity instanceof IceStorm){
                soundHandler.play(new LoopSound(ModSounds.ICE_STORM_LOOP.get(), entity));
            }
        }
    }

    /**
     * Ripped from @BobMowzie's codes:<a href="https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/ClientEventHandler.java#L211">...</a>
     */
    @SubscribeEvent
    public static void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        float delta = PARTIAL_TICK;
        if (player != null) {
            float ticksExistedDelta = player.tickCount + delta;
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.CameraShake, false) && !Minecraft.getInstance().isPaused()) {
                float shakeAmplitude = 0;
                for (CameraShake cameraShake : player.level().getEntitiesOfClass(CameraShake.class, player.getBoundingBox().inflate(20))) {
                    if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                        shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0F) {
                    shakeAmplitude = 1.0F;
                }
                event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3.0D + 2.0D) * 25.0D));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5.0D + 1.0D) * 25.0D));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4.0D) * 25.0D));
            }
        }
    }

    public static float PARTIAL_TICK = 0;

    @SubscribeEvent
    public static void renderTick(RenderFrameEvent.Pre event){
        PARTIAL_TICK = event.getPartialTick().getGameTimeDeltaPartialTick(false);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event){
        Player player = event.getEntity();
        if (SEHelper.hasCamera(player)){
            player.turn(0.0F, 0.0F);
            player.xxa = 0.0F;
            player.zza = 0.0F;
            player.setJumping(false);
        }
    }

    @SubscribeEvent
    public static void onInputInteract(InputEvent.InteractionKeyMappingTriggered event){
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player != null){
            if (SEHelper.hasCamera(player)){
                if (event.isAttack() || event.isPickBlock() || event.isUseItem()){
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Start event){
        if (event.getEntity().level() instanceof ClientLevel){
            Minecraft minecraft = Minecraft.getInstance();
            SoundManager soundHandler = minecraft.getSoundManager();
            if (WandUtil.getSpell(event.getEntity()) != null && event.getItem().getItem() instanceof IWand){
                ISpell spells = WandUtil.getSpell(event.getEntity());
                if (spells != null) {
                    if (spells.loopSound(event.getEntity()) != null) {
                        soundHandler.play(new ItemLoopSound(spells.loopSound(event.getEntity()), event.getEntity()));
                    } else if (spells instanceof PrismaBeamSpell){
                        soundHandler.play(new GuardianLaserSound(event.getEntity()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event){
        Entity entity = event.getEntity();
        if (entity.level() instanceof ClientLevel){
            Minecraft minecraft = Minecraft.getInstance();
            SoundManager soundHandler = minecraft.getSoundManager();
            if (entity instanceof SquallGolem squallGolem){
                if (squallGolem.noveltyTick == 1) {
                    soundHandler.play(new SummonNoveltySound(squallGolem, ModSounds.SQUALL_GOLEM_ALERT.get()));
                }
            }
            if (event.getEntity() instanceof Leapleaf leapleaf){
                if (leapleaf.noveltyTick == 1) {
                    soundHandler.play(new SummonNoveltySound(leapleaf, ModSounds.LEAPLEAF_ALERT.get()));
                }
            }
            if (event.getEntity() instanceof GuardianServant guardianServant){
                if (guardianServant.playAttackSound){
                    soundHandler.play(new GuardianAttackSound(guardianServant));
                    guardianServant.playAttackSound = false;
                }
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.BossMusic, false)) {
                if (entity instanceof LivingEntity livingEntity) {
                    if (entity instanceof Wight wight && !wight.isNoAi()) {
                        playPreBossMusic(ModSounds.ENDERMAN_THEME_PRE.get(), ModSounds.ARENA_END.get(), wight, 0.75F, 1.0F, 64);
                    }
                    if ((MiscCapHelper.getMobTarget(livingEntity) instanceof Player)
                    || (MiscCapHelper.getMobTarget(livingEntity) instanceof OwnableEntity ownable && ownable.getOwner() instanceof Player)
                    || entity.getType().is(ModTags.EntityTypes.GLOBAL_MUSIC_BOSS)) {
                        if (entity instanceof Apostle apostle && !apostle.isNoAi()) {
                            playBossMusic(ModSounds.APOSTLE_THEME.get(), ModSounds.APOSTLE_THEME_POST.get(), apostle);
                        }
                        if (entity instanceof Vizier vizier && !vizier.isNoAi()) {
                            playBossMusic(ModSounds.VIZIER_THEME.get(), vizier);
                        }
                        if (entity instanceof HostileRedstoneMonstrosity rm && !rm.isNoAi()) {
                            playBossMusic(ModSounds.RM_THEME.get(), ModSounds.BOSS_POST_2.get(), rm, 0.75F, 1.0F);
                        }
                        if (entity instanceof EnderKeeper enderKeeper && !enderKeeper.isNoAi()) {
                            playBossMusic(ModSounds.ENDER_KEEPER_THEME.get(), ModSounds.ENDER_KEEPER_THEME_POST.get(), enderKeeper, 0.75F, 0.825F);
                        }
                        if (entity instanceof HostileRedstoneGolem rm && !rm.isNoAi()) {
                            playBossMusic(ModSounds.RM_THEME.get(), ModSounds.BOSS_POST_2.get(), rm, 0.75F, 1.0F);
                        }
                        if (entity instanceof Endersent endersent && !endersent.isNoAi()) {
                            playBossMusic(ModSounds.ENDERMAN_THEME_PRE.get(), ModSounds.ARENA_END.get(), endersent, 0.75F, 1.0F);
                        }
                        if (entity instanceof Wight wight && !wight.isNoAi()) {
                            playBossMusic(ModSounds.ENDERMAN_THEME.get(), ModSounds.ARENA_END.get(), wight, 0.75F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    public static AbstractTickableSoundInstance PRE_BOSS_MUSIC;
    public static AbstractTickableSoundInstance BOSS_MUSIC;

    public static void playPreBossMusic(SoundEvent soundEvent, SoundEvent postBossMusic, Mob mob){
        playPreBossMusic(soundEvent, postBossMusic, mob, 1.0F, 1.0F, 0);
    }

    public static void playPreBossMusic(SoundEvent soundEvent, Mob mob, int withinRange){
        playPreBossMusic(soundEvent, ModSounds.BOSS_POST.get(), mob, 1.0F, 1.0F, withinRange);
    }

    public static void playPreBossMusic(SoundEvent soundEvent, SoundEvent postBossMusic, Mob mob, int withinRange){
        playPreBossMusic(soundEvent, postBossMusic, mob, 1.0F, 1.0F, withinRange);
    }

    public static void playPreBossMusic(SoundEvent soundEvent, SoundEvent postBossMusic, Mob mob, float volume, float pitch, int withinRange){
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.BossMusic, false)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (soundEvent != null && mob.isAlive()) {
                if (PRE_BOSS_MUSIC == null) {
                    PRE_BOSS_MUSIC = new PreBossLoopMusic(soundEvent, postBossMusic, mob, volume, pitch, withinRange);
                }
            } else {
                PRE_BOSS_MUSIC = null;
            }
            if (PRE_BOSS_MUSIC != null && !minecraft.getSoundManager().isActive(PRE_BOSS_MUSIC)) {
                Minecraft.getInstance().getSoundManager().play(PRE_BOSS_MUSIC);
            }
        }
    }

    public static void playBossMusic(SoundEvent soundEvent, Mob mob){
        playBossMusic(soundEvent, mob, 1.0F, 1.0F);
    }

    public static void playBossMusic(SoundEvent soundEvent, SoundEvent postBossMusic, Mob mob){
        playBossMusic(soundEvent, postBossMusic, mob, 1.0F, 1.0F);
    }

    public static void playBossMusic(SoundEvent soundEvent, Mob mob, float volume, float pitch){
        playBossMusic(soundEvent, ModSounds.BOSS_POST.get(), mob, volume, pitch);
    }

    public static void playBossMusic(SoundEvent soundEvent, SoundEvent postBossMusic, Mob mob, float volume, float pitch){
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.BossMusic, false)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (soundEvent != null && mob.isAlive()) {
                if (BOSS_MUSIC == null) {
                    BOSS_MUSIC = new BossLoopMusic(soundEvent, postBossMusic, mob, volume, pitch);
                }
            } else {
                BOSS_MUSIC = null;
            }
            if (BOSS_MUSIC != null && !minecraft.getSoundManager().isActive(BOSS_MUSIC)) {
                Minecraft.getInstance().getSoundManager().play(BOSS_MUSIC);
            }
        }
    }

    @SubscribeEvent
    public static void renderGlove(RenderArmEvent event){
        if (event.isCanceled() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(ItemConfig.FirstPersonGloves, false)){
            return;
        }

        // Curios is currently not on the compile classpath for the 1.21.1 NeoForge port.
        // TODO: Restore via optional (reflection) integration when Curios 1.21.1 is available.
    }

    @SubscribeEvent
    public static void renderArm(RenderArmEvent event){
        final AbstractClientPlayer player = event.getPlayer();
        if (!player.isSpectator() && LichdomHelper.isInLichMode(player)){
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            final int i = OverlayTexture.pack(OverlayTexture.u(0.0F), OverlayTexture.v(false));
            final ResourceLocation texture = Goety.location("textures/entity/lich.png");
            LichModeModel<?> lichModeModel = new LichModeModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModModelLayer.LICH));
            if (event.getArm() == HumanoidArm.RIGHT) {
                lichModeModel.rightArm.render(poseStack, event.getMultiBufferSource().getBuffer(RenderType.entityTranslucent(texture)), event.getPackedLight(), i);
                event.setCanceled(true);
            } else if (event.getArm() == HumanoidArm.LEFT) {
                lichModeModel.leftArm.render(poseStack, event.getMultiBufferSource().getBuffer(RenderType.entityTranslucent(texture)), event.getPackedLight(), i);
                event.setCanceled(true);
            }
            poseStack.popPose();
        }
        if (event.getPlayer().hasEffect(GoetyEffects.SHADOW_WALK)){
            if (event.getPlayer().getMainHandItem().isEmpty() && event.getArm() == event.getPlayer().getMainArm()){
                event.setCanceled(true);
            } else if (event.getPlayer().getOffhandItem().isEmpty() && event.getArm() != event.getPlayer().getMainArm()){
                event.setCanceled(true);
            }
        }
        if (SEHelper.hasCamera(event.getPlayer())){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderHand(RenderHandEvent event){
        final AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (SEHelper.hasCamera(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        final Player player = event.getEntity();
        if (player.hasEffect(GoetyEffects.SHADOW_WALK)){
            event.setCanceled(true);
        }
        if (player.isInvisible() && CuriosFinder.hasIllusionRobe(player)){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderLichHUD(final RenderGuiLayerEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        final Player player = minecraft.player;

        if (LichdomHelper.isLich(player)){
            if (event.getName().equals(VanillaGuiLayers.FOOD_LEVEL)){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderArcaAmount(final RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        final Player player = minecraft.player;

        if (player != null && event.getName().equals(VanillaGuiLayers.CROSSHAIR)) {
            HitResult hitResult = minecraft.hitResult;
            Font fontRenderer = minecraft.font;
            PoseStack poseStack = event.getGuiGraphics().pose();
            if (minecraft.level != null) {
                if (hitResult instanceof BlockHitResult blockRayTraceResult) {
                    BlockEntity blockEntity = minecraft.level.getBlockEntity(blockRayTraceResult.getBlockPos());
                    int width = minecraft.getWindow().getGuiScaledWidth();
                    int height = minecraft.getWindow().getGuiScaledHeight();
                    if (blockEntity instanceof ArcaBlockEntity arcaTile) {
                        if ((player.isShiftKeyDown() || player.isCrouching())) {
                            if (arcaTile.getPlayer() == player && SEHelper.getSEActive(player)) {
                                poseStack.pushPose();
                                poseStack.translate((float) (width / 2), (float) (height - 68), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                int SoulEnergy = SEHelper.getSESouls(player);
                                int SoulEnergyTotal = za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.MaxArcaSouls, 0);
                                String s = Component.translatable("tooltip.goety.blockSoul").getString() + SoulEnergy + "/" + SoulEnergyTotal;
                                int l = fontRenderer.width(s);
                                event.getGuiGraphics().drawString(fontRenderer, s, (-l / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();
                            } else if (arcaTile.getPlayer() != null) {
                                poseStack.pushPose();
                                poseStack.translate((float)(width / 2), (float)(height - 60), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                String s = Component.translatable("tooltip.goety.blockOwner").getString() + arcaTile.getPlayer().getDisplayName().getString();
                                int l = fontRenderer.width(s);
                                event.getGuiGraphics().drawString(fontRenderer, s, (-l / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();
                            }
                        }
                    } else if (blockEntity instanceof IOwnedBlock ownedBlock && ownedBlock.getPlayer() != null && ownedBlock.screenView()){
                        Player owner = ownedBlock.getPlayer();
                        if (owner != null) {
                            if (blockEntity instanceof ITrainingBlock trainingBlock) {
                                poseStack.pushPose();
                                poseStack.translate((float) (width / 2), (float) (height - 58), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                String s = Component.translatable("tooltip.goety.blockOwner").getString() + owner.getDisplayName().getString();
                                int l = fontRenderer.width(s);
                                event.getGuiGraphics().drawString(fontRenderer, s, (-l / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();

                                if (owner == player) {
                                    if (trainingBlock.reachedLimit()) {
                                        poseStack.pushPose();
                                        poseStack.translate((float) (width / 2), (float) (height - 116), 0.0F);
                                        RenderSystem.enableBlend();
                                        RenderSystem.defaultBlendFunc();
                                        String s0 = Component.translatable("info.goety.summon.limit").getString();
                                        int l0 = fontRenderer.width(s0);
                                        event.getGuiGraphics().drawString(fontRenderer, s0, (-l0 / 2), -4, 0xFFFFFF);
                                        RenderSystem.disableBlend();
                                        poseStack.popPose();
                                    }
                                    poseStack.pushPose();
                                    poseStack.translate((float) (width / 2), (float) (height - 100), 0.0F);
                                    RenderSystem.enableBlend();
                                    RenderSystem.defaultBlendFunc();
                                    String mode = Component.translatable("tooltip.goety.blockGuard").getString();
                                    if (!trainingBlock.isGuarding()) {
                                        mode = Component.translatable("tooltip.goety.blockFollow").getString();
                                    }
                                    int length = fontRenderer.width(mode);
                                    event.getGuiGraphics().drawString(fontRenderer, mode, (-length / 2), -4, 0xFFFFFF);
                                    RenderSystem.disableBlend();
                                    poseStack.popPose();
                                    if (trainingBlock.isSensorSensitive()) {
                                        poseStack.pushPose();
                                        poseStack.translate((float) (width / 2), (float) (height - 90), 0.0F);
                                        RenderSystem.enableBlend();
                                        RenderSystem.defaultBlendFunc();
                                        String s0 = Component.translatable("tooltip.goety.blockSense").getString();
                                        int l0 = fontRenderer.width(s0);
                                        event.getGuiGraphics().drawString(fontRenderer, s0, (-l0 / 2), -4, 0xFFFFFF);
                                        RenderSystem.disableBlend();
                                        poseStack.popPose();
                                    }
                                    if (trainingBlock.isGrounding()) {
                                        poseStack.pushPose();
                                        poseStack.translate((float) (width / 2), (float) (height - 46), 0.0F);
                                        RenderSystem.enableBlend();
                                        RenderSystem.defaultBlendFunc();
                                        String s0 = Component.translatable("tooltip.goety.blockGrounded").getString();
                                        int l0 = fontRenderer.width(s0);
                                        event.getGuiGraphics().drawString(fontRenderer, s0, (-l0 / 2), -4, 0xFFFFFF);
                                        RenderSystem.disableBlend();
                                        poseStack.popPose();
                                    }

                                    poseStack.pushPose();
                                    poseStack.translate((float) (width / 2), (float) (height - 68), 0.0F);
                                    RenderSystem.enableBlend();
                                    RenderSystem.defaultBlendFunc();
                                    String s1 = Component.translatable("tooltip.goety.blockTrain").getString() + trainingBlock.amountTrainLeft() + "/" + trainingBlock.maxTrainAmount() + " " + trainingBlock.getTrainMob().getDescription().getString();
                                    int l1 = fontRenderer.width(s1);
                                    event.getGuiGraphics().drawString(fontRenderer, s1, (-l1 / 2), -4, 0xFFFFFF);
                                    RenderSystem.disableBlend();
                                    poseStack.popPose();

                                    poseStack.pushPose();
                                    int train = 64;
                                    train *= ((double) trainingBlock.getTrainingTime() / trainingBlock.getMaxTrainTime());
                                    event.getGuiGraphics().blit(Goety.location("textures/gui/train_bar.png"), ((width - 64) / 2), (height - 86), 0, 0, 64, 16, 64, 32);
                                    event.getGuiGraphics().blit(Goety.location("textures/gui/train_bar.png"), ((width - 64) / 2), (height - 86), 0, 16, train, 16, 64, 32);
                                    poseStack.popPose();
                                }
                            } else if (blockEntity instanceof IBarrack barrack) {
                                poseStack.pushPose();
                                poseStack.translate((float) (width / 2), (float) (height - 58), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                String s = Component.translatable("tooltip.goety.blockOwner").getString() + owner.getDisplayName().getString();
                                int l = fontRenderer.width(s);
                                event.getGuiGraphics().drawString(fontRenderer, s, (-l / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();

                                poseStack.pushPose();
                                poseStack.translate((float) (width / 2), (float) (height - 68), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                String s1 = Component.translatable("tooltip.goety.blockTrainType").getString() + Component.translatable(barrack.getCurrentMob()).getString();
                                int l1 = fontRenderer.width(s1);
                                event.getGuiGraphics().drawString(fontRenderer, s1, (-l1 / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();

                                poseStack.pushPose();
                                poseStack.translate((float) (width / 2), (float) (height - 78), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                String s2 = Component.translatable("tooltip.goety.brew.capacity").getString() + barrack.getCurrentAmount() + "/" + barrack.trainLimit();
                                int l2 = fontRenderer.width(s2);
                                event.getGuiGraphics().drawString(fontRenderer, s2, (-l2 / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();
                            } else if (blockEntity instanceof OminousIdolBlockEntity idol) {
                                poseStack.pushPose();
                                poseStack.translate((float) (width / 2), (float) (height - 58), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                String s = Component.translatable("tooltip.goety.blockOwner").getString() + owner.getDisplayName().getString();
                                int l = fontRenderer.width(s);
                                event.getGuiGraphics().drawString(fontRenderer, s, (-l / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();

                                poseStack.pushPose();
                                poseStack.translate((float) (width / 2), (float) (height - 68), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                String s2 = Component.translatable("tooltip.goety.idol.count").getString() + idol.getClientCount() + "/" + za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.OminousIdolLimit, 0);
                                int l2 = fontRenderer.width(s2);
                                event.getGuiGraphics().drawString(fontRenderer, s2, (-l2 / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();
                            } else if ((player.isShiftKeyDown() || player.isCrouching()) && ownedBlock.getPlayer() != null) {
                                poseStack.pushPose();
                                poseStack.translate((float) (width / 2), (float) (height - 68), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                String s = Component.translatable("tooltip.goety.blockOwner").getString() + owner.getDisplayName().getString();
                                int l = fontRenderer.width(s);
                                event.getGuiGraphics().drawString(fontRenderer, s, (-l / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();
                            }
                        }
                    } else if (blockEntity instanceof CursedCageBlockEntity cageBlockEntity){
                        if (player.isShiftKeyDown() || player.isCrouching() && !cageBlockEntity.getItem().isEmpty()){
                            poseStack.pushPose();
                            poseStack.translate((float)(width / 2), (float)(height - 68), 0.0F);
                            RenderSystem.enableBlend();
                            RenderSystem.defaultBlendFunc();
                            String s = Component.translatable("tooltip.goety.blockSoul").getString() + cageBlockEntity.getSouls();
                            int l = fontRenderer.width(s);
                            event.getGuiGraphics().drawString(fontRenderer, s, (-l / 2), -4, 0xFFFFFF);
                            RenderSystem.disableBlend();
                            poseStack.popPose();
                        }
                    } else if (blockEntity instanceof BrewCauldronBlockEntity cauldronBlock){
                        if (player.isShiftKeyDown() || player.isCrouching()){
                            poseStack.pushPose();
                            poseStack.translate((float)(width / 2), (float)(height - 60), 0.0F);
                            RenderSystem.enableBlend();
                            RenderSystem.defaultBlendFunc();
                            String s1 = Component.translatable("tooltip.goety.brew.capacity").getString() + cauldronBlock.getCapacityUsed() + "/" + cauldronBlock.getCapacity();
                            int l2 = fontRenderer.width(s1);
                            event.getGuiGraphics().drawString(fontRenderer, s1, (-l2 / 2), -4, 0xFFFFFF);
                            RenderSystem.disableBlend();
                            poseStack.popPose();
                            poseStack.pushPose();
                            poseStack.translate((float)(width / 2), (float)(height - 68), 0.0F);
                            RenderSystem.enableBlend();
                            RenderSystem.defaultBlendFunc();
                            String s = Component.translatable("tooltip.goety.blockSoulCost").getString() + (cauldronBlock.getBrewCost() - cauldronBlock.soulTime);
                            int l = fontRenderer.width(s);
                            event.getGuiGraphics().drawString(fontRenderer, s, (-l / 2), -4, 0xFFFFFF);
                            RenderSystem.disableBlend();
                            poseStack.popPose();
                        }
                    } else if (blockEntity instanceof IWaystoneBlock waystoneBlock) {
                        GlobalPos globalPos = waystoneBlock.getPosition();
                        if ((player.isShiftKeyDown() || player.isCrouching()) && globalPos != null) {
                            poseStack.pushPose();
                            poseStack.translate((float) (width / 2), (float) (height - 60), 0.0F);
                            RenderSystem.enableBlend();
                            RenderSystem.defaultBlendFunc();
                            BlockPos blockPos = globalPos.pos();
                            String s1 = Component.translatable("tooltip.goety.arcaCoords", blockPos.getX(), blockPos.getY(), blockPos.getZ()).getString();
                            int l2 = fontRenderer.width(s1);
                            event.getGuiGraphics().drawString(fontRenderer, s1, (-l2 / 2), -4, 0xFFFFFF);
                            RenderSystem.disableBlend();
                            poseStack.popPose();
                            poseStack.pushPose();
                            poseStack.translate((float) (width / 2), (float) (height - 68), 0.0F);
                            RenderSystem.enableBlend();
                            RenderSystem.defaultBlendFunc();
                            String s = Component.translatable("tooltip.goety.arcaDimension", globalPos.dimension().location().toString()).getString();
                            int l = fontRenderer.width(s);
                            event.getGuiGraphics().drawString(fontRenderer, s, (-l / 2), -4, 0xFFFFFF);
                            RenderSystem.disableBlend();
                            poseStack.popPose();
                            if (waystoneBlock.getSoulCost() > 0) {
                                poseStack.pushPose();
                                poseStack.translate((float) (width / 2), (float) (height - 76), 0.0F);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                String s0 = Component.translatable("tooltip.goety.blockSoulCost").getString() + waystoneBlock.getSoulCost();
                                int l0 = fontRenderer.width(s0);
                                event.getGuiGraphics().drawString(fontRenderer, s0, (-l0 / 2), -4, 0xFFFFFF);
                                RenderSystem.disableBlend();
                                poseStack.popPose();
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void RenderWorldLast(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        Level level = minecraft.level;
        if (level != null) {
            List<? extends Player> players = level.players();
            if (player != null) {
                Level world = player.level();
                ItemStack stack = player.getMainHandItem();
                Map<BlockPos, ColorUtil> renderCubes = new HashMap<>();
                if (stack.getItem() instanceof WaystoneItem) {
                    net.minecraft.world.item.component.CustomData tag = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
                    if (tag != null && tag.contains("Display")) {
                        GlobalPos loc = WaystoneItem.getPosition(tag.copyTag());
                        if (loc != null) {
                            if (loc.dimension() == world.dimension()) {
                                renderCubes.put(loc.pos(), new ColorUtil(ChatFormatting.GOLD));
                            }
                        }
                    }
                }
                if (!renderCubes.keySet().isEmpty()) {
                    PoseStack matrix = event.getPoseStack();
                    Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                    RenderBlockUtils.renderColourCubes(matrix, view, renderCubes, 1.0F, 1.0F);
                }
                for (Player player1 : players) {
                    if (player1.distanceToSqr(player) > 500.0F) {
                        continue;
                    }

                    if (player1.isUsingItem()) {
                        if (WandUtil.getSpell(player1) instanceof BurrowingSpell) {
                            BurrowingLaserRenderer.renderLaser(event, player1, event.getPartialTick().getGameTimeDeltaPartialTick(false));
                        } else if (WandUtil.getSpell(player1) instanceof PrismaBeamSpell) {
                            GuardianLaserRenderer.renderLaser(event, player1, event.getPartialTick().getGameTimeDeltaPartialTick(false));
                        }
                    }
                }
            }
        }
    }

    // Custom heart rendering used the pre-1.21 overlay API (RenderGuiOverlayEvent/NeoForgeGui).
    // NeoForge 21.1+ uses GUI layers (RegisterGuiLayersEvent/RenderGuiLayerEvent) and vanilla GUI internals.
    // TODO(1.21): port if needed.
    private static boolean prevJumpBindState = false;

    @SubscribeEvent
    public static void TickEventsPre(ClientTickEvent.Pre event) {
        CustomItemsRenderer.incrementTick();
    }

    @SubscribeEvent
    public static void TickEventsPost(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null){
            Player player = minecraft.player;
            Wight wight = Wight.findWight(player);
            if (wight != null) {
                if (MobUtil.isPlayerLookingTowards(player, minecraft.options.fov().get().floatValue(), wight)){
                    wight.lookTime += 1;

                    if (wight.lookTime >= MathHelper.secondsToTicks(3)){
                        if (wight.lookTime % 20 == 0 && wight.getRandom().nextInt(8) == 0) {
                            wight.lookTime = 0;
                            PacketDistributor.sendToServer(new CTargetPlayerPacket(wight));
                        }
                    }
                } else {
                    if (wight.lookTime > 0){
                        wight.lookTime -= 1;
                    }
                }
            }
            if (minecraft.options.keyJump.isDown() && !prevJumpBindState && !player.isInWater() && SEHelper.getTicksInAir(player) > 2 && !player.isCreative() && !player.isSpectator() && !player.isPassenger()) {
                PacketDistributor.sendToServer(new CMultiJumpPacket());
                SEHelper.doubleJump(player);
            }
            prevJumpBindState = minecraft.options.keyJump.isDown();
        }
    }

    @SubscribeEvent
    public static void FogEvents(ViewportEvent.RenderFog event){
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            Player player = minecraft.player;
            Wight wight = Wight.findWight(player, EntitySelector.ENTITY_STILL_ALIVE::test);
            if (wight != null) {
                final float f = minecraft.gameRenderer.getRenderDistance();
                event.setNearPlaneDistance(f * 0.05F);
                event.setFarPlaneDistance(Math.min(f, 192.0F) * 0.5F);
                event.setCanceled(true);
            }
        }
    }

    /**
     * From here, code is modified and based of @gigaherz ClientEvents codes: <a href="https://github.com/gigaherz/ToolBelt/blob/master/src/main/java/dev/gigaherz/toolbelt/client/ClientEvents.java">...</a>
     * */
    public static void wipeOpen() {
        if (ModKeybindings.wandCircle() != null) {
            while (ModKeybindings.wandCircle().consumeClick()) {
            }
        }
        if (ModKeybindings.brewCircle() != null) {
            while (ModKeybindings.brewCircle().consumeClick()) {
            }
        }
    }

    private static boolean toolMenuKeyWasDown = false;

    @SubscribeEvent
    public static void handleKeys(ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.screen == null && ModKeybindings.wandCircle() != null && ModKeybindings.brewCircle() != null) {
            boolean toolMenuKeyIsDown = ModKeybindings.wandCircle().isDown() || ModKeybindings.brewCircle().isDown();
            boolean wandCircle = ModKeybindings.wandCircle().isDown();
            boolean brewCircle = ModKeybindings.brewCircle().isDown();
            if (toolMenuKeyIsDown && !toolMenuKeyWasDown) {
                if (wandCircle) {
                    while (ModKeybindings.wandCircle().consumeClick()) {
                        if (minecraft.screen == null && minecraft.player != null) {
                            ItemStack inHand = WandUtil.findWand(minecraft.player);
                            if (!inHand.isEmpty() && ((TotemFinder.canOpenWandCircle(minecraft.player)))) {
                                minecraft.setScreen(new FocusRadialMenuScreen());
                            }
                        }
                    }
                } else if (brewCircle){
                    while (ModKeybindings.brewCircle().consumeClick()) {
                        if (minecraft.screen == null && minecraft.player != null) {
                            if (CuriosFinder.hasBrewInBag(minecraft.player)) {
                                minecraft.setScreen(new BrewRadialMenuScreen());
                            }
                        }
                    }
                }
            }
            toolMenuKeyWasDown = toolMenuKeyIsDown;
        } else {
            toolMenuKeyWasDown = true;
        }
    }


    public static boolean isKeyDown0(KeyMapping keybind) {
        if (keybind.isUnbound()) {
            return false;
        }

        return switch (keybind.getKey().getType()) {
            case KEYSYM -> InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue());
            case MOUSE -> GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue()) == GLFW.GLFW_PRESS;
            default -> false;
        };
    }

    public static boolean isKeyDown(KeyMapping keybind) {
        if (keybind.isUnbound()) {
            return false;
        }

        return isKeyDown0(keybind) && keybind.getKeyConflictContext().isActive() && keybind.getKeyModifier().isActive(keybind.getKeyConflictContext());
    }

    @SubscribeEvent
    public static void updateInputEvent(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        Input input = event.getInput();
        if (player instanceof LocalPlayer localPlayer) {
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.WheelGuiMovement, false)) {
                if (Minecraft.getInstance().screen instanceof FocusRadialMenuScreen || Minecraft.getInstance().screen instanceof BrewRadialMenuScreen) {
                    Options settings = Minecraft.getInstance().options;
                    input.up = isKeyDown0(settings.keyUp);
                    input.down = isKeyDown0(settings.keyDown);
                    input.left = isKeyDown0(settings.keyLeft);
                    input.right = isKeyDown0(settings.keyRight);

                    input.forwardImpulse = input.up == input.down ? 0.0F : (input.up ? 1.0F : -1.0F);
                    input.leftImpulse = input.left == input.right ? 0.0F : (input.left ? 1.0F : -1.0F);
                    input.jumping = isKeyDown0(settings.keyJump);
                    input.shiftKeyDown = isKeyDown0(settings.keyShift);
                    if (localPlayer.isMovingSlowly()) {
                        input.leftImpulse = (float) ((double) input.leftImpulse * 0.3D);
                        input.forwardImpulse = (float) ((double) input.forwardImpulse * 0.3D);
                    }
                }
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(SpellConfig.FullStopCast, false)) {
                if (localPlayer.isUsingItem() && !localPlayer.isPassenger()) {
                    if (MobUtil.isSpellCasting(localPlayer)) {
                        input.leftImpulse = 0.0F;
                        input.forwardImpulse = 0.0F;
                        input.jumping = false;
                    }
                }
            }
        }
    }
    /**
     * To Here
     * */

    @SubscribeEvent
    public static void KeyInputs(InputEvent.Key event) {
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.WheelGuiMovement, false)) {
            if (MINECRAFT.screen instanceof FocusRadialMenuScreen || MINECRAFT.screen instanceof BrewRadialMenuScreen) {
                InputConstants.Key inputconstants$key = InputConstants.getKey(event.getKey(), event.getScanCode());
                if (event.getAction() == 0) {
                    KeyMapping.set(inputconstants$key, false);
                    if (event.getKey() == 292) {
                        // Debug rendering options API changed in 1.21.1 - these properties may have been removed
                        // MINECRAFT.options.renderDebug = !MINECRAFT.options.renderDebug;
                        // MINECRAFT.options.renderDebugCharts = MINECRAFT.options.renderDebug && Screen.hasShiftDown();
                        // MINECRAFT.options.renderFpsChart = MINECRAFT.options.renderDebug && Screen.hasAltDown();
                    }
                } else {
                    if (event.getKey() == 293 && MINECRAFT.gameRenderer != null) {
                        MINECRAFT.gameRenderer.togglePostEffect();
                    }

                    boolean flag3 = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292);
                    if (event.getKey() == 256) {
                        boolean flag2 = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292);
                        MINECRAFT.pauseGame(flag2);
                    }

                    if (event.getKey() == 290) {
                        MINECRAFT.options.hideGui = !MINECRAFT.options.hideGui;
                    }

                    if (flag3) {
                        KeyMapping.set(inputconstants$key, false);
                    } else {
                        KeyMapping.set(inputconstants$key, true);
                        KeyMapping.click(inputconstants$key);
                    }

                    // Debug charts API changed in 1.21.1
                    // if (MINECRAFT.options.renderDebugCharts && event.getKey() >= 48 && event.getKey() <= 57) {
                    //     MINECRAFT.debugFpsMeterKeyPress(event.getKey() - 48);
                    // }
                }
            }
        }

        if (ModKeybindings.keyBindings[0].isDown() && MINECRAFT.isWindowActive()){
            Goety.LOGGER.debug("[Goety] Z pressed -> sending CWandKeyPacket");
            PacketDistributor.sendToServer(new CWandKeyPacket());
        }
        if (ModKeybindings.keyBindings[2].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CBagKeyPacket());
        }
        if (ModKeybindings.keyBindings[3].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CWitchRobePacket());
        }
        if (ModKeybindings.keyBindings[4].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CStopAttackPacket());
        }
        if (ModKeybindings.keyBindings[5].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CMagnetPacket());
        }
        if (ModKeybindings.keyBindings[6].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CSetLichNightVisionMode());
            if (MINECRAFT.player != null && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichNightVision, false)){
                if (LichdomHelper.isLich(MINECRAFT.player)){
                    MINECRAFT.player.playSound(SoundEvents.END_PORTAL_FRAME_FILL);
                }
            }
        }
        if (ModKeybindings.keyBindings[7].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CExtractPotionKeyPacket());
        }
        if (ModKeybindings.keyBindings[8].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CBrewBagKeyPacket());
        }
        if (ModKeybindings.keyBindings[10].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CRavagerRoarPacket());
        }
        if (ModKeybindings.keyBindings[11].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CAutoRideablePacket());
        }
        if (ModKeybindings.keyBindings[12].isDown() && MINECRAFT.isWindowActive()){
            if (MINECRAFT.player != null){
                if (LichdomHelper.isLich(MINECRAFT.player)){
                    LichdomHelper.setLichMode(MINECRAFT.player, !LichdomHelper.isInLichMode(MINECRAFT.player));
                    if (!LichdomHelper.isInLichMode(MINECRAFT.player)) {
                        MINECRAFT.player.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
                    } else {
                        if (MINECRAFT.level != null) {
                            for (int i = 0; i < 5; ++i) {
                                double d0 = MINECRAFT.level.random.nextGaussian() * 0.02D;
                                double d1 = MINECRAFT.level.random.nextGaussian() * 0.02D;
                                double d2 = MINECRAFT.level.random.nextGaussian() * 0.02D;
                                MINECRAFT.level.addParticle(ParticleTypes.SCULK_SOUL, MINECRAFT.player.getRandomX(1.0D), MINECRAFT.player.getRandomY() + 1.0D, MINECRAFT.player.getRandomZ(1.0D), d0, d1, d2);
                            }
                        }
                        MINECRAFT.player.playSound(ModSounds.SOUL_EXPLODE.get(), 1.0F, 0.75F);
                    }
                    PacketDistributor.sendToServer(new CSetLichMode());
                }
            }
        }
        if (ModKeybindings.keyBindings[13].isDown() && MINECRAFT.isWindowActive()){
            if (MINECRAFT.player != null) {
                if (LichdomHelper.isLich(MINECRAFT.player)) {
                    if (LichdomHelper.isInLichMode(MINECRAFT.player)){
                        MINECRAFT.player.level().playLocalSound(MINECRAFT.player.getX(), MINECRAFT.player.getY(), MINECRAFT.player.getZ(), ModSounds.LICH_LAUGH.get(), MINECRAFT.player.getSoundSource(), 2.0F, MINECRAFT.player.getVoicePitch(), false);
                    }
                }
            }
        }
        if (ModKeybindings.keyBindings[14].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CActivateCurioKeyPacket());
        }
        if (ModKeybindings.keyBindings[15].isDown() && MINECRAFT.isWindowActive()){
            PacketDistributor.sendToServer(new CDismissServantsPacket());
        }
    }

    //Domestication Innovation work-a-round
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void InteractionKeyEvent(InputEvent.InteractionKeyMappingTriggered event) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (event.isAttack()
                    && Minecraft.getInstance().hitResult instanceof EntityHitResult result
                    && result.getEntity() instanceof IOwned owned
                    && owned.getTrueOwner() == player) {
                MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
                ClientPacketListener listener = Minecraft.getInstance().getConnection();
                if (gameMode != null && listener != null) {
                    ItemStack stack = player.getMainHandItem();
                    if (stack.getItem().onLeftClickEntity(stack, player, result.getEntity())) {
                        listener.send(ServerboundInteractPacket.createAttackPacket(result.getEntity(), player.isShiftKeyDown()));
                        if (gameMode.getPlayerMode() != GameType.SPECTATOR) {
                            player.attack(result.getEntity());
                        }
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends LivingEntity> void followBodyRotations(final T livingEntity, final HumanoidModel<T> model) {
        EntityRenderer<? super T> render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);
        if (render instanceof LivingEntityRenderer) {
            LivingEntityRenderer<T, EntityModel<T>> livingRenderer = (LivingEntityRenderer<T, EntityModel<T>>) render;
            EntityModel<T> entityModel = livingRenderer.getModel();
            if (entityModel instanceof HumanoidModel<T> humanoidModel) {
                humanoidModel.copyPropertiesTo(model);
            }
        }
    }
}
