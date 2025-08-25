package com.hughie.dandao.common.block.custom;

import com.hughie.dandao.common.entity.ModEntities;
import com.hughie.dandao.common.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayer;

public class PutuanBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(
            1.0, 0.0, 1.0,
            15.0, 4.0, 15.0
    );

    public PutuanBlock(Properties properties) {
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
        if (level.isClientSide || player instanceof FakePlayer) {
            return InteractionResult.SUCCESS;
        }

        // 检查玩家是否已经在乘坐实体
        if (player.isPassenger()) {
            player.stopRiding();
            return InteractionResult.SUCCESS;
        }

        // 计算座位实体的位置
        double x = pos.getX() + 0.5;
        double y = pos.getY(); // 调整这个值来改变玩家坐下的高度
        double z = pos.getZ() + 0.5;

        Direction direction = Direction.fromYRot(player.getYRot());
        float yaw = direction.toYRot();

        SeatEntity seat = ModEntities.SEAT.get().create(level);
        if (seat != null) {
            seat.setPos(x, y, z);
            seat.setYRot(yaw);
            level.addFreshEntity(seat);

            // 让玩家乘坐座位实体
            player.startRiding(seat);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);

        // 当方块被破坏时，移除可能存在的座位实体
        if (!level.isClientSide) {
            level.getEntitiesOfClass(SeatEntity.class,
                    new net.minecraft.world.phys.AABB(pos)).forEach(seat -> {
                seat.ejectPassengers();
                seat.discard();
            });
        }
    }
}
