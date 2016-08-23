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

import net.fabricmc.api.Hook;
import net.minecraft.block.Block;
import net.minecraft.item.block.ItemBlock;
import net.minecraft.util.Identifier;

public class RegistryTestMod {
    private static final Identifier TESTBLOCK_ID = new Identifier("fabricregistrytest", "testblock");
    private Block testBlock;

    @Hook(name = "fabric-registry-test:modsInitialized", before = {}, after = {"fabric:modsInitialized"})
    public void onPostInit() {
        // check if anything breaks
        try {
            System.out.println("Testing ID remap - same IDs");
            Registries.applySerializedIdMap(Registries.serializeIdMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Hook(name = "fabric-registry-test:registerBlocks", before = {}, after = {"fabric:registerBlocks"})
    public void onBlockRegistration() {
        Registries.register(TESTBLOCK_ID, testBlock = new BlockTest());
    }

    @Hook(name = "fabric-registry-test:registerItems", before = {}, after = {"fabric:registerItems"})
    public void onItemRegistration() {
        Registries.register(TESTBLOCK_ID, new ItemBlock(testBlock));
    }
}
