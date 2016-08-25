package net.fabricmc.registry.mixin.common;

import net.fabricmc.registry.Registries;
import net.fabricmc.registry.util.exception.RegistryMappingNotFoundException;
import net.minecraft.nbt.TagCompound;
import net.minecraft.util.TagStorageHelper;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.WorldSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Mixin(value = WorldSaveHandler.class, remap = false)
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

    // TODO: stop double save
    @Inject(method="readWorldProperties", at=@At("HEAD"))
    public void readWorldProperties(CallbackInfoReturnable<WorldProperties> callbackInfo) {
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
