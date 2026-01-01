package com.tcosmatic.client.render;

import com.tcosmatic.util.CapeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class PlayerModelRenderer {

    private final MinecraftClient client;

    public PlayerModelRenderer() { // 1.21.1 version uses no-arg constructor
        this.client = MinecraftClient.getInstance();
    }

    public void renderPlayerWithCape(DrawContext context, int x, int y, int size,
                                     float mouseX, float mouseY,
                                     AbstractClientPlayerEntity player,
                                     CapeManager.CapeData cape) {
        if (player == null) return;

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        matrices.translate(x, y, 50);
        matrices.scale(size, size, -size);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));

        Quaternionf xRot = RotationAxis.POSITIVE_X.rotationDegrees(mouseY * 0.2F);
        Quaternionf yRot = RotationAxis.POSITIVE_Y.rotationDegrees(mouseX * 0.2F);
        matrices.multiply(xRot);
        matrices.multiply(yRot);

        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);

        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
        dispatcher.render(player, 0, 0, 0, 0, 1, matrices, immediate, 15728880);
        immediate.draw();
        dispatcher.setRenderShadows(true);

        matrices.pop();
    }

    public void renderEntity(DrawContext context, int x, int y, int size,
                             float mouseX, float mouseY,
                             LivingEntity entity) {
        if (entity == null) return;

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(x, y, 50);
        matrices.scale(size, size, -size);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mouseY * 0.3F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(mouseX * 0.3F));

        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);

        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
        dispatcher.render(entity, 0, 0, 0, 0, 1, matrices, immediate, 15728880);
        immediate.draw();
        dispatcher.setRenderShadows(true);

        matrices.pop();
    }
}
