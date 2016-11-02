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

package net.fabricmc.registry.mixin.common;

import net.fabricmc.registry.mixin.interfaces.IMixinIdRegistry;
import net.minecraft.util.registry.IdRegistry;
import net.minecraft.util.registry.IdStore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(IdRegistry.class)
public class MixinIdRegistry implements IMixinIdRegistry {
	@Shadow
	private IdStore idStore;
	@Shadow
	private Map valueKeyMap;

	public IdStore getIdStore() {
		return idStore;
	}

	public Map getValueKeyMap() {
		return valueKeyMap;
	}

}
