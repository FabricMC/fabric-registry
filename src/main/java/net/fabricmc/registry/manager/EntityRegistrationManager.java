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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.util.Identifier;

public class EntityRegistrationManager extends IdRegistrationManager<Class<? extends Entity>> {
    public EntityRegistrationManager() {
        super(EntityRegistry.CLASS_MAP, 255);
    }

    @Override
    public void onBeforeRemap() {
        super.onBeforeRemap();
        EntityRegistry.ID_LIST.clear();
    }

    @Override
    protected boolean registerInternal(int rawId, Identifier id, Class<? extends Entity> value) {
        try {
            EntityRegistry.registerEntity(rawId, id.toString(), value, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
