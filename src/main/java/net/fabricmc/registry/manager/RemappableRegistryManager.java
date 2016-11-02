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

import com.google.common.collect.BiMap;
import net.fabricmc.api.Event;
import net.fabricmc.registry.util.IRemapListener;
import net.fabricmc.registry.util.exception.RegistryMappingNotFoundException;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class RemappableRegistryManager<V> implements IRemapListener, IRemappableRegistryManager<V> {
	private final Set<IRemapListener> remapListeners = new HashSet<>();
	private boolean frozen;
	private Event.Event1<IRegistryManager> event;

	public RemappableRegistryManager() {
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

	protected abstract boolean replaceInternal(Identifier source, Identifier target);

	protected abstract boolean registerInternal(int rawId, Identifier id, V value);

	protected void onFreeze() {

	}

	@Override
	public void onBeforeRemap() {

	}

	@Override
	public void onAfterRemap(Map<Integer, Integer> idRemapTable) {

	}

	// TODO
	public boolean replace(Identifier source, Identifier target) {
		if (!isFrozen() && contains(source) && contains(target)) {
			return replaceInternal(source, target);
		}

		return false;
	}

	@Override
	public boolean register(Identifier id, V value) {
		if (!isFrozen()) {
			int rawId = findNextFreeId(value);
			if (rawId >= 0) {
				return registerInternal(rawId, id, value);
			}
		}

		return false;
	}

	@Override
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

	@Override
	public void remap(BiMap<Integer, Identifier> idMap, boolean ignoreMissingEntries) throws RegistryMappingNotFoundException {
		Map<Identifier, V> valueMap = new HashMap<>();
		Map<Identifier, Integer> oldIds = new HashMap<>();
		Map<Identifier, Integer> newIds = new HashMap<>(idMap.inverse());
		Map<Integer, Integer> idRemapTable = new HashMap<>();
		Set<Identifier> missingIdsMap = new HashSet<>();

		for (V value : values()) {
			Identifier id = getId(value);
			oldIds.put(id, getRawId(value));
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

		boolean oldFrozen = frozen;
		frozen = false;

		for (Map.Entry<Integer, Identifier> entry : idMap.entrySet()) {
			V value = valueMap.get(entry.getValue());
			registerInternal(entry.getKey(), entry.getValue(), value);
		}

		for (Identifier id : missingIdsMap) {
			V value = valueMap.get(id);
			register(id, value);
			newIds.put(id, getRawId(value));
		}

		for (Map.Entry<Identifier, Integer> oldId : oldIds.entrySet()) {
			int oldRawId = oldId.getValue().intValue();
			int newRawId = newIds.get(oldId.getKey()).intValue();
			if (oldRawId != newRawId) {
				idRemapTable.put(oldRawId, newRawId);
			}
		}

		for (IRemapListener listener : remapListeners) {
			listener.onAfterRemap(idRemapTable);
		}

		frozen = oldFrozen;
	}

	public Map<Integer, Identifier> getRawIdMap() {
		Map<Integer, Identifier> idMap = new HashMap<>();
		for (V value : values()) {
			idMap.put(getRawId(value), getId(value));
		}
		return idMap;
	}

	@Override
	public Event.Event1<IRegistryManager> getEvent() {
		return event;
	}

	@Override
	public void setEvent(Event.Event1<IRegistryManager> event) {
		this.event = event;
	}
}
