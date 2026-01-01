package com.tcosmatic.network.packets;

import net.minecraft.network.PacketByteBuf;

public class CapeSyncPacket {
    public static void encode(CapeSyncPacket packet, PacketByteBuf buf) {}
    public static CapeSyncPacket decode(PacketByteBuf buf) { return new CapeSyncPacket(); }
}
