package za.co.infernos.goety.client;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.init.ClientSideInit;

/**
 * Entry point for client-only proxy and sided init.
 * <p>
 * Kept in the client package and only invoked when
 * {@code FMLEnvironment.dist == Dist.CLIENT} so dedicated servers never
 * resolve {@link ClientProxy} or {@link ClientSideInit}.
 */
public final class ClientBootstrap {
    private ClientBootstrap() {
    }

    public static void bootstrap() {
        Goety.PROXY = new ClientProxy();
        Goety.SIDED_INIT = new ClientSideInit();
    }
}
