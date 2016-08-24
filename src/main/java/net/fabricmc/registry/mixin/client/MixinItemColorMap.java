package net.fabricmc.registry.mixin.client;

import net.fabricmc.registry.util.IRemapListener;
import net.fabricmc.registry.util.RegistryModUtils;
import net.minecraft.client.render.item.IItemColorMapper;
import net.minecraft.client.render.item.ItemColorMap;
import net.minecraft.util.IdList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(value = ItemColorMap.class)
public class MixinItemColorMap implements IRemapListener {
    @Shadow
    public IdList<IItemColorMapper> mappers;

    @Override
    public void onBeforeRemap() {
    }

    @Override
    public void onAfterRemap(Map<Integer, Integer> idRemapTable) {
        RegistryModUtils.remapIdList(idRemapTable, mappers);
    }
}
