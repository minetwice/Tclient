package com.tcosmatic.client.gui.widgets;

import com.tcosmatic.util.CapeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

public class CapeListWidget extends AlwaysSelectedEntryListWidget<CapeListWidget.CapeEntry> {

    private CapeManager.CapeData selectedCape;

    public CapeListWidget(MinecraftClient client, int width, int height, int top, int bottom) {
        super(client, width, height, top, bottom, 32); // 1.21.1 uses 5-arg constructor
    }

    public void addCape(CapeManager.CapeData cape) {
        this.addEntry(new CapeEntry(cape));
    }

    public void setSelectedCape(CapeManager.CapeData cape) {
        this.selectedCape = cape;
        if (cape != null) {
            for (CapeEntry entry : this.children()) {
                if (entry.getCape() == cape) this.setSelected(entry);
            }
        }
    }

    public CapeManager.CapeData getSelectedCape() { return selectedCape; }

    public class CapeEntry extends Entry<CapeEntry> {
        private final CapeManager.CapeData cape;

        public CapeEntry(CapeManager.CapeData cape) { this.cape = cape; }

        public CapeManager.CapeData getCape() { return cape; }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean hovered, float tickDelta) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (cape == selectedCape) context.fill(x, y, x + entryWidth, y + entryHeight, 0x80FFFFFF);
            else if (hovered) context.fill(x, y, x + entryWidth, y + entryHeight, 0x40FFFFFF);

            if (cape.getTextureId() != null) context.drawTexture(cape.getTextureId(), x + 2, y + 2, 0, 0, 28, 28, 64, 32);

            context.drawTextWithShadow(client.textRenderer, cape.getName(), x + 35, y + 10, 0xFFFFFF);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            selectedCape = this.cape;
            CapeListWidget.this.setSelected(this);
            return true;
        }

        @Override
        public Text getNarration() {
            return Text.literal("Cape: " + cape.getName());
        }
    }
}
