package za.co.infernos.goety.compat.legacy.neoforge.common;

/**
 * Compatibility shim for older NeoForge/Forge code that referenced {@code IPlantable}.
 * <p>
 * NeoForge 1.21+ moved/removed portions of the old plant API. This interface exists only to keep
 * legacy method signatures compiling during the port.
 */
public interface IPlantable {
}

