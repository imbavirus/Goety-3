package za.co.infernos.goety.common.network;

import net.minecraft.server.level.ServerPlayer;
import za.co.infernos.goety.compat.legacy.network.NetworkEvent;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public final class NetworkContextHelper {
    private NetworkContextHelper() {
    }

    @Nullable
    public static ServerPlayer getServerPlayer(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context context = ctxSupplier.get();
        ServerPlayer player = tryResolvePlayer(context, "player");
        if (player != null) {
            return player;
        }
        return tryResolvePlayer(context, "getSender");
    }

    @Nullable
    private static ServerPlayer tryResolvePlayer(NetworkEvent.Context context, String methodName) {
        try {
            Method method = context.getClass().getMethod(methodName);
            Object result = method.invoke(context);
            if (result instanceof ServerPlayer serverPlayer) {
                return serverPlayer;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }
}
