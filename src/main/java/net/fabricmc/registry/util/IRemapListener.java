package net.fabricmc.registry.util;

/**
 * Created by asie on 8/24/16.
 */
public interface IRemapListener {
    void onBeforeRemap();
    void onAfterRemap();
}
