package com.tcosmatic.network;

import com.tcosmatic.TCosmaticMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;

public class NetworkHandler {

    public static final Identifier CAPE_SYNC_ID = new Identifier(TCosmaticMod.MOD_ID, "cape_sync");

    public static void registerPackets() {
        // Client-side packet registration
        ClientPlayNetworking.registerGlobalReceiver(CAPE_SYNC_ID, (client, handler, buf, responseSender) -> {
            String playerUuid = buf.readString();
            String capeName = buf.readString();

            client.execute(() -> {
                TCosmaticMod.LOGGER.info("Received cape sync for player: " + playerUuid + " - Cape: " + capeName);
                // TODO: Store playerUuid -> capeName mapping for rendering other players
            });
        });
    }

    public static void sendCapeSync(String capeName) {
        if (!ClientPlayNetworking.canSend(CAPE_SYNC_ID)) return;

        MinecraftClient client = MinecraftClient.getInstance();

        PacketByteBuf buf = new PacketByteBuf(PacketByteBufs.create());
        buf.writeString(client.player.getUuidAsString());
        buf.writeString(capeName);

        ClientPlayNetworking.send(CAPE_SYNC_ID, buf);
        TCosmaticMod.LOGGER.info("Sent cape sync: " + capeName);
    }
}
