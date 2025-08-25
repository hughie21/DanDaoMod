package com.hughie.dandao.common.block.custom;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.api.IncenseSubscriptionCapability;
import com.hughie.dandao.common.entity.IncenseBurnerBlockEntity;
import com.hughie.dandao.common.entity.ModEntities;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.setup.registry.CommonRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class IncenseBurnerBlock extends BaseEntityBlock {
    public static final IntegerProperty COUNT = IntegerProperty.create("count", 0, 3);
    public static final IntegerProperty BURNING_STAGE = IntegerProperty.create("burning_stage", 0, 4);

    private static final VoxelShape SHAPE = Block.box(
            4, 0, 3, 12, 5, 12
    );

    public IncenseBurnerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(COUNT, 0).setValue(BURNING_STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COUNT, BURNING_STAGE);
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
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide() || hand == InteractionHand.MAIN_HAND) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof IncenseBurnerBlockEntity incenseBurnerBlockEntity) {
                ItemStack heldItem = player.getItemInHand(hand);
                if(heldItem.isEmpty() && player.isShiftKeyDown()) {
                    incenseBurnerBlockEntity.popIncense(level, state, pos);
                }else if(heldItem.is(ModItems.INCENSE_STICK.get())) {
                    incenseBurnerBlockEntity.setIncense(level, player, heldItem, state, pos);
                }else if (heldItem.is(Items.FLINT_AND_STEEL)) {
                    if (incenseBurnerBlockEntity.lighton(level, state, pos)) {
                        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1f, 1f);
                        heldItem.hurtAndBreak(1, player, p -> {
                            p.broadcastBreakEvent(hand);
                        });
                    };
                } else if (heldItem.is(ModItems.WHISK.get())) {
                    if(!level.isClientSide()) {
                        if (entity instanceof IncenseBurnerBlockEntity newIncenseEntity) {
                            LazyOptional<IncenseSubscriptionCapability> cap = CommonRegistry.getSubscriptionCapability(player);
                            cap.ifPresent(subscription -> {
                                BlockPos oldPos = subscription.getSubscribedPos();
                                DanDao.LOGGER.debug("订阅香炉事件");
                                // 如果已订阅其他香炉，取消旧订阅
                                if (oldPos != null && !oldPos.equals(pos)) {
                                    BlockEntity oldEntity = level.getBlockEntity(oldPos);
                                    if (oldEntity instanceof IncenseBurnerBlockEntity oldIncenseEntity) {
                                        oldIncenseEntity.removeListenerByPlayer(player);
                                    }
                                    DanDao.LOGGER.debug("已取消上一次的订阅");
                                }

                                IncenseBurnerBlockEntity.PlayerIncenseListener newListener =
                                        new IncenseBurnerBlockEntity.PlayerIncenseListener(player);

                                newIncenseEntity.addListener(newListener);
                                subscription.setSubscribedPos(pos);
                                player.sendSystemMessage(Component.translatable("message.dandao.subscribe_incense_burner", pos.getX(), pos.getY(), pos.getZ()));
                            });

                            if (!cap.isPresent()) {
                                DanDao.LOGGER.error("玩家未获取到 IncenseSubscriptionCapability");
                            }
                        }
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.sidedSuccess(!level.isClientSide());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!level.isClientSide() && !(newState.getBlock() instanceof IncenseBurnerBlock)) {
            int count = state.getValue(COUNT);
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof IncenseBurnerBlockEntity incenseBurnerBlockEntity) {
                if(incenseBurnerBlockEntity.isBurning()) {
                    int newCount = Math.max(count - 1, 0);
                    Block.popResource(level, pos, new ItemStack(ModItems.INCENSE_STICK.get(), newCount));
                } else {
                    Block.popResource(level, pos, new ItemStack(ModItems.INCENSE_STICK.get(), count));
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IncenseBurnerBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        if (level.isClientSide()) {
            return createTickerHelper(entityType, ModEntities.INCENSE_BURNER.get(), IncenseBurnerBlockEntity::clientTick);
        }

        return createTickerHelper(entityType, ModEntities.INCENSE_BURNER.get(), IncenseBurnerBlockEntity::serverTick);
    }
}
