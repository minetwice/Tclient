package com.tcosmatic.client.gui.widgets;

import com.tcosmatic.client.gui.CapeEditorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;

public class ToolbarWidget implements Drawable, Element, Selectable {

    private final int x, y, width, height;
    private final CapeEditorScreen parent;
    private String selectedTool = "brush";

    private static final String[] TOOLS = {
            "brush", "bucket", "eraser", "eyedropper", "line", "rectangle"
    };

    private static final String[] ICONS = {
            "B", "F", "E", "P", "L", "R"
    };

    private static final int TOOL_SIZE = 20;
    private static final int TOOL_SPACING = 4;

    public ToolbarWidget(int x, int y, int width, int height, CapeEditorScreen parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;

        context.fill(x, y, x + width, y + height, 0xAA000000);

        context.drawTextWithShadow(tr, Text.literal("Tools"), x + 6, y + 6, 0xFFFFFF);

        int startY = y + 24;

        for (int i = 0; i < TOOLS.length; i++) {
            int ty = startY + i * (TOOL_SIZE + TOOL_SPACING);
            boolean selected = TOOLS[i].equals(selectedTool);

            int bg = selected ? 0xFF3F51B5 : 0xFF444444;
            context.fill(x + 6, ty, x + 6 + TOOL_SIZE, ty + TOOL_SIZE, bg);

            if (isMouseOver(x + 6, ty, TOOL_SIZE, TOOL_SIZE, mouseX, mouseY)) {
                context.fill(x + 6, ty, x + 6 + TOOL_SIZE, ty + TOOL_SIZE, 0x40FFFFFF);
            }

            context.drawTextWithShadow(
                    tr,
                    Text.literal(ICONS[i]),
                    x + 12,
                    ty + 6,
                    0xFFFFFF
            );

            context.drawTextWithShadow(
                    tr,
                    Text.literal(TOOLS[i]),
                    x + 32,
                    ty + 6,
                    0xCCCCCC
            );
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startY = y + 24;

        for (int i = 0; i < TOOLS.length; i++) {
            int ty = startY + i * (TOOL_SIZE + TOOL_SPACING);
            if (isMouseOver(x + 6, ty, TOOL_SIZE, TOOL_SIZE, (int) mouseX, (int) mouseY)) {
                selectedTool = TOOLS[i];
                parent.setTool(selectedTool);
                return true;
            }
        }
        return false;
    }

    private boolean isMouseOver(int x, int y, int w, int h, int mx, int my) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    @Override public SelectionType getType() { return SelectionType.NONE; }
    @Override public void appendNarrations(NarrationMessageBuilder builder) {}
    @Override public void setFocused(boolean focused) {}
    @Override public boolean isFocused() { return false; }
}
