package com.tcosmatic.client.gui.widgets;

import com.tcosmatic.util.CapeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

public class CapeListWidget extends AlwaysSelectedEntryListWidget<CapeListWidget.CapeEntry> {
    
    private CapeManager.CapeData selectedCape;
    
    public CapeListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }
    
    public void addEntry(CapeManager.CapeData cape) {
        this.addEntry(new CapeEntry(cape, this));
    }
    
    public void setSelectedCape(CapeManager.CapeData cape) {
        this.selectedCape = cape;
        if (cape != null) {
            for (CapeEntry entry : this.children()) {
                if (entry.cape == cape) {
                    this.setSelected(entry);
                    break;
                }
            }
        }
    }
    
    public CapeManager.CapeData getSelectedCape() {
        return selectedCape;
    }
    
    @Override
    public int getRowWidth() {
        return this.width - 10;
    }
    
    @Override
    protected int getScrollbarPositionX() {
        return this.width - 6;
    }
    
    public class CapeEntry extends Entry<CapeEntry> {
        private final CapeManager.CapeData cape;
        private final CapeListWidget parent;
        
        public CapeEntry(CapeManager.CapeData cape, CapeListWidget parent) {
            this.cape = cape;
            this.parent = parent;
        }
        
        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, 
                          int mouseX, int mouseY, boolean hovered, float tickDelta) {
            
            MinecraftClient client = MinecraftClient.getInstance();
            
            // Background
            if (parent.selectedCape == cape) {
                context.fill(x, y, x + entryWidth, y + entryHeight, 0x80FFFFFF);
            } else if (hovered) {
                context.fill(x, y, x + entryWidth, y + entryHeight, 0x40FFFFFF);
            }
            
            // Cape icon (small preview)
            if (cape.getTextureId() != null) {
                context.drawTexture(cape.getTextureId(), x + 2, y + 2, 0, 0, 28, 28, 64, 32);
            }
            
            // Cape name
            context.drawTextWithShadow(
                client.textRenderer,
                cape.getName(),
                x + 35,
                y + 10,
                0xFFFFFF
            );
        }
        
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            parent.selectedCape = this.cape;
            parent.setSelected(this);
            return true;
        }
        
        @Override
        public Text getNarration() {
            return Text.literal("Cape: " + cape.getName());
        }
    }
}
