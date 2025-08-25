package com.hughie.dandao.common.block.custom;

import com.hughie.dandao.api.IMultiBlock;
import com.hughie.dandao.common.block.mutiblock.MutiBlockManager;
import com.hughie.dandao.common.entity.AlchemyFurnaceBlockEntity;
import com.hughie.dandao.common.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AlchemyFurnaceBlock extends BaseEntityBlock implements EntityBlock {
    public AlchemyFurnaceBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(0.5f,0.5f).noOcclusion().noLootTable());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyFurnaceBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitCount) {
        if(!world.isClientSide()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if(entity instanceof AlchemyFurnaceBlockEntity alchemyFurnaceBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, alchemyFurnaceBlockEntity, pos);
            }else {
                throw new IllegalStateException("Missing provider");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        Map<String, IMultiBlock> multiBlocks = MutiBlockManager.getMutiBlockList();
        IMultiBlock alchemyFurnace = multiBlocks.get("alchemy furnace");
        alchemyFurnace.reset(world, pos);
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof  AlchemyFurnaceBlockEntity alchemyFurnaceBlockEntity) {
           alchemyFurnaceBlockEntity.drops();
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return createTickerHelper(blockEntityType, ModEntities.ALCHEMY_FURNACE.get(), AlchemyFurnaceBlockEntity::clientTick);
        }
        return createTickerHelper(blockEntityType, ModEntities.ALCHEMY_FURNACE.get(), (level1, pos, state1, blockEntity) -> blockEntity.serverTick(level1, pos, state1));
    }
}
