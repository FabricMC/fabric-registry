package net.fabricmc.registry.util;

import net.minecraft.client.render.item.IItemColorMapper;
import net.minecraft.client.render.item.ItemColorMap;
import net.minecraft.util.IdList;

import java.util.HashMap;
import java.util.Map;

public final class RegistryModUtils {
    private RegistryModUtils() {

    }

    public static void remapIdList(Map<Integer, Integer> idRemapTable, IdList list) {
        Map<Integer, Object> remapTemp = new HashMap<>();
        for (Integer integer : idRemapTable.keySet()) {
            remapTemp.put(idRemapTable.get(integer), list.get(integer));
            list.list.remove(integer);
        }

        for (Integer integer : remapTemp.keySet()) {
            list.add(remapTemp.get(integer), integer);
        }
    }

    public static <T> void remapIntMap(Map<Integer, Integer> idRemapTable, Map<Integer, T> target) {
        Map<Integer, T> remapTemp = new HashMap<>();
        for (Integer integer : idRemapTable.keySet()) {
            remapTemp.put(idRemapTable.get(integer), target.get(integer));
            target.remove(integer);
        }

        target.putAll(remapTemp);
    }

    public static void clear(IdList list) {
        list.list.clear();
        list.idMap.clear();
    }
}
