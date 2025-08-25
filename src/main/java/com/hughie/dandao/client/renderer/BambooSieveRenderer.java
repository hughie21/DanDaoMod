package com.hughie.dandao.client.renderer;

import com.hughie.dandao.client.model.geomodel.BambooSieveModel;
import com.hughie.dandao.common.item.custom.BambooSieveItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BambooSieveRenderer extends GeoItemRenderer<BambooSieveItem> {
    public BambooSieveRenderer() {
        super(new BambooSieveModel());
    }
}
