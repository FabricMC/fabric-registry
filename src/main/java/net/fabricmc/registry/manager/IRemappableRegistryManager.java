package net.fabricmc.registry.manager;

import com.google.common.collect.BiMap;
import net.fabricmc.registry.util.exception.RegistryMappingNotFoundException;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface IRemappableRegistryManager<V> extends IIdRegistryManager<V> {
    void remap(BiMap<Integer, Identifier> idMap, boolean ignoreMissingEntries) throws RegistryMappingNotFoundException;
}
