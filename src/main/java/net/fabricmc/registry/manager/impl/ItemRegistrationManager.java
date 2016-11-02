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
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class ItemRegistrationManager extends MojangIdRegistryManager<Item> {
    public ItemRegistrationManager() {
        super(Item.REGISTRY, 32767);
        nextFreeId = 256;
    }

    @Override
    public void onBeforeRemap() {
        super.onBeforeRemap();
        nextFreeId = 256;
	    getBlockItemMap().clear();
    }

    @Override
    public boolean registerInternal(int rawId, Identifier id, Item value) {
        if (super.registerInternal(rawId, id, value)) {
            if (Block.REGISTRY.containsKey(id)) {
	            addToBlockItemMap(Block.REGISTRY.get(id), value);
            }
            return true;
        } else {
            return false;
        }
    }

    private Map<Block, Item> getBlockItemMap(){
	    try {
		    //TODO remap this
		    Field field = Item.class.getDeclaredField("BLOCK_ITEM_MAP");
		    field.setAccessible(true);
		    return (Map<Block, Item>) field.get(null);
	    } catch (NoSuchFieldException | IllegalAccessException e) {
		    e.printStackTrace();
	    }

	    return null;
    }

    private void addToBlockItemMap(Block block, Item item){
	    try {
		    Method put = Map.class.getDeclaredMethod("put",Object.class,Object.class);
		    put.invoke(getBlockItemMap(), block, item);
	    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
		    e.printStackTrace();
	    }

    }

}
