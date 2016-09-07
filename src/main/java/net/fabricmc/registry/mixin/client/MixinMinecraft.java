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

@Mixin(value = Minecraft.class, remap = false)
public class MixinMinecraft {
    @Inject(method="init()V", at=@At("RETURN"))
    public void init(CallbackInfo callbackInfo) throws LWJGLException {
        RegistryMod.blockRM.registerRemapListener((IRemapListener) Minecraft.getInstance().getBlockColorMap());
        RegistryMod.itemRM.registerRemapListener((IRemapListener) Minecraft.getInstance().itemColorMap);
        RegistryMod.itemRM.registerRemapListener((IRemapListener) Minecraft.getInstance().getItemRenderer().getModelMap());
    }
}
