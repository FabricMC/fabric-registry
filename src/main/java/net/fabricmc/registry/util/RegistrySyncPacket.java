package net.fabricmc.registry.util;

import net.fabricmc.network.AbstractPacket;
import net.fabricmc.network.Asynchronous;
import net.fabricmc.registry.Registries;
import net.minecraft.nbt.TagCompound;
import net.minecraft.util.PacketByteBuf;

@Asynchronous
public class RegistrySyncPacket extends AbstractPacket {
    private TagCompound idMap;

    public RegistrySyncPacket() {

    }

    public RegistrySyncPacket init() {
        idMap = Registries.serializeIdMap();
        return this;
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeTagCompound(idMap);
    }

    @Override
    public void read(PacketByteBuf packetByteBuf) {
        idMap = packetByteBuf.readTagCompound();
    }

    @Override
    public void handle() {
        try {
            Registries.applySerializedIdMap(idMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
