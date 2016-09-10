package net.fabricmc.registry.util;

import net.minecraft.entity.Entity;

import javax.annotation.concurrent.Immutable;
import java.util.HashMap;
import java.util.Map;

@Immutable
public class EntityEntry {
    private static final Map<Class<? extends Entity>, EntityEntry> DUMMY_ENTRIES = new HashMap<>();

    public final Class<? extends Entity> entityClass;
    public final int trackingRange;
    public final int updateRateTicks;
    public final boolean sendVelocityUpdates;
    private boolean dummy;

    public EntityEntry(Class<? extends Entity> entityClass, int trackingRange, int updateRateTicks, boolean sendVelocityUpdates) {
        this.entityClass = entityClass;
        this.trackingRange = trackingRange;
        this.updateRateTicks = updateRateTicks;
        this.sendVelocityUpdates = sendVelocityUpdates;
    }

    public boolean isDummy() {
        return dummy;
    }

    public static EntityEntry dummy(Class<? extends Entity> entityClass) {
        EntityEntry entityEntry = DUMMY_ENTRIES.get(entityClass);
        if (entityEntry == null) {
            entityEntry = new EntityEntry(entityClass, 0, Integer.MAX_VALUE, false);
            entityEntry.dummy = true;
            DUMMY_ENTRIES.put(entityClass, entityEntry);
        }
        return entityEntry;
    }
}
