package com.tcosmatic.network.packets;

import net.minecraft.network.PacketByteBuf;

public class CapeDataPacket {
    public static void encode(CapeDataPacket packet, PacketByteBuf buf) {}
    public static CapeDataPacket decode(PacketByteBuf buf) { return new CapeDataPacket(); }
}
