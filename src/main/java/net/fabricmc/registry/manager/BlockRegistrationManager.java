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
import net.minecraft.block.IBlockState;
import net.minecraft.block.impl.BlockTripWire;
import net.minecraft.util.Identifier;

public class BlockRegistrationManager extends IdRegistrationManager<Block> {
    public BlockRegistrationManager() {
        super(Block.REGISTRY, 4095);
    }

    private void addStateManual(Block block, int i) {
        int pos = registry.getId(block) << 4 | i;
        Block.BLOCKSTATE_ID_LIST.add(block.deserializeState(i), pos);
    }

    private void addStates(Block block) {
        for (IBlockState state : block.getStateFactory().getValidStates()) {
            int pos = registry.getId(block) << 4 | block.serializeState(state);
            Block.BLOCKSTATE_ID_LIST.add(state, pos);
        }
    }

    private void buildState(Block block) {
        if (block instanceof BlockTripWire) {
            for (int i = 0; i < 15; i++) {
                addStateManual(block, i);
            }
        } else {
            addStates(block);
        }
    }

    @Override
    public void onBeforeRemap() {
        super.onBeforeRemap();
        Block.BLOCKSTATE_ID_LIST.idMap.clear();
        Block.BLOCKSTATE_ID_LIST.list.clear();
    }

    @Override
    public boolean registerInternal(int rawId, Identifier id, Block value) {
        if (super.registerInternal(rawId, id, value)) {
            buildState(value);
            return true;
        } else {
            return false;
        }
    }
}