package com.hughie.dandao.client.model.geomodel;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.entity.KneadingBoardEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KneadingBoardModel extends GeoModel<KneadingBoardEntity> {
    @Override
    public ResourceLocation getModelResource(KneadingBoardEntity kneadingBoardEntity) {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "geo/block/kneading_board.json");
    }

    @Override
    public ResourceLocation getTextureResource(KneadingBoardEntity kneadingBoardEntity) {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "textures/block/kneading_board.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KneadingBoardEntity kneadingBoardEntity) {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "animations/kneading_board.animation.json");
    }
}
