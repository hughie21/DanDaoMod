package com.hughie.dandao.api;

import net.minecraft.core.BlockPos;

public interface IncenseStateChangeListener {
    void onIncenseStateChanged(BlockPos pos, int remainingCount, int burningStage);
}
