package com.hughie.dandao.common.block.custom;

import com.hughie.dandao.common.entity.BambooSieveVirtualBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/*
* 该类是用于加载BambooSieveVirtualBlockEntity的空白方块
 */
public class BambooSieveVirtualBlock extends BaseEntityBlock {
    public BambooSieveVirtualBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BambooSieveVirtualBlockEntity();
    }
}
