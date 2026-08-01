package za.co.infernos.goetied.init;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.capabilities.lichdom.ILichdom;
import za.co.infernos.goetied.common.capabilities.lichdom.LichProvider;
import za.co.infernos.goetied.common.capabilities.misc.IMisc;
import za.co.infernos.goetied.common.capabilities.misc.MiscProvider;
import za.co.infernos.goetied.common.capabilities.soulenergy.ISoulEnergy;
import za.co.infernos.goetied.common.capabilities.soulenergy.SEProvider;
import za.co.infernos.goetied.common.capabilities.witchbarter.IWitchBarter;
import za.co.infernos.goetied.common.capabilities.witchbarter.WitchBarterProvider;
import za.co.infernos.goetied.common.commands.GoetiedCommand;
import za.co.infernos.goetied.common.commands.LichCommand;
import za.co.infernos.goetied.common.entities.hostile.cultists.Cultist;
import za.co.infernos.goetied.common.listeners.IllagerAssaultListener;
import za.co.infernos.goetied.common.listeners.SoulTakenListener;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Goetied.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class InitEvents {

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        LichCommand.register(commandDispatcher);
        GoetiedCommand.register(commandDispatcher, event.getBuildContext());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        // TODO NeoForge 1.21: restore explicit capability registration wiring.
    }

    @SubscribeEvent
    public static void registerListeners(AddReloadListenerEvent event) {
        event.addListener(new IllagerAssaultListener());
        event.addListener(new SoulTakenListener());
    }
}