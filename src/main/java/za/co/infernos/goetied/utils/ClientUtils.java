package za.co.infernos.goetied.utils;

import za.co.infernos.goetied.mixin.ClientModLoaderAccessor;
import net.neoforged.fml.ModLoadingException;

public class ClientUtils {
    public static boolean noLoadingExceptions() {
        System.out.println();
        ModLoadingException error = ClientModLoaderAccessor.getError();
        return error == null;
    }
}