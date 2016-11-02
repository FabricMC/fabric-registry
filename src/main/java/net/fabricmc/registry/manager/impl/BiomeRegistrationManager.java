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

package net.fabricmc.registry.manager.impl;

import net.fabricmc.registry.manager.MojangIdRegistryManager;
import net.fabricmc.registry.util.RegistryModUtils;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.lang.reflect.Method;

public class BiomeRegistrationManager extends MojangIdRegistryManager<Biome> {
    public BiomeRegistrationManager() {
        super(Biome.REGISTRY, 255);
    }

    @Override
    public void onBeforeRemap() {
        super.onBeforeRemap();
        Biome.BIOMES.clear();
        RegistryModUtils.clear(Biome.NAMED_BIOME_IDS);
    }

    @Override
    protected boolean registerInternal(int rawId, Identifier id, Biome value) {
        try {
	        //TODO remap
	        Method register = Biome.class.getDeclaredMethod("register", Integer.class, String.class, Biome.class);
	        register.setAccessible(true);
	        register.invoke(null, rawId, id.toString(), value);

            Biome.BIOMES.add(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
