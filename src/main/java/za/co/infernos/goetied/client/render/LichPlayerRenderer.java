package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.utils.LichdomHelper;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

import java.util.Optional;

/**
 * Based on @TeamLapen's Werewolf Rendering codes: <a href="https://github.com/TeamLapen/Werewolves/blob/1.20/src/main/java/de/teamlapen/werewolves/client/render/WerewolfPlayerRenderer.java">...</a>
 */
public abstract class LichPlayerRenderer <T extends AbstractClientPlayer, E extends PlayerModel<T>> extends PlayerRenderer {

    public static Optional<String> getLichRenderer(AbstractClientPlayer player) {
        if (LichdomHelper.isLich(player)) {
            if (LichdomHelper.isInLichMode(player)) {
                return Optional.of(Goetied.MOD_ID +":lich");
            }
        }
        return Optional.empty();
    }

    public LichPlayerRenderer(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model) {
        super(context, true);
        this.model = model;
    }
}