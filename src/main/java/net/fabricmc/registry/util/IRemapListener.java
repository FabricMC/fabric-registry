package net.fabricmc.registry.util;

import java.util.Map;

/**
 * Created by asie on 8/24/16.
 */
public interface IRemapListener {
    void onBeforeRemap();
    void onAfterRemap(Map<Integer, Integer> idRemapTable);
}
