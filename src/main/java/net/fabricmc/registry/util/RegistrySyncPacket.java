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

package net.fabricmc.registry.util;

import net.fabricmc.network.AbstractPacket;
import net.fabricmc.network.Asynchronous;
import net.fabricmc.registry.Registries;
import net.minecraft.nbt.TagCompound;
import net.minecraft.util.PacketByteBuf;

@Asynchronous
public class RegistrySyncPacket extends AbstractPacket {
	private TagCompound idMap;

	public RegistrySyncPacket() {

	}

	public RegistrySyncPacket init() {
		idMap = Registries.serializeIdMap();
		return this;
	}

	@Override
	public void write(PacketByteBuf packetByteBuf) {
		packetByteBuf.writeTagCompound(idMap);
	}

	@Override
	public void read(PacketByteBuf packetByteBuf) {
		idMap = packetByteBuf.readTagCompound();
	}

	@Override
	public void handle() {
		try {
			Registries.applySerializedIdMap(idMap);
		} catch (Exception e) {
			// TODO: Kick from server
			e.printStackTrace();
		}
	}
}
