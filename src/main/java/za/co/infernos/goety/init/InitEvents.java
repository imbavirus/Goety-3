package za.co.infernos.goety.init;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.capabilities.lichdom.ILichdom;
import za.co.infernos.goety.common.capabilities.lichdom.LichProvider;
import za.co.infernos.goety.common.capabilities.misc.IMisc;
import za.co.infernos.goety.common.capabilities.misc.MiscProvider;
import za.co.infernos.goety.common.capabilities.soulenergy.ISoulEnergy;
import za.co.infernos.goety.common.capabilities.soulenergy.SEProvider;
import za.co.infernos.goety.common.capabilities.witchbarter.IWitchBarter;
import za.co.infernos.goety.common.capabilities.witchbarter.WitchBarterProvider;
import za.co.infernos.goety.common.commands.GoetyCommand;
import za.co.infernos.goety.common.commands.LichCommand;
import za.co.infernos.goety.common.entities.hostile.cultists.Cultist;
import za.co.infernos.goety.common.listeners.IllagerAssaultListener;
import za.co.infernos.goety.common.listeners.SoulTakenListener;
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

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class InitEvents {

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        LichCommand.register(commandDispatcher);
        GoetyCommand.register(commandDispatcher, event.getBuildContext());
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