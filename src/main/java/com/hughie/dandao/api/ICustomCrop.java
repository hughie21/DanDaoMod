package com.hughie.dandao.api;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface ICustomCrop {
    IntegerProperty getAgeProperty();
    int getMaxAge();
}
