package com.tcosmatic;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class TCosmaticMod implements ModInitializer {
    public static final String MOD_ID = "tcosmatic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    public static File TCOSMATIC_DIR;
    public static File CAPES_DIR;
    public static File CONFIG_DIR;
    
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing TCosmatic Mod...");
        
        // Create mod directories
        createDirectories();
        
        LOGGER.info("TCosmatic Mod initialized successfully!");
    }
    
    private void createDirectories() {
        try {
            // Get Minecraft directory
            File minecraftDir = new File(".");
            
            // Create TCosmatic folder
            TCOSMATIC_DIR = new File(minecraftDir, "TCosmatic");
            if (!TCOSMATIC_DIR.exists()) {
                TCOSMATIC_DIR.mkdirs();
                LOGGER.info("Created TCosmatic directory at: " + TCOSMATIC_DIR.getAbsolutePath());
            }
            
            // Create capes subfolder
            CAPES_DIR = new File(TCOSMATIC_DIR, "capes");
            if (!CAPES_DIR.exists()) {
                CAPES_DIR.mkdirs();
                LOGGER.info("Created capes directory at: " + CAPES_DIR.getAbsolutePath());
            }
            
            // Create config subfolder
            CONFIG_DIR = new File(TCOSMATIC_DIR, "config");
            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
                LOGGER.info("Created config directory");
            }
            
        } catch (Exception e) {
            LOGGER.error("Failed to create directories", e);
        }
    }
}
