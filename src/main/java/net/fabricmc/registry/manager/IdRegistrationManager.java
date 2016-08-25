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

package net.fabricmc.registry.manager;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.IdRegistry;

public class IdRegistrationManager<V> extends RegistrationManager<V> {
    protected final IdRegistry<Identifier, V> registry;
    protected int nextFreeId = 1;
    private final int maxId;

    public IdRegistrationManager(IdRegistry<Identifier, V> registry, int maxId) {
        this.registry = registry;
        this.maxId = maxId;
    }

    @Override
    protected int findNextFreeId(V value) {
        V defValue = getDefaultValue();
        while (nextFreeId <= maxId && registry.get(nextFreeId) != null && registry.get(nextFreeId) != defValue) {
            nextFreeId++;
        }
        return nextFreeId <= maxId ? nextFreeId : -1;
    }

    @Override
    protected boolean replaceInternal(Identifier source, Identifier target) {
        V sourceValue = registry.get(source);
        V targetValue = registry.get(target);
        if (sourceValue == targetValue) {
            return true;
        }

        /* registry.idStore.set(registry.get(target), registry.getId(sourceValue));
        registry.put(source, targetValue);
        return true; */
        return false;
    }

    public final V getDefaultValue() {
        return registry.get(0);
    }

    @Override
    protected boolean registerInternal(int rawId, Identifier id, V value) {
        registry.register(rawId, id, value);
        return true;
    }

    @Override
    public void onBeforeRemap() {
        super.onBeforeRemap();
        registry.idStore.reset();
        registry.map.clear();
        registry.valueKeyMap.clear();
        registry.valueCache = null;
    }

    @Override
    public V get(int id) {
        return registry.get(id);
    }

    @Override
    public V get(Identifier id) {
        return registry.get(id);
    }

    @Override
    public boolean contains(Identifier id) {
        return registry.containsKey(id);
    }

    @Override
    public Identifier getId(V value) {
        return registry.getKey(value);
    }

    @Override
    public int getRawId(V value) {
        return registry.getId(value);
    }

    @Override
    public Iterable<V> getValues() {
        return registry;
    }
}
