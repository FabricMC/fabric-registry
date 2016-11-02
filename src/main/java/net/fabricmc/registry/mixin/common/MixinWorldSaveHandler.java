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

import net.fabricmc.registry.Registries;
import net.fabricmc.registry.util.exception.RegistryMappingNotFoundException;
import net.minecraft.nbt.TagCompound;

import net.minecraft.nbt.TagStorageHelper;
import net.minecraft.world.WorldSaveHandlerAnvil;
import net.minecraft.world.WorldSaveHandlerOld;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Mixin(value = WorldSaveHandlerOld.class)
public class MixinWorldSaveHandler {
    private static final int ID_REGISTRY_BACKUPS = 3;
    private TagCompound lastSavedIdMap = null;

    @Shadow
    public File worldDataDir;

    private boolean readWorldIdMap(File file) {
        try {
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                TagCompound tag = TagStorageHelper.readCompoundTagCompressed(fileInputStream);
                fileInputStream.close();
                if (tag != null) {
                    Registries.applySerializedIdMap(tag);
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            return false;
        } catch (RegistryMappingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private File getWorldIdMapFile(int i) {
        return new File(worldDataDir, "fabricRegistry" + ".dat" + (i == 0 ? "" : ("." + i)));
    }

    // TODO: stop double save on client?
    @Inject(method="readProperties", at=@At("HEAD"))
    public void readWorldProperties(CallbackInfoReturnable<LevelProperties> callbackInfo) {
        // Load
        for (int i = 0; i < ID_REGISTRY_BACKUPS; i++) {
            if (readWorldIdMap(getWorldIdMapFile(i))) {
                break;
            }
        }

        TagCompound newIdMap = Registries.serializeIdMap();
        if (lastSavedIdMap == null || !newIdMap.equals(lastSavedIdMap)) {
            for (int i = ID_REGISTRY_BACKUPS - 1; i >= 0; i--) {
                File file = getWorldIdMapFile(i);
                if (file.exists()) {
                    if (i == ID_REGISTRY_BACKUPS - 1) {
                        file.delete();
                    } else {
                        File target = getWorldIdMapFile(i + 1);
                        file.renameTo(target);
                    }
                }
            }

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(getWorldIdMapFile(0));
                TagStorageHelper.writeCompoundTagCompressed(newIdMap, fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            lastSavedIdMap = newIdMap;
        }
    }
}
