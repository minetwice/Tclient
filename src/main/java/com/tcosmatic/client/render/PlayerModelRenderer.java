package com.tcosmatic.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class PlayerModelRenderer {

    private final MinecraftClient client = MinecraftClient.getInstance();

    public void renderPlayer(DrawContext context, int x, int y, int size,
                             float mouseX, float mouseY,
                             AbstractClientPlayerEntity player) {

        if (player == null) return;

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        matrices.translate(x, y, 100);
        matrices.scale(size, size, size);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(mouseX * 0.3F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mouseY * 0.3F));

        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);

        VertexConsumerProvider.Immediate buffers =
                client.getBufferBuilders().getEntityVertexConsumers();

        dispatcher.render(player, 0, 0, 0, 0, 1,
                matrices, buffers, 15728880);

        buffers.draw();
        dispatcher.setRenderShadows(true);

        matrices.pop();
    }
}
