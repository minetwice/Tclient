package com.tcosmatic.client;

import com.tcosmatic.TCosmaticMod;
import com.tcosmatic.client.gui.DashboardScreen;
import com.tcosmatic.network.NetworkHandler;
import com.tcosmatic.util.CapeManager;
import com.tcosmatic.util.FileWatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TCosmaticClient implements ClientModInitializer {
    
    private static KeyBinding openMenuKey;
    public static CapeManager capeManager;
    private static FileWatcher fileWatcher;
    
    @Override
    public void onInitializeClient() {
        TCosmaticMod.LOGGER.info("Initializing TCosmatic Client...");
        
        // Initialize cape manager
        capeManager = new CapeManager();
        
        // Initialize network handler
        NetworkHandler.registerPackets();
        
        // Setup keybinding (Right Shift key)
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.tcosmatic.open_menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.tcosmatic"
        ));
        
        // Register tick event for key handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new DashboardScreen());
                }
            }
        });
        
        // Start file watcher for cape folder
        fileWatcher = new FileWatcher(TCosmaticMod.CAPES_DIR, capeManager);
        fileWatcher.start();
        
        TCosmaticMod.LOGGER.info("TCosmatic Client initialized!");
    }
    
    public static CapeManager getCapeManager() {
        return capeManager;
    }
}
