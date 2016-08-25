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
