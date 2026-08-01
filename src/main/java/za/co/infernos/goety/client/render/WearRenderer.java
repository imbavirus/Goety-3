package za.co.infernos.goety.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

/**
 * Temporary stub: Curios is not available on the compile classpath for the 1.21.1 NeoForge port.
 * This keeps references compiling; rendering will be re-enabled once Curios 1.21.1 is available.
 */
public final class WearRenderer {
    private WearRenderer() {}

    public static WearRenderer getRenderer(ItemStack stack) {
        return null;
    }

    public void renderFirstPersonArm(PoseStack matrixStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, HumanoidArm side, boolean hasFoil) {
        // no-op
    }

    public static ResourceLocation getDefaultTexture() {
        return CuriosRenderer.render("missing.png");
    }
}