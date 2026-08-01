package za.co.infernos.goety.client.render.layer;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.ModRenderType;
import za.co.infernos.goety.client.render.model.WraithModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.world.entity.Mob;

public class WraithGlowLayer<T extends Mob, M extends WraithModel<T>> extends EyesLayer<T, M> {
    private static final RenderType RENDER_TYPE = ModRenderType.wraith(Goety.location("textures/entity/wraith/wraith_glow.png"));

    public WraithGlowLayer(RenderLayerParent<T, M> p_i50919_1_) {
        super(p_i50919_1_);
    }

    @Override
    public RenderType renderType() {
        return RENDER_TYPE;
    }
}