package za.co.infernos.goety.compat.legacy.network;

/**
 * Compatibility shim for older networking code.
 * <p>
 * NeoForge 1.21+ uses a different networking API; this exists only to keep legacy packet classes compiling.
 */
public enum NetworkDirection {
    PLAY_TO_CLIENT,
    PLAY_TO_SERVER
}

