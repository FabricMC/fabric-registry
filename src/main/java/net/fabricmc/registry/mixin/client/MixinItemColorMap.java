package net.fabricmc.registry.mixin.client;

import net.fabricmc.registry.util.IRemapListener;
import net.fabricmc.registry.util.RegistryModUtils;
import net.minecraft.client.render.item.IItemColorMapper;
import net.minecraft.client.render.item.ItemColorMap;
import net.minecraft.item.Item;
import net.minecraft.util.IdList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = ItemColorMap.class)
public class MixinItemColorMap implements IRemapListener {
    @Shadow
    public IdList<IItemColorMapper> mappers;
    private Map<Item, IItemColorMapper> remapTemp;

    @Override
    public void onBeforeRemap() {
        if (remapTemp == null) {
            remapTemp = new HashMap<>();
        }

        for (int i = 0; i < mappers.size(); i++) {
            IItemColorMapper mapper = mappers.get(i);
            if (mapper != null) {
                Item b = Item.getItemByRawId(i);
                if (b != null) {
                    remapTemp.put(b, mapper);
                }
            }
        }
        RegistryModUtils.clear(mappers);
    }

    @Override
    public void onAfterRemap() {
        ItemColorMap mapper = (ItemColorMap) ((Object) this);
        for (Map.Entry<Item, IItemColorMapper> entry : remapTemp.entrySet()) {
            mapper.register(entry.getValue(), entry.getKey());
        }
        remapTemp.clear();
    }
}
