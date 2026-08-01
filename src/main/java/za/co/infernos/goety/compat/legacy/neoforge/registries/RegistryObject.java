package za.co.infernos.goety.compat.legacy.neoforge.registries;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Compatibility shim for older Forge/NeoForge code that still uses {@code RegistryObject}.
 * <p>
 * NeoForge 1.21+ migrated to {@link DeferredHolder}. This wrapper keeps existing code compiling by
 * delegating to a {@link DeferredHolder}.
 */
public final class RegistryObject<T> implements Supplier<T>, Holder<T> {
    private final DeferredHolder<T, T> delegate;

    private RegistryObject(DeferredHolder<T, T> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    public static <R, T extends R> RegistryObject<T> of(DeferredHolder<R, T> delegate) {
        return new RegistryObject<>((DeferredHolder<T, T>) delegate);
    }

    @Override
    public T get() {
        return delegate.get();
    }

    public ResourceLocation getId() {
        return delegate.getId();
    }

    public boolean isPresent() {
        return delegate.isBound();
    }

    public Optional<T> asOptional() {
        return delegate.asOptional();
    }

    /**
     * Compatibility helper for 1.21+ APIs that now take {@link Holder}s instead of raw registry values.
     * <p>
     * {@link DeferredHolder} implements {@link Holder}, so we can safely expose it here for callers.
     */
    @SuppressWarnings("unchecked")
    public Holder<T> getHolder() {
        return this;
    }

    @Override
    public String toString() {
        return "RegistryObject[" + delegate + "]";
    }

    // Holder<T> implementation
    @Override
    public T value() {
        return delegate.value();
    }

    @Override
    public boolean isBound() {
        return delegate.isBound();
    }

    @Override
    public boolean is(ResourceLocation location) {
        return delegate.is(location);
    }

    @Override
    public boolean is(net.minecraft.resources.ResourceKey<T> resourceKey) {
        return delegate.is(resourceKey);
    }

    @Override
    public boolean is(net.minecraft.tags.TagKey<T> tag) {
        return delegate.is(tag);
    }

    @Override
    public boolean is(Holder<T> holder) {
        return delegate.is(holder);
    }

    @Override
    public java.util.stream.Stream<net.minecraft.tags.TagKey<T>> tags() {
        return delegate.tags();
    }

    @Override
    public com.mojang.datafixers.util.Either<net.minecraft.resources.ResourceKey<T>, T> unwrap() {
        return delegate.unwrap();
    }

    @Override
    public Optional<net.minecraft.resources.ResourceKey<T>> unwrapKey() {
        return delegate.unwrapKey();
    }

    @Override
    public Kind kind() {
        return delegate.kind();
    }

    @Override
    public boolean canSerializeIn(net.minecraft.core.HolderOwner<T> owner) {
        return delegate.canSerializeIn(owner);
    }

    @Override
    public boolean is(java.util.function.Predicate<net.minecraft.resources.ResourceKey<T>> predicate) {
        return delegate.is(predicate);
    }
}

