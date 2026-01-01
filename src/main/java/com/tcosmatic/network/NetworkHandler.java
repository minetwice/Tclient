package com.tcosmatic.network;

import com.tcosmatic.TCosmaticMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class NetworkHandler {

    // ===============================
    // CHANNEL ID
    // ===============================
    public static final Identifier CAPE_SYNC_ID =
            Identifier.of(TCosmaticMod.MOD_ID, "cape_sync");

    // ===============================
    // REGISTER (CLIENT SIDE)
    // ===============================
    public static void registerPackets() {
        TCosmaticMod.LOGGER.info("Registering cape sync packets");

        // Register payload codec
        PayloadTypeRegistry.playC2S().register(
                CapeSyncPayload.ID,
                CapeSyncPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                CapeSyncPayload.ID,
                CapeSyncPayload.CODEC
        );

        // Receiver
        ClientPlayNetworking.registerGlobalReceiver(
                CapeSyncPayload.ID,
                (payload, context) -> {
                    context.client().execute(() -> {
                        TCosmaticMod.LOGGER.info(
                                "Received cape sync | Player: "
                                        + payload.playerUuid()
                                        + " Cape: "
                                        + payload.capeName()
                        );

                        // TODO:
                        // CapeManager.setCape(UUID.fromString(payload.playerUuid()), payload.capeName());
                    });
                }
        );
    }

    // ===============================
    // SEND CAPE DATA
    // ===============================
    public static void sendCapeSync(String capeName) {
        if (ClientPlayNetworking.canSend(CapeSyncPayload.ID)) {
            String uuid = UUID.randomUUID().toString(); // replace with real player UUID

            ClientPlayNetworking.send(
                    new CapeSyncPayload(uuid, capeName)
            );

            TCosmaticMod.LOGGER.info("Sent cape sync: " + capeName);
        }
    }

    // ===============================
    // CUSTOM PAYLOAD (1.21.1)
    // ===============================
    public record CapeSyncPayload(String playerUuid, String capeName)
            implements CustomPayload {

        public static final CustomPayload.Id<CapeSyncPayload> ID =
                new CustomPayload.Id<>(CAPE_SYNC_ID);

        public static final PacketCodec<PacketByteBuf, CapeSyncPayload> CODEC =
                PacketCodec.of(
                        (value, buf) -> {
                            buf.writeString(value.playerUuid);
                            buf.writeString(value.capeName);
                        },
                        buf -> new CapeSyncPayload(
                                buf.readString(),
                                buf.readString()
                        )
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
