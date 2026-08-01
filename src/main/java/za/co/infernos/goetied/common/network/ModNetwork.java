package za.co.infernos.goetied.common.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import za.co.infernos.goetied.common.capabilities.lichdom.LichUpdatePacket;
import za.co.infernos.goetied.common.capabilities.misc.MiscCapUpdatePacket;
import za.co.infernos.goetied.common.capabilities.soulenergy.SEUpdatePacket;
import za.co.infernos.goetied.common.capabilities.witchbarter.WBUpdatePacket;
import za.co.infernos.goetied.common.network.client.CActivateCurioKeyPacket;
import za.co.infernos.goetied.common.network.client.CAutoRideablePacket;
import za.co.infernos.goetied.common.network.client.CBagKeyPacket;
import za.co.infernos.goetied.common.network.client.CDismissServantsPacket;
import za.co.infernos.goetied.common.network.client.CExtractPotionKeyPacket;
import za.co.infernos.goetied.common.network.client.CMagnetPacket;
import za.co.infernos.goetied.common.network.client.CMultiJumpPacket;
import za.co.infernos.goetied.common.network.client.CRavagerRoarPacket;
import za.co.infernos.goetied.common.network.client.CScytheStrikePacket;
import za.co.infernos.goetied.common.network.client.CSetLichMode;
import za.co.infernos.goetied.common.network.client.CSetLichNightVisionMode;
import za.co.infernos.goetied.common.network.client.CStopAttackPacket;
import za.co.infernos.goetied.common.network.client.CTargetPlayerPacket;
import za.co.infernos.goetied.common.network.client.CWandKeyPacket;
import za.co.infernos.goetied.common.network.client.CWitchRobePacket;
import za.co.infernos.goetied.common.network.client.brew.CBrewBagKeyPacket;
import za.co.infernos.goetied.common.network.client.focus.CAddFocusToBagPacket;
import za.co.infernos.goetied.common.network.client.focus.CAddFocusToInventoryPacket;
import za.co.infernos.goetied.common.network.client.focus.CSwapFocusPacket;
import za.co.infernos.goetied.common.network.client.focus.CSwapFocusTwoPacket;
import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.network.server.SFocusCooldownPacket;
import za.co.infernos.goetied.common.network.server.SPlayEntitySoundPacket;
import za.co.infernos.goetied.common.network.server.SPlayPlayerSoundPacket;
import za.co.infernos.goetied.common.network.server.SPlayWorldSoundPacket;

/**
 * NeoForge 1.21+ networking uses the payload system (CustomPacketPayload + StreamCodec) registered via
 * {@link RegisterPayloadHandlersEvent}. This class registers all Goetied payloads and exposes
 * convenience send helpers backed by {@link PacketDistributor}.
 */
public class ModNetwork {
    public static final String VERSION = "1";

    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(VERSION);

        // Serverbound (client -> server) keypackets
        registrar.playToServer(CWandKeyPacket.TYPE, CWandKeyPacket.STREAM_CODEC, CWandKeyPacket::handle);
        registrar.playToServer(CBagKeyPacket.TYPE, CBagKeyPacket.STREAM_CODEC, CBagKeyPacket::handle);
        registrar.playToServer(CWitchRobePacket.TYPE, CWitchRobePacket.STREAM_CODEC, CWitchRobePacket::handle);
        registrar.playToServer(CStopAttackPacket.TYPE, CStopAttackPacket.STREAM_CODEC, CStopAttackPacket::handle);
        registrar.playToServer(CMagnetPacket.TYPE, CMagnetPacket.STREAM_CODEC, CMagnetPacket::handle);
        registrar.playToServer(CSetLichNightVisionMode.TYPE, CSetLichNightVisionMode.STREAM_CODEC, CSetLichNightVisionMode::handle);
        registrar.playToServer(CExtractPotionKeyPacket.TYPE, CExtractPotionKeyPacket.STREAM_CODEC, CExtractPotionKeyPacket::handle);
        registrar.playToServer(CBrewBagKeyPacket.TYPE, CBrewBagKeyPacket.STREAM_CODEC, CBrewBagKeyPacket::handle);
        registrar.playToServer(CRavagerRoarPacket.TYPE, CRavagerRoarPacket.STREAM_CODEC, CRavagerRoarPacket::handle);
        registrar.playToServer(CAutoRideablePacket.TYPE, CAutoRideablePacket.STREAM_CODEC, CAutoRideablePacket::handle);
        registrar.playToServer(CSetLichMode.TYPE, CSetLichMode.STREAM_CODEC, CSetLichMode::handle);
        registrar.playToServer(CActivateCurioKeyPacket.TYPE, CActivateCurioKeyPacket.STREAM_CODEC, CActivateCurioKeyPacket::handle);
        registrar.playToServer(CDismissServantsPacket.TYPE, CDismissServantsPacket.STREAM_CODEC, CDismissServantsPacket::handle);
        registrar.playToServer(CTargetPlayerPacket.TYPE, CTargetPlayerPacket.STREAM_CODEC, CTargetPlayerPacket::handle);
        registrar.playToServer(CMultiJumpPacket.TYPE, CMultiJumpPacket.STREAM_CODEC, CMultiJumpPacket::handle);
        registrar.playToServer(CScytheStrikePacket.TYPE, CScytheStrikePacket.STREAM_CODEC, CScytheStrikePacket::handle);
        registrar.playToServer(CAddFocusToBagPacket.TYPE, CAddFocusToBagPacket.STREAM_CODEC, CAddFocusToBagPacket::handle);
        registrar.playToServer(CAddFocusToInventoryPacket.TYPE, CAddFocusToInventoryPacket.STREAM_CODEC, CAddFocusToInventoryPacket::handle);
        registrar.playToServer(CSwapFocusPacket.TYPE, CSwapFocusPacket.STREAM_CODEC, CSwapFocusPacket::handle);
        registrar.playToServer(CSwapFocusTwoPacket.TYPE, CSwapFocusTwoPacket.STREAM_CODEC, CSwapFocusTwoPacket::handle);

