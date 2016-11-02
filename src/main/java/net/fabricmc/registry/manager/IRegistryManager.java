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

import net.fabricmc.api.Event;
import net.minecraft.util.Identifier;

public interface IRegistryManager<V> {
	V get(Identifier id);

	Identifier getId(V value);

	boolean contains(Identifier id);

	Iterable<V> values();

	boolean register(Identifier id, V value);

	boolean isFrozen();

	void freeze();

	Event.Event1<IRegistryManager> getEvent();

	void setEvent(Event.Event1<IRegistryManager> event);

}
