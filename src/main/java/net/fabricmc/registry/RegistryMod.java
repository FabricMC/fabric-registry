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
import net.fabricmc.base.Fabric;
import net.fabricmc.base.loader.Init;
import net.fabricmc.registry.util.BiomeRegistrationManager;
import net.fabricmc.registry.util.BlockRegistrationManager;
import net.fabricmc.registry.util.EntityRegistrationManager;
import net.fabricmc.registry.util.IdRegistrationManager;
import net.fabricmc.registry.util.ItemRegistrationManager;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.block.ItemBlock;
import net.minecraft.potion.PotionEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class RegistryMod {
	@Init
	public void init() {
		Registries.add(new Identifier("blocks"), Block.class, new BlockRegistrationManager());
		Registries.add(new Identifier("items"), Item.class, new ItemRegistrationManager());
		Registries.add(new Identifier("entities"), Class.class, new EntityRegistrationManager());
		Registries.add(new Identifier("potionEffectTypes"), PotionEffectType.class, new IdRegistrationManager(PotionEffectType.REGISTRY, 255));
		Registries.add(new Identifier("enchantments"), Enchantment.class, new IdRegistrationManager(Enchantment.REGISTRY, 255));
		Registries.add(new Identifier("biomes"), Biome.class, new BiomeRegistrationManager());

		Fabric.getLoadingBus().subscribe(this);
		Fabric.getLoadingBus().subscribe(new RegistryTestMod());
	}

	@Hook(name = "fabric-registry:initRegistries", before = {}, after = "fabric:modsInitialized")
	public void postInit() {
		Registries.initRegistries();
	}
}
