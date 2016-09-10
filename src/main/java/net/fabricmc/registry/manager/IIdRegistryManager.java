package net.fabricmc.registry.manager;

import net.minecraft.util.Identifier;

import java.util.Map;

public interface IIdRegistryManager<V> extends IRegistryManager<V> {
    V get(int rawId);
    int getRawId(V value);

    Map<Integer, Identifier> getRawIdMap();
}
