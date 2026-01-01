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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class CapeLayerFeatureRenderer
        extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public CapeLayerFeatureRenderer(
            FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> ctx) {
        super(ctx);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider provider, int light,
                       AbstractClientPlayerEntity player,
                       float limbAngle, float limbDistance,
                       float tickDelta, float animProgress,
                       float headYaw, float headPitch) {

        CapeManager.CapeData cape = TCosmaticClient.getCapeManager().getSelectedCape();
        if (cape == null || player.isInvisible()) return;

        Identifier tex = cape.getTextureId();
        if (tex == null) return;

        matrices.push();
        matrices.translate(0.0, 0.0, 0.125);
        matrices.scale(cape.getScale(), cape.getScale(), cape.getScale());
        matrices.translate(cape.getOffsetX(), cape.getOffsetY(), cape.getOffsetZ());

        if (TCosmaticClient.getCapeManager().getConfig().isCapePhysics()) {
            float move = MathHelper.clamp(player.horizontalSpeed * 100F, 0F, 90F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(move));
        }

        VertexConsumer vc = provider.getBuffer(RenderLayer.getEntityCutoutNoCull(tex));
        getContextModel().render(matrices, vc, light, 0xF000F0);
        matrices.pop();
    }
        }
