/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.registry.util;

import net.fabricmc.registry.util.exception.RegistryMappingNotFoundException;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class RegistrationManager<V> implements IRemapListener {
    private final Set<IRemapListener> remapListeners = new HashSet<>();
    private boolean frozen;

    public RegistrationManager() {
        registerRemapListener(this);
    }

    public final void freeze() {
        frozen = true;
        onFreeze();
    }

    public final void registerRemapListener(IRemapListener listener) {
        remapListeners.add(listener);
    }

    protected abstract int findNextFreeId(V value);
    protected abstract boolean registerInternal(int rawId, Identifier id, V value);

    protected void onFreeze() {

    }

    @Override
    public void onBeforeRemap() {

    }

    @Override
    public void onAfterRemap() {

    }

    public abstract V get(int id);
    public abstract V get(Identifier id);
    public abstract boolean contains(Identifier id);
    public abstract Identifier getId(V value);
    public abstract int getRawId(V value);
    public abstract Iterable<V> getValues();

    public boolean register(Identifier id, V value) {
        if (!isFrozen()) {
            int rawId = findNextFreeId(value);
            if (rawId >= 0) {
                return registerInternal(rawId, id, value);
            }
        }

        return false;
    }

    public final boolean isFrozen() {
        return frozen;
    }

    public Set<Identifier> getRemovedEntries(Map<Integer, Identifier> idMap) {
        Set<Identifier> missingIdsLocal = new HashSet<>();

        for (Identifier id : idMap.values()) {
            if (!contains(id)) {
                missingIdsLocal.add(id);
            }
        }

        return missingIdsLocal;
    }

    public void remap(Map<Integer, Identifier> idMap, boolean ignoreMissingEntries) throws RegistryMappingNotFoundException {
        Map<Identifier, V> valueMap = new HashMap<>();
        Set<Identifier> missingIdsMap = new HashSet<>();

        for (V value : getValues()) {
            Identifier id = getId(value);
            valueMap.put(id, value);
            if (!idMap.containsValue(id)) {
                missingIdsMap.add(id);
            }
        }

        if (!ignoreMissingEntries) {
            Set<Identifier> missingIdsLocal = getRemovedEntries(idMap);

            if (missingIdsLocal.size() > 0) {
                throw new RegistryMappingNotFoundException(missingIdsLocal);
            }
        }

        for (IRemapListener listener : remapListeners) {
            listener.onBeforeRemap();
        }

        for (Map.Entry<Integer, Identifier> entry : idMap.entrySet()) {
            V value = valueMap.get(entry.getValue());
            registerInternal(entry.getKey(), entry.getValue(), value);
        }

        for (Identifier id : missingIdsMap) {
            register(id, valueMap.get(id));
        }

        for (IRemapListener listener : remapListeners) {
            listener.onAfterRemap();
        }
    }

    public Map<Integer, Identifier> getIdMap() {
        Map<Integer, Identifier> idMap = new HashMap<>();
        for (V value : getValues()) {
            idMap.put(getRawId(value), getId(value));
        }
        return idMap;
    }
}
