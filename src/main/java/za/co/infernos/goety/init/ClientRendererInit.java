package za.co.infernos.goety.init;

import za.co.infernos.goety.client.render.ModPlayerRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import org.jetbrains.annotations.UnknownNullability;

public class ClientRendererInit {
    private ModPlayerRenderer renderer;

    private static ClientRendererInit INSTANCE;
    public static ClientRendererInit getInstance() {
        return INSTANCE;
    }

    public ClientRendererInit() {
        INSTANCE = this;
    }

    @UnknownNullability
    public ModPlayerRenderer getModPlayerRenderer() {
        return this.renderer;
    }

    @SubscribeEvent
    public void onAddLayers(EntityRenderersEvent.AddLayers event) {
        this.renderer = new ModPlayerRenderer(event.getContext());
    }
}