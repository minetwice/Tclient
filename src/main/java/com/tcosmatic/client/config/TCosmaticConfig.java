package com.tcosmatic.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcosmatic.TCosmaticMod;

import java.io.*;

public class TCosmaticConfig {
    
    private static final File CONFIG_FILE = new File(TCosmaticMod.CONFIG_DIR, "config.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    private String selectedCape;
    private boolean showCapeInFirstPerson = false;
    private boolean showOtherPlayersCapes = true;
    private float capeOpacity = 1.0f;
    private boolean animateCape = true;
    private boolean capePhysics = true;
    
    public TCosmaticConfig() {
        load();
    }
    
    public void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }
        
        try (Reader reader = new FileReader(CONFIG_FILE)) {
            TCosmaticConfig loaded = GSON.fromJson(reader, TCosmaticConfig.class);
            if (loaded != null) {
                this.selectedCape = loaded.selectedCape;
                this.showCapeInFirstPerson = loaded.showCapeInFirstPerson;
                this.showOtherPlayersCapes = loaded.showOtherPlayersCapes;
                this.capeOpacity = loaded.capeOpacity;
                this.animateCape = loaded.animateCape;
                this.capePhysics = loaded.capePhysics;
            }
        } catch (Exception e) {
            TCosmaticMod.LOGGER.error("Failed to load config", e);
        }
    }
    
    public void save() {
        try {
            if (!TCosmaticMod.CONFIG_DIR.exists()) {
                TCosmaticMod.CONFIG_DIR.mkdirs();
            }
            
            try (Writer writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (Exception e) {
            TCosmaticMod.LOGGER.error("Failed to save config", e);
        }
    }
    
    // Getters and setters
    public String getSelectedCape() { return selectedCape; }
    public void setSelectedCape(String cape) { 
        this.selectedCape = cape;
        save();
    }
    
    public boolean showCapeInFirstPerson() { return showCapeInFirstPerson; }
    public void setShowCapeInFirstPerson(boolean show) { 
        this.showCapeInFirstPerson = show;
        save();
    }
    
    public boolean showOtherPlayersCapes() { return showOtherPlayersCapes; }
    public void setShowOtherPlayersCapes(boolean show) { 
        this.showOtherPlayersCapes = show;
        save();
    }
    
    public float getCapeOpacity() { return capeOpacity; }
    public void setCapeOpacity(float opacity) { 
        this.capeOpacity = Math.max(0.0f, Math.min(1.0f, opacity));
        save();
    }
    
    public boolean isAnimateCape() { return animateCape; }
    public void setAnimateCape(boolean animate) { 
        this.animateCape = animate;
        save();
    }
    
    public boolean isCapePhysics() { return capePhysics; }
    public void setCapePhysics(boolean physics) { 
        this.capePhysics = physics;
        save();
    }
}
