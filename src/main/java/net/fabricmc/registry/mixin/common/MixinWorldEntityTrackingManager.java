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

@Mixin(value = WorldEntityTrackingManager.class, remap = false)
public class MixinWorldEntityTrackingManager {
    @Inject(method = "startTracking", at = @At("HEAD"), cancellable = true)
    public void startTracking(Entity entity, CallbackInfo info) {
        EntityRegistryEntry entityEntry = RegistryMod.entityRM.getEntryForClass(entity.getClass());
        if (entityEntry != null) {
            startTracking(entity, entityEntry.trackingRange, entityEntry.updateRateTicks, entityEntry.sendVelocityUpdates);
            info.cancel();
        }
    }

    @Shadow
    public void startTracking(Entity entity, int trackingRange, int updateRateTicks, boolean sendVelocityUpdates) {

    }
}
