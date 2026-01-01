package com.tcosmatic.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tcosmatic.util.CapeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class PlayerModelRenderer {
    
    private final MinecraftClient client;
    
    public PlayerModelRenderer(MinecraftClient client) {
        this.client = client;
    }
    
    public void renderPlayerWithCape(DrawContext context, int x, int y, int size, 
                                    float mouseX, float mouseY, 
                                    AbstractClientPlayerEntity player,
                                    CapeManager.CapeData cape) {
        if (player == null) return;
        
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        
        // Position and scale
        matrices.translate(x, y, 50);
        matrices.scale(size, size, -size);
        
        // Rotate based on mouse position
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
        
        Quaternionf rotation = RotationAxis.POSITIVE_X.rotationDegrees(mouseY * 0.2F);
        matrices.multiply(rotation);
        
        Quaternionf yRotation = RotationAxis.POSITIVE_Y.rotationDegrees(mouseX * 0.2F);
        matrices.multiply(yRotation);
        
        // Lighting setup
        RenderSystem.setShaderLights(
            new org.joml.Vector3f(0.2F, 1.0F, -1.0F),
            new org.joml.Vector3f(-0.2F, 1.0F, 0.0F)
        );
        
        // Render player entity
        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
        
        // Render the player
        dispatcher.render(
            player,
            0.0,
            0.0,
            0.0,
            0.0F,
            1.0F,
            matrices,
            immediate,
            15728880
        );
        
        immediate.draw();
        dispatcher.setRenderShadows(true);
        
        matrices.pop();
        
        // Reset lighting
        RenderSystem.setShaderLights(
            DiffuseLighting.NETHER_DIRECTION,
            DiffuseLighting.NETHER_DIRECTION
        );
    }
    
    public void renderEntity(DrawContext context, int x, int y, int size, 
                            float mouseX, float mouseY, LivingEntity entity) {
        if (entity == null) return;
        
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        
        matrices.translate(x, y, 50);
        matrices.scale(size, size, -size);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
        
        Quaternionf xRotation = RotationAxis.POSITIVE_X.rotationDegrees(mouseY * 0.3F);
        matrices.multiply(xRotation);
        
        Quaternionf yRotation = RotationAxis.POSITIVE_Y.rotationDegrees(mouseX * 0.3F);
        matrices.multiply(yRotation);
        
        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
        
        dispatcher.render(
            entity,
            0.0,
            0.0,
            0.0,
            0.0F,
            1.0F,
            matrices,
            immediate,
            15728880
        );
        
        immediate.draw();
        dispatcher.setRenderShadows(true);
        
        matrices.pop();
    }
}
