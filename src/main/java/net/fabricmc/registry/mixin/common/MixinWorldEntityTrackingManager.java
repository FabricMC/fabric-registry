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
import net.fabricmc.registry.util.EntityRegistryEntry;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.WorldEntityTrackingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldEntityTrackingManager.class)
public class MixinWorldEntityTrackingManager {
    @Inject(method = "startTracking", at = @At("HEAD"), cancellable = true)
    public void startTracking(Entity entity, CallbackInfo info) {
//        EntityRegistryEntry entityEntry = RegistryMod.entityRM.getEntryForClass(entity.getClass());
//        if (entityEntry != null) {
//            startTracking(entity, entityEntry.trackingRange, entityEntry.updateRateTicks, entityEntry.sendVelocityUpdates);
//            info.cancel();
//        }
    }

    @Shadow
    public void startTracking(Entity entity, int trackingRange, int updateRateTicks, boolean sendVelocityUpdates) {

    }
}
