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
