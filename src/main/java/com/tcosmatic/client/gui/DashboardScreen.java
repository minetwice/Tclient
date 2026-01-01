package com.tcosmatic.client.gui;

import com.tcosmatic.client.TCosmaticClient;
import com.tcosmatic.client.gui.widgets.CapeListWidget;
import com.tcosmatic.client.render.PlayerModelRenderer;
import com.tcosmatic.util.CapeManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class DashboardScreen extends Screen {
    
    private CapeListWidget capeList;
    private PlayerModelRenderer playerRenderer;
    private CapeManager.CapeData selectedCape;
    private int previewX, previewY;
    
    public DashboardScreen() {
        super(Text.literal("TCosmatic Dashboard"));
    }
    
    @Override
    protected void init() {
        super.init();
        
        int listWidth = 200;
        int listHeight = this.height - 80;
        
        // Cape list on the left
        capeList = new CapeListWidget(
            this.client,
            listWidth,
            listHeight,
            40,
            this.height - 40,
            32
        );
        
        // Load capes into list
        for (CapeManager.CapeData cape : TCosmaticClient.getCapeManager().getAllCapes()) {
            capeList.addEntry(cape);
        }
        
        capeList.setSelectedCape(TCosmaticClient.getCapeManager().getSelectedCape());
        addSelectableChild(capeList);
        
        // Preview area position
        previewX = listWidth + (this.width - listWidth) / 2;
        previewY = this.height / 2;
        
        // Initialize player renderer
        playerRenderer = new PlayerModelRenderer(client);
        
        // Buttons
        int buttonWidth = 120;
        int buttonHeight = 20;
        int buttonY = this.height - 30;
        
        // Cape Editor Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Cape Editor"),
            button -> {
                if (selectedCape != null) {
                    client.setScreen(new CapeEditorScreen(selectedCape, this));
                }
            })
            .dimensions(listWidth + 10, buttonY, buttonWidth, buttonHeight)
            .build()
        );
        
        // Apply Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Apply Cape"),
            button -> {
                if (selectedCape != null) {
                    TCosmaticClient.getCapeManager().selectCape(selectedCape.getName());
                }
                client.setScreen(null);
            })
            .dimensions(listWidth + 140, buttonY, buttonWidth, buttonHeight)
            .build()
        );
        
        // Remove Cape Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Remove Cape"),
            button -> {
                TCosmaticClient.getCapeManager().clearCape();
                selectedCape = null;
            })
            .dimensions(listWidth + 270, buttonY, buttonWidth, buttonHeight)
            .build()
        );
        
        // Close Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Close"),
            button -> client.setScreen(null))
            .dimensions(this.width - 130, buttonY, buttonWidth, buttonHeight)
            .build()
        );
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Background
        renderBackground(context, mouseX, mouseY, delta);
        
        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        
        // Cape list
        capeList.render(context, mouseX, mouseY, delta);
        
        // Get selected cape from list
        selectedCape = capeList.getSelectedCape();
        
        // Preview panel background
        int panelX = 210;
        int panelY = 40;
        int panelWidth = this.width - 220;
        int panelHeight = this.height - 80;
        
        context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x80000000);
        context.drawBorder(panelX, panelY, panelWidth, panelHeight, 0xFF555555);
        
        // Preview label
        context.drawTextWithShadow(this.textRenderer, "Preview", panelX + 10, panelY + 10, 0xFFFFFF);
        
        // Render player preview with cape
        if (selectedCape != null && client.player != null) {
            playerRenderer.renderPlayerWithCape(
                context,
                previewX,
                previewY,
                50,
                previewX - mouseX,
                previewY - 30 - mouseY,
                client.player,
                selectedCape
            );
            
            // Cape info
            context.drawTextWithShadow(
                this.textRenderer,
                "Cape: " + selectedCape.getName(),
                panelX + 10,
                panelY + panelHeight - 30,
                0xFFFFFF
            );
        } else {
            context.drawCenteredTextWithShadow(
                this.textRenderer,
                "No cape selected",
                previewX,
                previewY,
                0x888888
            );
        }
        
        // Render buttons
        super.render(context, mouseX, mouseY, delta);
        
        // Info text
        context.drawTextWithShadow(
            this.textRenderer,
            "Place PNG capes in: .minecraft/TCosmatic/capes/",
            10,
            this.height - 15,
            0x888888
        );
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
