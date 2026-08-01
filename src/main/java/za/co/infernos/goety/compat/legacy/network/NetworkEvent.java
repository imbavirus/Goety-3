package za.co.infernos.goety.compat.legacy.network;

import java.util.function.Supplier;

/**
 * Compatibility shim for older networking code that used {@code NetworkEvent.Context}.
 */
public final class NetworkEvent {
    private NetworkEvent() {}

    public static final class Context {
        public void enqueueWork(Runnable runnable) {
            runnable.run();
        }

        public void setPacketHandled(boolean handled) {
            // no-op
        }
    }

    public interface ContextSupplier extends Supplier<Context> {}
}

