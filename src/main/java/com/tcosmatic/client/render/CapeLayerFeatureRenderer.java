package com.tcosmatic.client.render;

import com.tcosmatic.client.TCosmaticClient;
import com.tcosmatic.util.CapeManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class CapeLayerFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public CapeLayerFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context,
                                    EntityModelLoader loader) {
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

        double deltaX = player.getX() - player.prevX;
        double deltaY = player.getY() - player.prevY;
        double deltaZ = player.getZ() - player.prevZ;
        double velocity = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        float rotation = (float) (player.prevBodyYaw + (player.bodyYaw - player.prevBodyYaw) * tickDelta -
                (player.prevYaw + (player.getYaw() - player.prevYaw) * tickDelta));

        if (TCosmaticClient.getCapeManager().getConfig().isCapePhysics()) {
            float capeAngle = 6.0F + (float) velocity * 100.0F;
            capeAngle = MathHelper.clamp(capeAngle, 0.0F, 90.0F);
            if (!player.isOnGround()) capeAngle = 90.0F;
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(capeAngle));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation * 0.5F));
        }

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(capeTexture));
        renderCape(matrices, vertexConsumer, light, TCosmaticClient.getCapeManager().getConfig().getCapeOpacity());
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
                .overlay(0)
                .light(light)
                .normal(matrices.peek().getNormalMatrix(), 0.0F, 1.0F, 0.0F)
                .next();
    }
}
