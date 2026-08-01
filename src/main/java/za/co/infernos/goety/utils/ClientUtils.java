package za.co.infernos.goety.utils;

import za.co.infernos.goety.mixin.ClientModLoaderAccessor;
import net.neoforged.fml.ModLoadingException;

public class ClientUtils {
    public static boolean noLoadingExceptions() {
        System.out.println();
        ModLoadingException error = ClientModLoaderAccessor.getError();
        return error == null;
    }
}