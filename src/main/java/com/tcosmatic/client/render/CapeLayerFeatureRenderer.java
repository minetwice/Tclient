package com.tcosmatic.client.render;

import com.tcosmatic.client.TCosmaticClient;
import com.tcosmatic.util.CapeManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.MathHelper;

public class CapeLayerFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public CapeLayerFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       AbstractClientPlayerEntity player, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {

        CapeManager.CapeData cape = TCosmaticClient.getCapeManager().getSelectedCape();
        if (cape == null || player.isInvisible()) return;

        Identifier capeTexture = cape.getTextureId();
        if (capeTexture == null) return;

        matrices.push();
        matrices.translate(0.0, 0.0, 0.125);
        matrices.scale(cape.getScale(), cape.getScale(), cape.getScale());
        matrices.translate(cape.getOffsetX(), cape.getOffsetY(), cape.getOffsetZ());

        if (cape.getRotationX() != 0) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cape.getRotationX()));
        if (cape.getRotationY() != 0) matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cape.getRotationY()));
        if (cape.getRotationZ() != 0) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(cape.getRotationZ()));

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(capeTexture));
        float opacity = TCosmaticClient.getCapeManager().getConfig().getCapeOpacity();

        renderCape(matrices, vertexConsumer, light, opacity);
        matrices.pop();
    }

    private void renderCape(MatrixStack matrices, VertexConsumer vertices, int light, float opacity) {
        float width = 10.0F / 16.0F;
        float height = 16.0F / 16.0F;

        // Front face
        vertex(matrices, vertices, -width, 0, -0.0625F, 0, 0, light, opacity);
        vertex(matrices, vertices, width, 0, -0.0625F, 1, 0, light, opacity);
        vertex(matrices, vertices, width, -height, -0.0625F, 1, 1, light, opacity);
        vertex(matrices, vertices, -width, -height, -0.0625F, 0, 1, light, opacity);

        // Back face
        vertex(matrices, vertices, -width, -height, 0.0625F, 0, 1, light, opacity);
        vertex(matrices, vertices, width, -height, 0.0625F, 1, 1, light, opacity);
        vertex(matrices, vertices, width, 0, 0.0625F, 1, 0, light, opacity);
        vertex(matrices, vertices, -width, 0, 0.0625F, 0, 0, light, opacity);
    }

    private void vertex(MatrixStack matrices, VertexConsumer vertices, float x, float y, float z,
                        float u, float v, int light, float opacity) {
        vertices.vertex(matrices.peek().getPositionMatrix(), x, y, z)
                .color(1.0F, 1.0F, 1.0F, opacity)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(matrices.peek().getNormalMatrix(), 0, 1, 0)
                .next();
    }
}
