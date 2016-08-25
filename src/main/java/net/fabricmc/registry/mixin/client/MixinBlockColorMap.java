package net.fabricmc.registry.mixin.client;

import net.fabricmc.registry.util.IRemapListener;
import net.fabricmc.registry.util.RegistryModUtils;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockColorMap;
import net.minecraft.client.render.block.IBlockColorMapper;
import net.minecraft.util.IdList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = BlockColorMap.class, remap = false)
public class MixinBlockColorMap implements IRemapListener {
    @Shadow
    public IdList<IBlockColorMapper> mappers;

    @Override
    public void onBeforeRemap() {
    }

    @Override
    public void onAfterRemap(Map<Integer, Integer> idRemapTable) {
        RegistryModUtils.remapIdList(idRemapTable, mappers);
    }
}
