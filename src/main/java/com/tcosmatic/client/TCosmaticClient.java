package com.tcosmatic.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TCosmaticClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Initialize Client side logic (Renderers, Screens)
    }
}
