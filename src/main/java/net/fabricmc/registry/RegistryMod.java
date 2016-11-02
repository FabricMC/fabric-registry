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

import net.fabricmc.base.loader.Init;
import net.fabricmc.base.loader.Loader;
import net.fabricmc.network.NetworkManager;
import net.fabricmc.network.impl.IndexedChannel;
import net.fabricmc.registry.manager.impl.BiomeRegistrationManager;
import net.fabricmc.registry.manager.impl.BlockRegistrationManager;
import net.fabricmc.registry.manager.MojangIdRegistryManager;
import net.fabricmc.registry.manager.impl.EntityRegistrationManager;
import net.fabricmc.registry.manager.impl.ItemRegistrationManager;
import net.fabricmc.registry.util.EntityRegistryEntry;
import net.fabricmc.registry.util.RegistrySyncPacket;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class RegistryMod {
	public static IndexedChannel channel;
	public static BlockRegistrationManager blockRM;
	//public static EntityRegistrationManager entityRM;
	public static ItemRegistrationManager itemRM;

	@Init
	public void init() {
		channel = new IndexedChannel();
		channel.register(RegistrySyncPacket.class);
		NetworkManager.registerChannel("registry", channel);

		Registries.add(new Identifier("blocks"), Block.class, blockRM = new BlockRegistrationManager());
		Registries.add(new Identifier("items"), Item.class, itemRM = new ItemRegistrationManager());
		//Currenly breaking all entitys needs fixing asap
		//Registries.add(new Identifier("entities"), EntityRegistryEntry.class, entityRM = new EntityRegistrationManager());
		Registries.add(new Identifier("potionEffectTypes"), PotionEffectType.class, new MojangIdRegistryManager(PotionEffectType.REGISTRY, 255));
		Registries.add(new Identifier("enchantments"), Enchantment.class, new MojangIdRegistryManager(Enchantment.REGISTRY, 255));
		Registries.add(new Identifier("biomes"), Biome.class, new BiomeRegistrationManager());

		// Uncomment when testing
		//new RegistryTestMod().init();

		Loader.INSTANCE.modsInitialized.subscribe(() -> Registries.initRegistries());

	}

}
