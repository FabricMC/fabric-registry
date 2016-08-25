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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ItemRegistrationManager extends IdRegistrationManager<Item> {
    public ItemRegistrationManager() {
        super(Item.REGISTRY, 32767);
        nextFreeId = 256;
    }

    @Override
    public void onBeforeRemap() {
        super.onBeforeRemap();
        nextFreeId = 256;
        Item.BLOCK_ITEM_MAP.clear();
    }

    @Override
    public boolean registerInternal(int rawId, Identifier id, Item value) {
        if (super.registerInternal(rawId, id, value)) {
            if (Block.REGISTRY.containsKey(id)) {
                Item.BLOCK_ITEM_MAP.put(Block.REGISTRY.get(id), value);
            }
            return true;
        } else {
            return false;
        }
    }
}
