package net.fabricmc.registry.util;

import net.minecraft.util.IdList;

public final class RegistryModUtils {
    private RegistryModUtils() {

    }

    public static void clear(IdList list) {
        list.list.clear();
        list.idMap.clear();
    }
}
