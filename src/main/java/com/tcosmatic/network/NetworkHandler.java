package com.tcosmatic.network;

import com.tcosmatic.TCosmaticMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class NetworkHandler {
    
    public static final Identifier CAPE_SYNC_ID = new Identifier(TCosmaticMod.MOD_ID, "cape_sync");
    
    public static void registerPackets() {
        TCosmaticMod.LOGGER.info("Registering network packets for cape sync");
        
        // Register client-to-server packet handler
        ClientPlayNetworking.registerGlobalReceiver(CAPE_SYNC_ID, (client, handler, buf, responseSender) -> {
            // Read cape data from buffer
            String playerUuid = buf.readString();
            String capeName = buf.readString();
            
            client.execute(() -> {
                // Update cape for other players
                TCosmaticMod.LOGGER.info("Received cape sync for player: " + playerUuid + " - Cape: " + capeName);
                // Store in a map of UUID -> Cape for rendering other players
            });
        });
    }
    
    public static void sendCapeSync(String capeName) {
        if (!ClientPlayNetworking.canSend(CAPE_SYNC_ID)) {
            return;
        }
        
        // Create packet to send cape info to server/other clients
        // Note: This is client-side only, so sync only works if both players have the mod
        TCosmaticMod.LOGGER.info("Sending cape sync: " + capeName);
    }
    
    // Custom payload for cape data
    public record CapeSyncPayload(String playerUuid, String capeName) implements CustomPayload {
        public static final CustomPayload.Id<CapeSyncPayload> ID = new CustomPayload.Id<>(CAPE_SYNC_ID);
        public static final PacketCodec<PacketByteBuf, CapeSyncPayload> CODEC = PacketCodec.of(
            (value, buf) -> {
                buf.writeString(value.playerUuid);
                buf.writeString(value.capeName);
            },
            buf -> new CapeSyncPayload(buf.readString(), buf.readString())
        );
        
        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
