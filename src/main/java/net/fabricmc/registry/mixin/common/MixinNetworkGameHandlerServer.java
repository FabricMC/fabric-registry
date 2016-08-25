package net.fabricmc.registry.mixin.common;

import net.fabricmc.network.NetworkManager;
import net.fabricmc.network.util.NetworkHelper;
import net.fabricmc.registry.RegistryMod;
import net.fabricmc.registry.util.RegistrySyncPacket;
import net.minecraft.entity.player.EntityPlayerServer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.IPacketHandler;
import net.minecraft.network.handler.impl.NetworkGameHandlerServer;
import net.minecraft.network.packet.client.CPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkGameHandlerServer.class, remap = false)
public class MixinNetworkGameHandlerServer {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(MinecraftServer server, ClientConnection connection, EntityPlayerServer player, CallbackInfo info) {
        if (server.isDedicated()) {
           RegistryMod.channel.sendToPlayer(new RegistrySyncPacket().init(), player);
        }
    }
}