        // Clientbound (server -> client)
        registrar.playToClient(SEUpdatePacket.TYPE, SEUpdatePacket.STREAM_CODEC, SEUpdatePacket::handle);
        registrar.playToClient(SFocusCooldownPacket.TYPE, SFocusCooldownPacket.STREAM_CODEC, SFocusCooldownPacket::handle);
        registrar.playToClient(LichUpdatePacket.TYPE, LichUpdatePacket.STREAM_CODEC, LichUpdatePacket::handle);
        registrar.playToClient(MiscCapUpdatePacket.TYPE, MiscCapUpdatePacket.STREAM_CODEC, MiscCapUpdatePacket::handle);
        registrar.playToClient(WBUpdatePacket.TYPE, WBUpdatePacket.STREAM_CODEC, WBUpdatePacket::handle);
        registrar.playToClient(SPlayPlayerSoundPacket.TYPE, SPlayPlayerSoundPacket.STREAM_CODEC, SPlayPlayerSoundPacket::handle);
        registrar.playToClient(SPlayEntitySoundPacket.TYPE, SPlayEntitySoundPacket.STREAM_CODEC, SPlayEntitySoundPacket::handle);
        registrar.playToClient(SPlayWorldSoundPacket.TYPE, SPlayWorldSoundPacket.STREAM_CODEC, SPlayWorldSoundPacket::handle);
    }

    // ---------------------------------------------------------------------
    // Send helpers — PacketDistributor wrappers used across the codebase.
    // Caller must pass a registered CustomPacketPayload; passing anything
    // else is a programming error.
    // ---------------------------------------------------------------------

    public static void sendTo(Player player, Object msg) {
        if (player instanceof ServerPlayer sp && msg instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToPlayer(sp, payload);
        } else {
            warnBadPayload("sendTo", msg);
        }
    }

    public static void sendToServer(Object msg) {
        if (msg instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToServer(payload);
        } else {
            warnBadPayload("sendToServer", msg);
        }
    }

    public static void sentToTrackingChunk(LevelChunk chunk, Object msg) {
        if (msg instanceof CustomPacketPayload payload && chunk.getLevel() instanceof ServerLevel sl) {
            PacketDistributor.sendToPlayersTrackingChunk(sl, chunk.getPos(), payload);
        } else {
            warnBadPayload("sentToTrackingChunk", msg);
        }
    }

    public static void sentToTrackingEntity(Entity entity, Object msg) {
        if (msg instanceof CustomPacketPayload payload && !(entity.level() instanceof Level lvl ? lvl.isClientSide : true)) {
            PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
        } else if (!(msg instanceof CustomPacketPayload)) {
            warnBadPayload("sentToTrackingEntity", msg);
        }
    }

    public static void sentToTrackingEntityAndPlayer(Entity entity, Object msg) {
        if (msg instanceof CustomPacketPayload payload && !(entity.level() instanceof Level lvl ? lvl.isClientSide : true)) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, payload);
        } else if (!(msg instanceof CustomPacketPayload)) {
            warnBadPayload("sentToTrackingEntityAndPlayer", msg);
        }
    }

    public static void sendToALL(Object msg) {
        if (msg instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToAllPlayers(payload);
        } else {
            warnBadPayload("sendToALL", msg);
        }
    }

    public static void sendToClient(ServerPlayer player, Object msg) {
        if (msg instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToPlayer(player, payload);
        } else {
            warnBadPayload("sendToClient", msg);
        }
    }

    private static void warnBadPayload(String method, Object msg) {
        Goetied.LOGGER.error("ModNetwork.{} expected CustomPacketPayload, got {}", method,
                msg == null ? "null" : msg.getClass().getName());
    }
}
