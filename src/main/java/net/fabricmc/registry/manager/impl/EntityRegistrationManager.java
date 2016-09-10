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

package net.fabricmc.registry.manager.impl;

import com.google.common.base.Objects;
import net.fabricmc.registry.manager.RemappableRegistryManager;
import net.fabricmc.registry.util.EntityEntry;
import net.fabricmc.registry.util.RegistryModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.IdRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

public class EntityRegistrationManager extends RemappableRegistryManager<EntityEntry> {
    private static final int MAX_ID = 255;

    protected int nextFreeId = 1;
    private final Map<Class<? extends Entity>, EntityEntry> classEntityEntryMap = new HashMap<>();
    private final IdRegistry<Identifier, EntityEntry> registry = new IdRegistry<>();
    private final IdRegistry<Identifier, Class<? extends Entity>> classRegistry = EntityRegistry.CLASS_MAP;

    public EntityEntry getEntryForClass(Class<? extends Entity> entityClass) {
        return classEntityEntryMap.get(entityClass);
    }

    @Override
    protected int findNextFreeId(EntityEntry value) {
        while (nextFreeId <= MAX_ID && classRegistry.get(nextFreeId) != null) {
            nextFreeId++;
        }
        return nextFreeId <= MAX_ID ? nextFreeId : -1;
    }

    @Override
    protected boolean replaceInternal(Identifier source, Identifier target) {
        return false;
    }

    @Override
    public void onBeforeRemap() {
        super.onBeforeRemap();

        EntityRegistry.ID_SET.clear();

        classEntityEntryMap.clear();
        RegistryModUtils.clear(classRegistry);
        RegistryModUtils.clear(registry);

        nextFreeId = 1;
    }

    @Override
    protected boolean registerInternal(int rawId, Identifier id, EntityEntry value) {
        try {
            EntityRegistry.registerEntity(rawId, id.toString(), value.entityClass, null);
            if (!value.isDummy()) {
                registry.register(rawId, id, value);
                classEntityEntryMap.put(value.entityClass, value);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* FIXME: ugly hacks for remapping taking vanilla entities
       without EntityEntry into account */

    @Override
    public EntityEntry get(int id) {
        return get(classRegistry.getKey(classRegistry.get(id)));
    }

    @Override
    public EntityEntry get(Identifier id) {
        EntityEntry entry = registry.get(id);
        if (entry != null) {
            return entry;
        } else if (classRegistry.containsKey(id)) {
            return EntityEntry.dummy(classRegistry.get(id));
        } else {
            return null;
        }
    }

    @Override
    public boolean contains(Identifier id) {
        return classRegistry.containsKey(id);
    }

    @Override
    public Identifier getId(EntityEntry value) {
        return classRegistry.getKey(value.entityClass);
    }

    @Override
    public int getRawId(EntityEntry value) {
        return classRegistry.getId(value.entityClass);
    }

    @Override
    public Iterable<EntityEntry> values() {
        return StreamSupport.stream(classRegistry.spliterator(), false)
                .map((Class<? extends Entity> eClass) -> get(classRegistry.getKey(eClass)))
                ::iterator;
    }
}
