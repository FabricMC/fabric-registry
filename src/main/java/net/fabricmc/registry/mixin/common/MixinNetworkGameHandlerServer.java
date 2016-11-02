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

package net.fabricmc.registry.mixin.common;


import net.fabricmc.registry.RegistryMod;
import net.fabricmc.registry.util.RegistrySyncPacket;
import net.minecraft.entity.player.EntityPlayerServer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.handler.NetworkGameHandlerServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkGameHandlerServer.class)
public class MixinNetworkGameHandlerServer {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftServer server, ClientConnection connection, EntityPlayerServer player, CallbackInfo info) {
        if (server.isDedicated()) {
           RegistryMod.channel.sendToPlayer(new RegistrySyncPacket().init(), player);
        }
    }
}
