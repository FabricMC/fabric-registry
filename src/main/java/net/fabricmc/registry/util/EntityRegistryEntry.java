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

import net.minecraft.entity.Entity;

import javax.annotation.concurrent.Immutable;
import java.util.HashMap;
import java.util.Map;

@Immutable
public class EntityRegistryEntry {
    private static final Map<Class<? extends Entity>, EntityRegistryEntry> DUMMY_ENTRIES = new HashMap<>();

    public final Class<? extends Entity> entityClass;
    public final int trackingRange;
    public final int updateRateTicks;
    public final boolean sendVelocityUpdates;
    private final boolean dummy;

    public EntityRegistryEntry(Class<? extends Entity> entityClass, int trackingRange, int updateRateTicks, boolean sendVelocityUpdates) {
        this(entityClass, trackingRange, updateRateTicks, sendVelocityUpdates, false);
    }

    private EntityRegistryEntry(Class<? extends Entity> entityClass, int trackingRange, int updateRateTicks, boolean sendVelocityUpdates, boolean dummy) {
        this.entityClass = entityClass;
        this.trackingRange = trackingRange;
        this.updateRateTicks = updateRateTicks;
        this.sendVelocityUpdates = sendVelocityUpdates;
        this.dummy = dummy;
    }

    public boolean isDummy() {
        return dummy;
    }

    public static EntityRegistryEntry dummy(Class<? extends Entity> entityClass) {
        EntityRegistryEntry entityEntry = DUMMY_ENTRIES.get(entityClass);
        if (entityEntry == null) {
            entityEntry = new EntityRegistryEntry(entityClass, 0, Integer.MAX_VALUE, false, true);
            DUMMY_ENTRIES.put(entityClass, entityEntry);
        }
        return entityEntry;
    }
}
