package com.hughie.dandao.api;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public interface IncenseSubscriptionCapability {
    // 获取当前订阅的香炉位置（null表示无订阅）
    BlockPos getSubscribedPos();

    // 设置当前订阅的香炉位置
    void setSubscribedPos(BlockPos pos);

    // 清除当前订阅
    void clearSubscription();

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag nbt);
}
