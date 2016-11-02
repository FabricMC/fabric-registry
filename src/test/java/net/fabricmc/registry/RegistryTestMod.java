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

import net.fabricmc.registry.manager.IRegistryManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.block.ItemBlock;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RegistryTestMod {
	public static final Identifier TESTBLOCK_1_ID = new Identifier("fabricregistrytest", "testblock1");
	public static final Identifier TESTBLOCK_2_ID = new Identifier("fabricregistrytest", "testblock2");
	private Block testBlock;
	private Block testBlockTwo;

	public void init() {
		RegistryMod.blockRM.getEvent().subscribe(new Consumer<IRegistryManager>() {
			@Override
			public void accept(IRegistryManager iRegistryManager) {
				Registries.register(TESTBLOCK_2_ID, testBlockTwo = new BlockTest(2));
				Registries.register(TESTBLOCK_1_ID, testBlock = new BlockTest(1));

				System.out.println("Block IDs: " + Block.getRawIdFromBlock(testBlock) + ", " + Block.getRawIdFromBlock(testBlockTwo));
			}
		});

		RegistryMod.itemRM.getEvent().subscribe(new Consumer<IRegistryManager>() {
			@Override
			public void accept(IRegistryManager iRegistryManager) {
				Registries.register(TESTBLOCK_2_ID, new ItemBlock(testBlockTwo));
				Registries.register(TESTBLOCK_1_ID, new ItemBlock(testBlock));

				System.out.println("Item IDs: "
					+ Item.getRawIdByItem(Item.getItemFromBlock(testBlock)) + ", "
					+ Item.getRawIdByItem(Item.getItemFromBlock(testBlockTwo))
				);
			}
		});

	}
}
