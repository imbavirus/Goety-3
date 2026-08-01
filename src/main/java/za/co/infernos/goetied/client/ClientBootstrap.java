package za.co.infernos.goetied.client;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.init.ClientSideInit;

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
        Goetied.PROXY = new ClientProxy();
        Goetied.SIDED_INIT = new ClientSideInit();
    }
}
