package net.fabricmc.registry.mixin.client;

import net.fabricmc.registry.Registries;
import net.fabricmc.registry.RegistryMod;
import net.fabricmc.registry.util.IRemapListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.item.ItemModelMap;
import net.minecraft.client.render.model.IBakedModel;
import net.minecraft.client.util.ModelIdentifier;
import org.lwjgl.LWJGLException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = Minecraft.class)
public class MixinMinecraft {
    @Inject(method="init()V", at=@At("RETURN"))
    public void init(CallbackInfo callbackInfo) throws LWJGLException {
        RegistryMod.blockRM.registerRemapListener((IRemapListener) Minecraft.getInstance().getBlockColorMap());
        RegistryMod.itemRM.registerRemapListener((IRemapListener) Minecraft.getInstance().itemColorMap);
        RegistryMod.itemRM.registerRemapListener((IRemapListener) Minecraft.getInstance().getItemRenderer().a()); 
    }
}
