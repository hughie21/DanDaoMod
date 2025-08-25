package com.hughie.dandao.common.block.custom;

import com.hughie.dandao.common.entity.ModEntities;
import com.hughie.dandao.common.entity.MortarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MortarBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Shapes.or(
            // 底部基座（x:6-11, y:0-1, z:6-11）
            Block.box(6, 1, 6, 11, 2, 11),
            // 北边缘（x:6-11, y:1-4, z:5-6）
            Block.box(6, 2, 5, 11, 5, 6),
            // 西边缘（x:5-6, y:1-4, z:6-11）
            Block.box(5, 2, 6, 6, 5, 11),
            // 南边缘（x:6-11, y:1-4, z:11-12）
            Block.box(6, 2, 11, 11, 5, 12),
            // 东边缘（x:11-12, y:1-4, z:6-11）
            Block.box(11, 2, 6, 12, 5, 11)
    );

    public MortarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof MortarBlockEntity mortarBlock) {
                NetworkHooks.openScreen((ServerPlayer) player, mortarBlock, pos);
            }else {
                throw new IllegalStateException("Missing provider");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof MortarBlockEntity mortarBlock) {
                mortarBlock.drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MortarBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }
        return createTickerHelper(blockEntityType, ModEntities.MORTAR_ENT.get(), (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1));
    }
}
