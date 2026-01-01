package com.tcosmatic.client.gui;

import com.tcosmatic.client.gui.widgets.ColorPickerWidget;
import com.tcosmatic.client.gui.widgets.ToolbarWidget;
import com.tcosmatic.client.render.PlayerModelRenderer;
import com.tcosmatic.util.CapeManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class CapeEditorScreen extends Screen {
    
    private final CapeManager.CapeData cape;
    private final Screen parent;
    private PlayerModelRenderer playerRenderer;
    
    private ToolbarWidget toolbar;
    private ColorPickerWidget colorPicker;
    
    // Transform values
    private SliderWidget scaleSlider;
    private SliderWidget rotationXSlider;
    private SliderWidget rotationYSlider;
    private SliderWidget rotationZSlider;
    private SliderWidget offsetXSlider;
    private SliderWidget offsetYSlider;
    
    private String currentTool = "brush";
    private int brushSize = 1;
    private int selectedColor = 0xFFFFFFFF;
    
    public CapeEditorScreen(CapeManager.CapeData cape, Screen parent) {
        super(Text.literal("Cape Editor - " + cape.getName()));
        this.cape = cape;
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        super.init();
        
        playerRenderer = new PlayerModelRenderer(client);
        
        // Toolbar
        toolbar = new ToolbarWidget(10, 40, 200, 300, this);
        addSelectableChild(toolbar);
        
        // Color picker
        colorPicker = new ColorPickerWidget(10, 350, 200, 150, color -> {
            this.selectedColor = color;
        });
        addSelectableChild(colorPicker);
        
        int rightPanelX = this.width - 210;
        int sliderWidth = 200;
        int sliderHeight = 20;
        int startY = 60;
        int spacing = 30;
        
        // Scale slider
        addDrawableChild(new SliderWidget(
            rightPanelX, startY, sliderWidth, sliderHeight,
            Text.literal("Scale: " + String.format("%.2f", cape.getScale())),
            (cape.getScale() - 0.1) / 2.9
        ) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Scale: " + String.format("%.2f", cape.getScale())));
            }
            
            @Override
            protected void applyValue() {
                cape.setScale((float) (0.1 + value * 2.9));
            }
        });
        
        // Rotation X slider
        addDrawableChild(new SliderWidget(
            rightPanelX, startY + spacing, sliderWidth, sliderHeight,
            Text.literal("Rotation X: " + (int)cape.getRotationX() + "°"),
            cape.getRotationX() / 360.0
        ) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Rotation X: " + (int)cape.getRotationX() + "°"));
            }
            
            @Override
            protected void applyValue() {
                cape.setRotationX((float) (value * 360));
            }
        });
        
        // Rotation Y slider
        addDrawableChild(new SliderWidget(
            rightPanelX, startY + spacing * 2, sliderWidth, sliderHeight,
            Text.literal("Rotation Y: " + (int)cape.getRotationY() + "°"),
            cape.getRotationY() / 360.0
        ) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Rotation Y: " + (int)cape.getRotationY() + "°"));
            }
            
            @Override
            protected void applyValue() {
                cape.setRotationY((float) (value * 360));
            }
        });
        
        // Rotation Z slider
        addDrawableChild(new SliderWidget(
            rightPanelX, startY + spacing * 3, sliderWidth, sliderHeight,
            Text.literal("Rotation Z: " + (int)cape.getRotationZ() + "°"),
            cape.getRotationZ() / 360.0
        ) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Rotation Z: " + (int)cape.getRotationZ() + "°"));
            }
            
            @Override
            protected void applyValue() {
                cape.setRotationZ((float) (value * 360));
            }
        });
        
        // Offset X slider
        addDrawableChild(new SliderWidget(
            rightPanelX, startY + spacing * 4, sliderWidth, sliderHeight,
            Text.literal("Offset X: " + String.format("%.2f", cape.getOffsetX())),
            (cape.getOffsetX() + 2) / 4.0
        ) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Offset X: " + String.format("%.2f", cape.getOffsetX())));
            }
            
            @Override
            protected void applyValue() {
                cape.setOffsetX((float) (value * 4 - 2));
            }
        });
        
        // Offset Y slider
        addDrawableChild(new SliderWidget(
            rightPanelX, startY + spacing * 5, sliderWidth, sliderHeight,
            Text.literal("Offset Y: " + String.format("%.2f", cape.getOffsetY())),
            (cape.getOffsetY() + 2) / 4.0
        ) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Offset Y: " + String.format("%.2f", cape.getOffsetY())));
            }
            
            @Override
            protected void applyValue() {
                cape.setOffsetY((float) (value * 4 - 2));
            }
        });
        
        // Save and Back buttons
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Save & Apply"),
            button -> {
                client.setScreen(parent);
            })
            .dimensions(rightPanelX, this.height - 60, 95, 20)
            .build()
        );
        
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Cancel"),
            button -> client.setScreen(parent))
            .dimensions(rightPanelX + 105, this.height - 60, 95, 20)
            .build()
        );
        
        // Reset button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Reset Transform"),
            button -> {
                cape.setScale(1.0f);
                cape.setRotationX(0);
                cape.setRotationY(0);
                cape.setRotationZ(0);
                cape.setOffsetX(0);
                cape.setOffsetY(0);
                cape.setOffsetZ(0);
                this.clearAndInit();
            })
            .dimensions(rightPanelX, this.height - 90, 200, 20)
            .build()
        );
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        
        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        
        // Preview panel
        int previewX = 220;
        int previewY = 40;
        int previewWidth = this.width - 440;
        int previewHeight = this.height - 80;
        
        context.fill(previewX, previewY, previewX + previewWidth, previewY + previewHeight, 0x80000000);
        context.drawBorder(previewX, previewY, previewWidth, previewHeight, 0xFF555555);
        
        // Render player with cape
        if (client.player != null) {
            int centerX = previewX + previewWidth / 2;
            int centerY = previewY + previewHeight / 2;
            
            playerRenderer.renderPlayerWithCape(
                context,
                centerX,
                centerY + 50,
                60,
                centerX - mouseX,
                centerY - mouseY,
                client.player,
                cape
            );
        }
        
        // Toolbar and color picker
        toolbar.render(context, mouseX, mouseY, delta);
        colorPicker.render(context, mouseX, mouseY, delta);
        
        // Labels for right panel
        context.drawTextWithShadow(this.textRenderer, "Transform Controls", this.width - 210, 40, 0xFFFFFF);
        
        super.render(context, mouseX, mouseY, delta);
        
        // Tool info
        context.drawTextWithShadow(
            this.textRenderer,
            "Current Tool: " + currentTool.toUpperCase(),
            10,
            this.height - 15,
            0xFFFFFF
        );
    }
    
    public void setTool(String tool) {
        this.currentTool = tool;
    }
    
    public void setBrushSize(int size) {
        this.brushSize = Math.max(1, Math.min(10, size));
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
                       }
