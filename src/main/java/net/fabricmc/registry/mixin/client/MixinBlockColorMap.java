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

@Mixin(value = BlockColorMap.class)
public class MixinBlockColorMap implements IRemapListener {
    @Shadow
    public IdList<IBlockColorMapper> mappers;
    private Map<Block, IBlockColorMapper> remapTemp;

    @Override
    public void onBeforeRemap() {
        if (remapTemp == null) {
            remapTemp = new HashMap<>();
        }

        for (int i = 0; i < mappers.size(); i++) {
            IBlockColorMapper mapper = mappers.get(i);
            if (mapper != null) {
                Block b = Block.getBlockByRawId(i);
                if (b != null) {
                    remapTemp.put(b, mapper);
                }
            }
        }
        RegistryModUtils.clear(mappers);
    }

    @Override
    public void onAfterRemap() {
        BlockColorMap mapper = (BlockColorMap) ((Object) this);
        for (Map.Entry<Block, IBlockColorMapper> entry : remapTemp.entrySet()) {
            mapper.register(entry.getValue(), entry.getKey());
        }
        remapTemp.clear();
    }
}
