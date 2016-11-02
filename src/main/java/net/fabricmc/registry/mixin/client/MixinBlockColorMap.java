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

package net.fabricmc.registry.mixin.client;

import net.fabricmc.registry.util.IRemapListener;
import net.fabricmc.registry.util.RegistryModUtils;
import net.minecraft.client.render.block.BlockColorMap;
import net.minecraft.client.render.block.IBlockColorMapper;
import net.minecraft.util.IdList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(value = BlockColorMap.class, remap = false)
public class MixinBlockColorMap implements IRemapListener {
	@Shadow
	public IdList<IBlockColorMapper> mappers;

	@Override
	public void onBeforeRemap() {
	}

	@Override
	public void onAfterRemap(Map<Integer, Integer> idRemapTable) {
		RegistryModUtils.remapIdList(idRemapTable, mappers);
	}
}
