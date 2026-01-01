package com.tcosmatic;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCosmaticMod implements ModInitializer {
    public static final String MOD_ID = "tcosmatic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing TCosmatic Mod!");
        // Initialize Network, Config, etc. here
    }
}
