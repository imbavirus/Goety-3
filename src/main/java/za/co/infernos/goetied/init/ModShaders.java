package za.co.infernos.goetied.init;

import za.co.infernos.goetied.Goetied;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.io.IOException;

@EventBusSubscriber(modid = Goetied.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ModShaders {

    private static ShaderInstance holeShader;

    @SubscribeEvent
    static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(Goetied.MOD_ID, "hole"),
                        DefaultVertexFormat.POSITION),
                shader -> holeShader = shader
        );
    }

    public static ShaderInstance getHoleShader() {
        return holeShader;
    }

}
