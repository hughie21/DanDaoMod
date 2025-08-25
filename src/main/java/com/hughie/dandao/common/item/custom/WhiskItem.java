package com.hughie.dandao.common.item.custom;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.api.IMultiBlock;
import com.hughie.dandao.api.IncenseStateChangeListener;
import com.hughie.dandao.api.IncenseSubscriptionCapability;
import com.hughie.dandao.client.network.AlchemyFurnaceDataPacket;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.block.custom.AlchemyFurnaceBlock;
import com.hughie.dandao.common.block.custom.IncenseBurnerBlock;
import com.hughie.dandao.common.block.mutiblock.MutiBlockManager;
import com.hughie.dandao.common.entity.IncenseBurnerBlockEntity;
import com.hughie.dandao.setup.registry.CommonRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Map;

public class WhiskItem extends Item {
    public WhiskItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Direction direction = context.getClickedFace();
        Map<String, IMultiBlock> multiBlocks = MutiBlockManager.getMutiBlockList();
        IMultiBlock alchemyFurnace = multiBlocks.get("alchemy furnace");
//  这段有奇怪的bug，普通右键没反应，但shift右键却有反应，改为在方块处判断
//        if (blockState.is(ModBlocks.INCENSE_BURNER.get())) {
//            if (player != null && !world.isClientSide()) {
//                DanDao.LOGGER.debug("右键点击");
//                BlockEntity entity = world.getBlockEntity(pos);
//                if (entity instanceof IncenseBurnerBlockEntity newIncenseEntity) {
//                    DanDao.LOGGER.debug("右键点击");
//                    LazyOptional<IncenseSubscriptionCapability> cap = CommonRegistry.getSubscriptionCapability(player);
//                    cap.ifPresent(subscription -> {
//                        BlockPos oldPos = subscription.getSubscribedPos();
//                        DanDao.LOGGER.debug("订阅香炉事件");
//                        // 如果已订阅其他香炉，取消旧订阅
//                        if (oldPos != null && !oldPos.equals(pos)) {
//                            BlockEntity oldEntity = world.getBlockEntity(oldPos);
//                            if (oldEntity instanceof IncenseBurnerBlockEntity oldIncenseEntity) {
//                                oldIncenseEntity.removeListenerByPlayer(player);
//                            }
//                        }
//
//                        IncenseBurnerBlockEntity.PlayerIncenseListener newListener =
//                                new IncenseBurnerBlockEntity.PlayerIncenseListener(player);
//
//                        newIncenseEntity.addListener(newListener);
//                        subscription.setSubscribedPos(pos);
//                        player.sendSystemMessage(Component.translatable("message.dandao.subscribe_incense_burner", pos.getX(), pos.getY(), pos.getZ()));
//                    });
//
//                    if (!cap.isPresent()) {
//                        DanDao.LOGGER.error("玩家未获取到 IncenseSubscriptionCapability");
//                    }
//                }
//                return  InteractionResult.SUCCESS;
//            }
//        }
        if(alchemyFurnace.isCoreBlock(blockState)) {
            if(world instanceof ServerLevel serverLevel) {
                DanDao.LOGGER.debug("[alchemy furnace muti] 开始创建多方块结构");
                BlockPos posStart = pos.offset(alchemyFurnace.getCenterPos(direction));
                DanDao.LOGGER.debug("[alchemy furnace muti] 起始方块" + world.getBlockState(posStart));

                if(world.getBlockState(posStart).getValue(BlockStateProperties.HORIZONTAL_FACING) != direction) {
                    DanDao.LOGGER.debug("[alchemy furnace muti] 反应炉的方向不对");
                    return InteractionResult.FAIL;
                }

                if (!alchemyFurnace.isMatch(world, posStart, direction, null)) {
                    DanDao.LOGGER.debug("[alchemy furnace muti] 结构不匹配");
                    return InteractionResult.SUCCESS;
                }
                alchemyFurnace.build(world, posStart, direction, null);
                serverLevel.playSound(null, pos, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 1.5f, 1);
                return InteractionResult.SUCCESS;
            }
        }else if (blockState.getBlock() instanceof AlchemyFurnaceBlock && player.isShiftKeyDown()) {
            if (world.isClientSide) {
                DanDao.NETWORK.sendToServer(new AlchemyFurnaceDataPacket(pos));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
