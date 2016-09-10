package net.fabricmc.registry.manager;

import net.minecraft.util.Identifier;

public interface IRegistryManager<V> {
    V get(Identifier id);
    Identifier getId(V value);
    boolean contains(Identifier id);
    Iterable<V> values();

    boolean register(Identifier id, V value);

    boolean isFrozen();
    void freeze();
}
