package za.co.infernos.goety.common.network;

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
import za.co.infernos.goety.common.capabilities.lichdom.LichUpdatePacket;
import za.co.infernos.goety.common.capabilities.misc.MiscCapUpdatePacket;
import za.co.infernos.goety.common.capabilities.soulenergy.SEUpdatePacket;
import za.co.infernos.goety.common.capabilities.witchbarter.WBUpdatePacket;
import za.co.infernos.goety.common.network.client.CActivateCurioKeyPacket;
import za.co.infernos.goety.common.network.client.CAutoRideablePacket;
import za.co.infernos.goety.common.network.client.CBagKeyPacket;
import za.co.infernos.goety.common.network.client.CDismissServantsPacket;
import za.co.infernos.goety.common.network.client.CExtractPotionKeyPacket;
import za.co.infernos.goety.common.network.client.CMagnetPacket;
import za.co.infernos.goety.common.network.client.CMultiJumpPacket;
import za.co.infernos.goety.common.network.client.CRavagerRoarPacket;
import za.co.infernos.goety.common.network.client.CScytheStrikePacket;
import za.co.infernos.goety.common.network.client.CSetLichMode;
import za.co.infernos.goety.common.network.client.CSetLichNightVisionMode;
import za.co.infernos.goety.common.network.client.CStopAttackPacket;
import za.co.infernos.goety.common.network.client.CTargetPlayerPacket;
import za.co.infernos.goety.common.network.client.CWandKeyPacket;
import za.co.infernos.goety.common.network.client.CWitchRobePacket;
import za.co.infernos.goety.common.network.client.brew.CBrewBagKeyPacket;
import za.co.infernos.goety.common.network.client.focus.CAddFocusToBagPacket;
import za.co.infernos.goety.common.network.client.focus.CAddFocusToInventoryPacket;
import za.co.infernos.goety.common.network.client.focus.CSwapFocusPacket;
import za.co.infernos.goety.common.network.client.focus.CSwapFocusTwoPacket;
import za.co.infernos.goety.common.network.server.SFocusCooldownPacket;
import za.co.infernos.goety.common.network.server.SPlayEntitySoundPacket;
import za.co.infernos.goety.common.network.server.SPlayPlayerSoundPacket;

/**
 * NeoForge 1.21+ networking uses the payload system (CustomPacketPayload + StreamCodec) registered via
 * {@link RegisterPayloadHandlersEvent}. This class registers all Goety payloads and exposes
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
    }

    // ---------------------------------------------------------------------
    // Send helpers — PacketDistributor wrappers used across the codebase.
    // Caller must pass a registered CustomPacketPayload; passing anything
    // else is a programming error.
    // ---------------------------------------------------------------------

    public static void sendTo(Player player, Object msg) {
        if (player instanceof ServerPlayer sp && msg instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToPlayer(sp, payload);
        }
    }

    public static void sendToServer(Object msg) {
        if (msg instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToServer(payload);
        }
    }

    public static void sentToTrackingChunk(LevelChunk chunk, Object msg) {
        if (msg instanceof CustomPacketPayload payload && chunk.getLevel() instanceof ServerLevel sl) {
            PacketDistributor.sendToPlayersTrackingChunk(sl, chunk.getPos(), payload);
        }
    }

    public static void sentToTrackingEntity(Entity entity, Object msg) {
        if (msg instanceof CustomPacketPayload payload && !(entity.level() instanceof Level lvl ? lvl.isClientSide : true)) {
            PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
        }
    }

    public static void sentToTrackingEntityAndPlayer(Entity entity, Object msg) {
        if (msg instanceof CustomPacketPayload payload && !(entity.level() instanceof Level lvl ? lvl.isClientSide : true)) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, payload);
        }
    }

    public static void sendToALL(Object msg) {
        if (msg instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToAllPlayers(payload);
        }
    }

    public static void sendToClient(ServerPlayer player, Object msg) {
        if (msg instanceof CustomPacketPayload payload) {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }
}
