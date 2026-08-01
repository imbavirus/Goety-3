package za.co.infernos.goety.compat.legacy.neoforge.common.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Compatibility shim for older NeoForge/Forge code that relied on {@code LazyOptional}.
 * <p>
 * NeoForge 1.21+ moved away from the old capability API; this class exists only to keep legacy code compiling.
 */
public final class LazyOptional<T> {
    private Supplier<? extends T> supplier;
    private T value;
    private boolean resolved;

    private LazyOptional(Supplier<? extends T> supplier) {
        this.supplier = Objects.requireNonNull(supplier, "supplier");
    }

    public static <T> LazyOptional<T> of(Supplier<? extends T> supplier) {
        return new LazyOptional<>(supplier);
    }

    public boolean isPresent() {
        return resolve().isPresent();
    }

    public Optional<T> resolve() {
        if (!resolved && supplier != null) {
            value = supplier.get();
            resolved = true;
        }
        return Optional.ofNullable(value);
    }

    public void invalidate() {
        supplier = null;
        value = null;
        resolved = false;
    }
}

