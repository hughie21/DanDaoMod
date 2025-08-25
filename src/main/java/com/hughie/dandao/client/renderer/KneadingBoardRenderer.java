package com.hughie.dandao.client.renderer;

import com.hughie.dandao.client.model.geomodel.KneadingBoardModel;
import com.hughie.dandao.common.entity.KneadingBoardEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;


public class KneadingBoardRenderer extends GeoBlockRenderer<KneadingBoardEntity> {
    public KneadingBoardRenderer(BlockEntityRendererProvider.Context context) {
        super(new KneadingBoardModel());
    }

    @Override
    public void preRender(PoseStack poseStack, KneadingBoardEntity blockEntity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Direction facing = blockEntity.getFacing();
        float rotationY = switch (facing) {
            case EAST -> 90f;
            case SOUTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        };
        poseStack.pushPose();
        poseStack.translate (0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY));
        poseStack.translate (-0.5, 0, -0.5);
        super.preRender(poseStack, blockEntity, model, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay,
                red, green, blue, alpha);
        poseStack.popPose();
    }
}
