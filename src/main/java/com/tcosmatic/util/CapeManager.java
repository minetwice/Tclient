package com.tcosmatic.util;

import com.tcosmatic.TCosmaticMod;
import com.tcosmatic.client.config.TCosmaticConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class CapeManager {
    
    private final Map<String, CapeData> capes = new HashMap<>();
    private CapeData selectedCape;
    private final TCosmaticConfig config;
    
    public CapeManager() {
        this.config = new TCosmaticConfig();
        loadCapes();
        loadConfig();
    }
    
    public void loadCapes() {
        capes.clear();
        
        File capesDir = TCosmaticMod.CAPES_DIR;
        if (!capesDir.exists()) return;
        
        File[] files = capesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        if (files == null) return;
        
        for (File file : files) {
            try {
                String capeName = file.getName().replace(".png", "");
                NativeImage image = NativeImage.read(new FileInputStream(file));
                
                // Validate cape dimensions (should be 64x32 or 22x17 for elytra-style)
                if ((image.getWidth() == 64 && image.getHeight() == 32) || 
                    (image.getWidth() == 22 && image.getHeight() == 17)) {
                    
                    Identifier texId = Identifier.of(
        TCosmaticMod.MOD_ID,
        "cape_" + capeName
);
                    MinecraftClient.getInstance().getTextureManager()
                        .registerTexture(texId, new NativeImageBackedTexture(image));
                    
                    CapeData capeData = new CapeData(capeName, file, texId);
                    capes.put(capeName, capeData);
                    
                    TCosmaticMod.LOGGER.info("Loaded cape: " + capeName);
                } else {
                    TCosmaticMod.LOGGER.warn("Invalid cape dimensions for " + file.getName() + 
                        " (Expected 64x32 or 22x17, got " + image.getWidth() + "x" + image.getHeight() + ")");
                    image.close();
                }
            } catch (Exception e) {
                TCosmaticMod.LOGGER.error("Failed to load cape: " + file.getName(), e);
            }
        }
    }
    
    public void selectCape(String capeName) {
        selectedCape = capes.get(capeName);
        config.setSelectedCape(capeName);
        config.save();
    }
    
    public void clearCape() {
        selectedCape = null;
        config.setSelectedCape(null);
        config.save();
    }
    
    public CapeData getSelectedCape() {
        return selectedCape;
    }
    
    public Collection<CapeData> getAllCapes() {
        return capes.values();
    }
    
    private void loadConfig() {
        String selectedCapeName = config.getSelectedCape();
        if (selectedCapeName != null && capes.containsKey(selectedCapeName)) {
            selectedCape = capes.get(selectedCapeName);
        }
    }
    
    public TCosmaticConfig getConfig() {
        return config;
    }
    
    public static class CapeData {
        private final String name;
        private final File file;
        private final Identifier textureId;
        private float scale = 1.0f;
        private float rotationX = 0.0f;
        private float rotationY = 0.0f;
        private float rotationZ = 0.0f;
        private float offsetX = 0.0f;
        private float offsetY = 0.0f;
        private float offsetZ = 0.0f;
        
        public CapeData(String name, File file, Identifier textureId) {
            this.name = name;
            this.file = file;
            this.textureId = textureId;
        }
        
        // Getters and setters
        public String getName() { return name; }
        public File getFile() { return file; }
        public Identifier getTextureId() { return textureId; }
        
        public float getScale() { return scale; }
        public void setScale(float scale) { this.scale = Math.max(0.1f, Math.min(3.0f, scale)); }
        
        public float getRotationX() { return rotationX; }
        public void setRotationX(float rotation) { this.rotationX = rotation % 360; }
        
        public float getRotationY() { return rotationY; }
        public void setRotationY(float rotation) { this.rotationY = rotation % 360; }
        
        public float getRotationZ() { return rotationZ; }
        public void setRotationZ(float rotation) { this.rotationZ = rotation % 360; }
        
        public float getOffsetX() { return offsetX; }
        public void setOffsetX(float offset) { this.offsetX = offset; }
        
        public float getOffsetY() { return offsetY; }
        public void setOffsetY(float offset) { this.offsetY = offset; }
        
        public float getOffsetZ() { return offsetZ; }
        public void setOffsetZ(float offset) { this.offsetZ = offset; }
    }
}
