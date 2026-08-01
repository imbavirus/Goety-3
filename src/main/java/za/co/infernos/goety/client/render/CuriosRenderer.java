package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.*;
import za.co.infernos.goety.common.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;

public class CuriosRenderer {
    public static String folderPath = "textures/models/curios/";

    public static ResourceLocation render(String textureName){
        return Goety.location(folderPath + textureName);
    }

    public static void register() {
        // Curios is currently not on the compile classpath for the 1.21.1 NeoForge port.
        // TODO: Restore Curios render registration when Curios 1.21.1 is available.
    }

    public static ModelPart bakeLayer(ModelLayerLocation layerLocation) {
        return Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation);
    }
}