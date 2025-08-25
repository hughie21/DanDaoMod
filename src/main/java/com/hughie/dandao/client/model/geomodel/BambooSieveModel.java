package com.hughie.dandao.client.model.geomodel;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.item.custom.BambooSieveItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BambooSieveModel extends GeoModel<BambooSieveItem> {
    @Override
    public ResourceLocation getModelResource(BambooSieveItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "geo/item/bamboo_sieve.json");
    }

    @Override
    public ResourceLocation getTextureResource(BambooSieveItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "textures/item/bamboo_sieve.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BambooSieveItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "animations/bamboo_sieve.animation.json");
    }
}
