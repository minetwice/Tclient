package com.tcosmatic.util;

import com.tcosmatic.TCosmaticMod;

import java.io.File;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

public class FileWatcher {
    
    private final File watchDir;
    private final CapeManager capeManager;
    private Thread watchThread;
    private volatile boolean running = false;
    
    public FileWatcher(File watchDir, CapeManager capeManager) {
        this.watchDir = watchDir;
        this.capeManager = capeManager;
    }
    
    public void start() {
        if (running) return;
        
        running = true;
        watchThread = new Thread(() -> {
            try {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                Path path = watchDir.toPath();
                path.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
                
                TCosmaticMod.LOGGER.info("Started watching cape folder for changes");
                
                while (running) {
                    WatchKey key;
                    try {
                        key = watcher.poll(1, TimeUnit.SECONDS);
                        if (key == null) continue;
                    } catch (InterruptedException e) {
                        break;
                    }
                    
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }
                        
                        @SuppressWarnings("unchecked")
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path filename = ev.context();
                        
                        if (filename.toString().toLowerCase().endsWith(".png")) {
                            TCosmaticMod.LOGGER.info("Detected change in capes folder: " + filename);
                            
                            // Reload capes after a short delay
                            Thread.sleep(500);
                            capeManager.loadCapes();
                        }
                    }
                    
                    key.reset();
                }
                
                watcher.close();
            } catch (Exception e) {
                TCosmaticMod.LOGGER.error("Error in file watcher", e);
            }
        });
        
        watchThread.setDaemon(true);
        watchThread.setName("TCosmatic-FileWatcher");
        watchThread.start();
    }
    
    public void stop() {
        running = false;
        if (watchThread != null) {
            watchThread.interrupt();
        }
    }
}
