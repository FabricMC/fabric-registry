package net.fabricmc.registry.mixin.client;

import net.fabricmc.registry.util.IRemapListener;
import net.fabricmc.registry.util.RegistryModUtils;
import net.minecraft.client.render.item.ItemModelMap;
import net.minecraft.client.render.model.IBakedModel;
import net.minecraft.client.util.ModelIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = ItemModelMap.class, remap = false)
public class MixinItemModelMap implements IRemapListener {
    @Shadow
    public Map<Integer, ModelIdentifier> modelIds;
    @Shadow
    public Map<Integer, IBakedModel> models;

    @Override
    public void onBeforeRemap() {
    }

    @Override
    public void onAfterRemap(Map<Integer, Integer> idRemapTable) {
        remapItemMap(idRemapTable, modelIds);
        remapItemMap(idRemapTable, models);
    }

    private static <T> void remapItemMap(Map<Integer, Integer> idRemapTable, Map<Integer, T> target) {
        Map<Integer, T> remapTemp = new HashMap<>();
        for (Integer oldVal : target.keySet()) {
            int oldIdVal = oldVal >> 16;
            if (idRemapTable.containsKey(oldIdVal)) {
                int newVal = (idRemapTable.get(oldIdVal) << 16) | (oldVal & 0xFFFF);
                remapTemp.put(newVal, target.get(oldVal));
                target.remove(oldVal);
            }
        }

        target.putAll(remapTemp);
    }
}
