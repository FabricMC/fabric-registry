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

package net.fabricmc.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.base.Fabric;
import net.fabricmc.registry.manager.IRegistryManager;
import net.fabricmc.registry.manager.IRemappableRegistryManager;
import net.fabricmc.registry.manager.RemappableRegistryManager;
import net.fabricmc.registry.util.exception.RegistryMappingNotFoundException;
import net.minecraft.nbt.TagCompound;
import net.minecraft.nbt.TagList;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Registries {
    private static final List<IRegistryManager> REGISTRY_LIST = new ArrayList<>();
    private static final Map<Identifier, IRegistryManager> REGISTRIES = new HashMap<>();
    private static final Map<IRegistryManager, String> REGISTRY_EVENT_NAMES = new HashMap<>();
    private static final Map<Class<?>, IRegistryManager> REGISTRIES_BY_CLASS = new HashMap<>();

    private Registries() {

    }

    public static void initRegistries() {
        for (IRegistryManager manager : REGISTRY_LIST) {
            if (!manager.isFrozen()) {
                Fabric.getLoadingBus().call(REGISTRY_EVENT_NAMES.get(manager));
                manager.freeze();
            }
        }
    }

    // TODO: Don't differentiate just based on class! :(
    public static void add(Identifier id, Class c, IRegistryManager manager) {
        String eventName = "register" + StringUtils.capitalize(id.getName());
        if (!"minecraft".equals(id.getOwner())) {
            eventName = id.getOwner() + ":" + eventName;
        } else {
            eventName = "fabric:" + eventName;
        }

        REGISTRY_LIST.add(manager);
        REGISTRIES.put(id, manager);
        REGISTRY_EVENT_NAMES.put(manager, eventName);
        REGISTRIES_BY_CLASS.put(c, manager);
        Fabric.getLoadingBus().addDummyHookName(eventName);
    }

    public static boolean register(Identifier id, Object o) {
        Class c = o.getClass();
        while (!REGISTRIES_BY_CLASS.containsKey(c) && c != null) {
            c = c.getSuperclass();
        }

        if (REGISTRIES_BY_CLASS.containsKey(c)) {
            return REGISTRIES_BY_CLASS.get(c).register(id, o);
        }
        return false;
    }

    public static TagCompound serializeIdMap() {
        TagCompound compound = new TagCompound();
        for (Map.Entry<Identifier, IRegistryManager> entry : REGISTRIES.entrySet()) {
            if (entry.getValue() instanceof IRemappableRegistryManager) {
                IRemappableRegistryManager rrm = (IRemappableRegistryManager) entry.getValue();
                Map<Integer, Identifier> idMap = rrm.getRawIdMap();

                TagList entryData = new TagList();
                for (Map.Entry<Integer, Identifier> idEntry : idMap.entrySet()) {
                    TagCompound data = new TagCompound();
                    data.setInt("id", idEntry.getKey());
                    data.setString("name", idEntry.getValue().toString());
                    entryData.add(data);
                }
                compound.setTag(entry.getKey().toString(), entryData);
                // System.out.println(entry.getKey() + " = " + entryData.toString());
            }
        }
        return compound;
    }

    public static void applySerializedIdMap(TagCompound compound) throws RegistryMappingNotFoundException {
        for (Map.Entry<Identifier, IRegistryManager> entry : REGISTRIES.entrySet()) {
            if (entry.getValue() instanceof IRemappableRegistryManager) {
                IRemappableRegistryManager rrm = (IRemappableRegistryManager) entry.getValue();
                if (compound.hasKey(entry.getKey().toString())) {
                    TagList list = compound.getTagList(entry.getKey().toString(), 10);
                    BiMap<Integer, Identifier> idMap = HashBiMap.create(list.getSize());
                    for (int i = 0; i < list.getSize(); i++) {
                        TagCompound entryTag = list.getTagCompound(i);
                        if (entryTag.hasKey("id") && entryTag.hasKey("name")) {
                            idMap.put(entryTag.getInt("id"), new Identifier(entryTag.getString("name")));
                        }
                    }

                    rrm.remap(idMap, false);
                }
            }
        }
    }
}
