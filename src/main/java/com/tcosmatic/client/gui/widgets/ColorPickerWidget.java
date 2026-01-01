package com.tcosmatic.client.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;

import java.util.function.Consumer;

public class ColorPickerWidget implements Drawable, Element, Selectable {
    
    private final int x, y, width, height;
    private final Consumer<Integer> onColorChange;
    private int selectedColor = 0xFFFFFFFF;
    
    // Predefined color palette
    private static final int[] COLORS = {
        0xFFFFFFFF, 0xFFCCCCCC, 0xFF999999, 0xFF666666, 0xFF333333, 0xFF000000,
        0xFFFF0000, 0xFFFF8800, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF,
        0xFFFF00FF, 0xFF880000, 0xFF888800, 0xFF008800, 0xFF008888, 0xFF000088,
        0xFFFFAAAA, 0xFFFFDDAA, 0xFFFFFFAA, 0xFFAAFFAA, 0xFFAAFFFF, 0xFFAAAAFF
    };
    
    private static final int COLOR_SIZE = 20;
    private static final int COLORS_PER_ROW = 6;
    
    public ColorPickerWidget(int x, int y, int width, int height, Consumer<Integer> onColorChange) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.onColorChange = onColorChange;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Background
        context.fill(x, y, x + width, y + height, 0x80000000);
        context.drawBorder(x, y, width, height, 0xFF555555);
        
        // Title
        context.drawTextWithShadow(
            context.getMatrices().peek().getPositionMatrix(),
            "Color Picker",
            x + 10,
            y + 10,
            0xFFFFFF
        );
        
        // Draw color palette
        int startY = y + 30;
        for (int i = 0; i < COLORS.length; i++) {
            int col = i % COLORS_PER_ROW;
            int row = i / COLORS_PER_ROW;
            
            int colorX = x + 10 + col * (COLOR_SIZE + 5);
            int colorY = startY + row * (COLOR_SIZE + 5);
            
            // Color swatch
            context.fill(colorX, colorY, colorX + COLOR_SIZE, colorY + COLOR_SIZE, COLORS[i]);
            
            // Selection border
            if (COLORS[i] == selectedColor) {
                context.drawBorder(colorX - 2, colorY - 2, COLOR_SIZE + 4, COLOR_SIZE + 4, 0xFFFFFFFF);
            }
            
            // Hover effect
            if (isMouseOver(colorX, colorY, COLOR_SIZE, COLOR_SIZE, mouseX, mouseY)) {
                context.drawBorder(colorX - 1, colorY - 1, COLOR_SIZE + 2, COLOR_SIZE + 2, 0xFF888888);
            }
        }
        
        // Current color display
        int currentColorY = startY + ((COLORS.length / COLORS_PER_ROW) + 1) * (COLOR_SIZE + 5);
        context.drawTextWithShadow(
            context.getMatrices().peek().getPositionMatrix(),
            "Current:",
            x + 10,
            currentColorY,
            0xFFFFFF
        );
        
        context.fill(x + 70, currentColorY - 2, x + 120, currentColorY + 18, selectedColor);
        context.drawBorder(x + 70, currentColorY - 2, 50, 20, 0xFFFFFFFF);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startY = y + 30;
        
        for (int i = 0; i < COLORS.length; i++) {
            int col = i % COLORS_PER_ROW;
            int row = i / COLORS_PER_ROW;
            
            int colorX = x + 10 + col * (COLOR_SIZE + 5);
            int colorY = startY + row * (COLOR_SIZE + 5);
            
            if (isMouseOver(colorX, colorY, COLOR_SIZE, COLOR_SIZE, (int)mouseX, (int)mouseY)) {
                selectedColor = COLORS[i];
                onColorChange.accept(selectedColor);
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isMouseOver(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
    
    public int getSelectedColor() {
        return selectedColor;
    }
    
    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }
    
    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {}
    
    @Override
    public void setFocused(boolean focused) {}
    
    @Override
    public boolean isFocused() {
        return false;
    }
}
