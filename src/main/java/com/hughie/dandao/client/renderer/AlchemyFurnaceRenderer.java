package com.hughie.dandao.client.renderer;

import com.github.tartaricacid.simplebedrockmodel.client.manager.BedrockEntityModelRegister;
import com.hughie.dandao.DanDao;
import com.hughie.dandao.client.model.bedrock.SimpleBedrockModel;
import com.hughie.dandao.common.entity.AlchemyFurnaceBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class AlchemyFurnaceRenderer implements BlockEntityRenderer<AlchemyFurnaceBlockEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "textures/bedrock/block/alchemy_furnace.png");
    private final SimpleBedrockModel<? extends Entity> model;

    private static final float MODEL_HEIGHT = 8.0f;
    private static final float PIXEL_TO_BLOCK = 1/16.0f;

    public AlchemyFurnaceRenderer(BlockEntityRendererProvider.Context context) {
        this.model = (SimpleBedrockModel<? extends Entity>) BedrockEntityModelRegister.INSTANCE.getModel(ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "bedrock/block/alchemy_furnace"));
    }

    @Override
    public void render(AlchemyFurnaceBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(entity.isRender()) {
            poseStack.pushPose();
            setTranslateAndPose(entity, poseStack);
            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
            model.renderToBuffer(poseStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(AlchemyFurnaceBlockEntity p_112306_) {
        return true;
    }


    private void setTranslateAndPose(AlchemyFurnaceBlockEntity entity, PoseStack poseStack) {
        // 使模型底部贴地
        float baseY = MODEL_HEIGHT * PIXEL_TO_BLOCK;

        // 根据朝向调整位置和旋转
        switch (entity.getDirection()) {
            case NORTH:
                poseStack.translate(0.5f, baseY, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case EAST:
                poseStack.translate(0.5f, baseY, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case WEST:
                poseStack.translate(0.5f, baseY, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(270));
                break;
            case SOUTH:
            default:
                poseStack.translate(0.5f, baseY, 0.5f);
        }
    }
}
