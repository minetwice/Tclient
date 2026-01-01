package com.tcosmatic.network;

import com.tcosmatic.TCosmaticMod;
import com.tcosmatic.util.CapeManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NetworkHandler {

    public static final Identifier CAPE_SYNC_ID = new Identifier(TCosmaticMod.MOD_ID, "cape_sync");

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CAPE_SYNC_ID, (client, handler, buf, responseSender) -> {
            String playerUuid = buf.readString();
            String capeName = buf.readString();

            client.execute(() -> {
                TCosmaticMod.LOGGER.info("Received cape sync for player: " + playerUuid + " - Cape: " + capeName);
                // Update CapeManager map for other players
                CapeManager.getInstance().setOtherPlayerCape(playerUuid, capeName);
            });
        });
    }

    public static void sendCapeSync(String capeName) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        PacketByteBuf buf = new PacketByteBuf(net.minecraft.network.PacketByteBufs.create());
        buf.writeString(player.getUuidAsString());
        buf.writeString(capeName);

        ClientPlayNetworking.send(CAPE_SYNC_ID, buf);
        TCosmaticMod.LOGGER.info("Sending cape sync: " + capeName);
    }
}
