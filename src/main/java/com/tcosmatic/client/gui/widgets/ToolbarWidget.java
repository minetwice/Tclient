package com.tcosmatic.client.gui.widgets;

import com.tcosmatic.client.gui.CapeEditorScreen;
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
    
    private static final int TOOL_SIZE = 32;
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
        // Background
        context.fill(x, y, x + width, y + height, 0x80000000);
        context.drawBorder(x, y, width, height, 0xFF555555);
        
        // Title
        context.drawTextWithShadow(
            context.getMatrices().peek().getPositionMatrix(),
            "Tools",
            x + 10,
            y + 10,
            0xFFFFFF
        );
        
        // Draw tool buttons
        int startY = y + 30;
        for (int i = 0; i < TOOLS.length; i++) {
            String tool = TOOLS[i];
            int toolY = startY + i * (TOOL_SIZE + TOOL_SPACING);
            
            // Button background
            int color = tool.equals(selectedTool) ? 0xFF4444FF : 0xFF444444;
            context.fill(x + 10, toolY, x + 10 + TOOL_SIZE, toolY + TOOL_SIZE, color);
            
            // Hover effect
            if (isMouseOver(x + 10, toolY, TOOL_SIZE, TOOL_SIZE, mouseX, mouseY)) {
                context.fill(x + 10, toolY, x + 10 + TOOL_SIZE, toolY + TOOL_SIZE, 0x40FFFFFF);
            }
            
            // Tool icon (simplified text for now)
            String icon = getToolIcon(tool);
            context.drawCenteredTextWithShadow(
                context.getMatrices().peek().getPositionMatrix(),
                icon,
                x + 10 + TOOL_SIZE / 2,
                toolY + TOOL_SIZE / 2 - 4,
                0xFFFFFF
            );
            
            // Tool name
            context.drawTextWithShadow(
                context.getMatrices().peek().getPositionMatrix(),
                tool,
                x + 50,
                toolY + 10,
                0xFFFFFF
            );
        }
        
        // Brush size controls
        int brushY = startY + TOOLS.length * (TOOL_SIZE + TOOL_SPACING) + 20;
        context.drawTextWithShadow(
            context.getMatrices().peek().getPositionMatrix(),
            "Brush Size:",
            x + 10,
            brushY,
            0xFFFFFF
        );
        
        // Size buttons
        for (int size = 1; size <= 5; size++) {
            int btnX = x + 10 + (size - 1) * 30;
            int btnY = brushY + 15;
            
            context.fill(btnX, btnY, btnX + 25, btnY + 25, 0xFF444444);
            
            if (isMouseOver(btnX, btnY, 25, 25, mouseX, mouseY)) {
                context.fill(btnX, btnY, btnX + 25, btnY + 25, 0x40FFFFFF);
            }
            
            context.drawCenteredTextWithShadow(
                context.getMatrices().peek().getPositionMatrix(),
                String.valueOf(size),
                btnX + 12,
                btnY + 8,
                0xFFFFFF
            );
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startY = y + 30;
        
        // Check tool buttons
        for (int i = 0; i < TOOLS.length; i++) {
            String tool = TOOLS[i];
            int toolY = startY + i * (TOOL_SIZE + TOOL_SPACING);
            
            if (isMouseOver(x + 10, toolY, TOOL_SIZE, TOOL_SIZE, (int)mouseX, (int)mouseY)) {
                selectedTool = tool;
                parent.setTool(tool);
                return true;
            }
        }
        
        // Check brush size buttons
        int brushY = startY + TOOLS.length * (TOOL_SIZE + TOOL_SPACING) + 35;
        for (int size = 1; size <= 5; size++) {
            int btnX = x + 10 + (size - 1) * 30;
            
            if (isMouseOver(btnX, brushY, 25, 25, (int)mouseX, (int)mouseY)) {
                parent.setBrushSize(size);
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isMouseOver(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
    
    private String getToolIcon(String tool) {
        return switch (tool) {
            case "brush" -> "🖌";
            case "bucket" -> "🪣";
            case "eraser" -> "🧽";
            case "eyedropper" -> "💧";
            case "line" -> "📏";
            case "rectangle" -> "▭";
            default -> "?";
        };
    }
    
    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }
    
    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationMessageBuilder.EMPTY);
    }
    
    @Override
    public void setFocused(boolean focused) {}
    
    @Override
    public boolean isFocused() {
        return false;
    }
}
