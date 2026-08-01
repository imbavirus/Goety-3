package za.co.infernos.goety.compat.legacy.neoforge.common.extensions;

import net.neoforged.neoforge.common.extensions.IBlockExtension;

/**
 * Compatibility shim for older NeoForge/Forge code that implemented {@code IForgeBlock}.
 * <p>
 * NeoForge 1.21+ replaced many Forge extension interfaces with fine-grained *Extension types.
 * This interface exists only to keep legacy block classes compiling during the port.
 */
public interface IForgeBlock extends IBlockExtension {
}

