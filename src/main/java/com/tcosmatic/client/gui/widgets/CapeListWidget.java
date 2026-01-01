package com.tcosmatic.client.gui.widgets;

import com.tcosmatic.util.CapeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

public class CapeListWidget extends AlwaysSelectedEntryListWidget<CapeListWidget.CapeEntry> {

    private CapeManager.CapeData selectedCape;

    public CapeListWidget(MinecraftClient client, int width, int height, int top, int bottom) {
        super(client, width, height, top, bottom, 32);
    }

    public void addCape(CapeManager.CapeData cape) {
        addEntry(new CapeEntry(cape));
    }

    public CapeManager.CapeData getSelectedCape() {
        return selectedCape;
    }

    public class CapeEntry extends AlwaysSelectedEntryListWidget.Entry<CapeEntry> {

        private final CapeManager.CapeData cape;

        public CapeEntry(CapeManager.CapeData cape) {
            this.cape = cape;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int w, int h,
                           int mouseX, int mouseY, boolean hovered, float delta) {

            TextRenderer tr = MinecraftClient.getInstance().textRenderer;

            if (cape == selectedCape) {
                context.fill(x, y, x + w, y + h, 0x803F51B5);
            } else if (hovered) {
                context.fill(x, y, x + w, y + h, 0x40FFFFFF);
            }

            if (cape.getTextureId() != null) {
                context.drawTexture(
                        cape.getTextureId(),
                        x + 4, y + 2,
                        0, 0,
                        24, 16,
                        64, 32
                );
            }

            context.drawTextWithShadow(
                    tr,
                    Text.literal(cape.getName()),
                    x + 36,
                    y + 10,
                    0xFFFFFF
            );
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            selectedCape = cape;
            setSelected(this);
            return true;
        }

        @Override
        public Text getNarration() {
            return Text.literal("Cape " + cape.getName());
        }
    }
}
