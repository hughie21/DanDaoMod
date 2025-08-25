package com.hughie.dandao.client.model.geomodel;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.entity.IronMillBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class IronMillModel extends GeoModel<IronMillBlockEntity> {
    @Override
    public ResourceLocation getModelResource(IronMillBlockEntity ironMillBlock) {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "geo/block/iron_mill.json");
    }

    @Override
    public ResourceLocation getTextureResource(IronMillBlockEntity ironMillBlock) {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "textures/block/iron_mill.png");
    }

    @Override
    public ResourceLocation getAnimationResource(IronMillBlockEntity ironMillBlock) {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "animations/iron_mill.animation.json");
    }
}
