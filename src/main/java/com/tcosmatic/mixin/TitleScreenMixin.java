package com.tcosmatic.mixin;

import com.tcosmatic.client.gui.DashboardScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    
    protected TitleScreenMixin(Text title) {
        super(title);
    }
    
    @Inject(method = "init", at = @At("RETURN"))
    private void addTCosmaticButton(CallbackInfo ci) {
        // Add TCosmatic button to title screen
        int buttonWidth = 200;
        int buttonHeight = 20;
        int x = this.width / 2 - 100;
        int y = this.height / 4 + 48 + 72; // Position below multiplayer button
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("TCosmatic"),
            button -> {
                if (this.client != null) {
                    this.client.setScreen(new DashboardScreen());
                }
            })
            .dimensions(x, y, buttonWidth, buttonHeight)
            .build()
        );
    }
}
