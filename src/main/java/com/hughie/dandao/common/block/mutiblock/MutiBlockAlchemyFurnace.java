package com.hughie.dandao.common.block.mutiblock;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.api.IMultiBlock;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.block.custom.ReactorBlock;
import com.hughie.dandao.common.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.List;

public class MutiBlockAlchemyFurnace implements IMultiBlock {
    private static final List<Block> REQUIRE_BLOCKS = List.of(ModBlocks.REACTOR.get(), Blocks.MAGMA_BLOCK, Blocks.IRON_BLOCK);
    /**
     * 触发的构建多方块结构时，点击的方块是否为核心方块
     *
     * @param blockState 点击的方块
     * @return 是否为核心方块
     */
    @Override
    public boolean isCoreBlock(BlockState blockState) {
        return blockState.is(ModBlocks.REACTOR.get());
    }

    /**
     * 触发的构建多方块结构时，朝向是否正确
     *
     * @param direction 朝向
     * @return 是否为合法的触发方向
     */
    @Override
    public boolean directionIsSuitable(Direction direction) {
        return direction.getAxis().isHorizontal();
    }

    /**
     * 获取多方块结构的中心点
     *
     * @param direction 朝向
     * @return 多方块结构的中心点
     */
    @Override
    public BlockPos getCenterPos(Direction direction) {
        return BlockPos.ZERO;
    }

    /**
     * 获取多方块结构模板
     *
     * @param world     世界
     * @param direction 朝向
     * @return 多方块结构模板
     */
    @Override
    public StructureTemplate getTemplate(ServerLevel world, Direction direction) {
        return null;
    }

    /**
     * 是否匹配该多方块结构
     *
     * @param world     世界实例
     * @param posStart  起始坐标
     * @param direction 判定匹配时的朝向，用来应用到一些具有方向的多方块结构
     * @param template  结构模板
     * @return 是否匹配
     */
    @Override
    public boolean isMatch(Level world, BlockPos posStart, Direction direction, StructureTemplate template) {
        return checkRequireBlock(world, posStart, getTransferMatrix(direction));
    }

    /**
     * 修建多方块结构的逻辑
     *
     * @param worldIn   世界实例
     * @param posStart  起始坐标
     * @param direction 判定匹配时的朝向，用来应用到一些具有方向的多方块结构
     * @param template  结构模板
     */
    @Override
    public void build(Level worldIn, BlockPos posStart, Direction direction, StructureTemplate template) {
        // 仅在服务器端执行
        if (worldIn.isClientSide() || !(worldIn instanceof ServerLevel)) {
            return;
        }

        // 先检查结构是否匹配
        if (!isMatch(worldIn, posStart, direction, template)) {
            return; // 结构不匹配则不生成新方块
        }

        List<List<BlockPos>> matrix = getTransferMatrix(direction);

        // 结构匹配，清除原有结构方块
        clearOriginalStructure(worldIn, posStart, matrix);

        // 获取岩浆块位置作为新方块的生成位置（第二层中心）
        BlockPos magmaPos = posStart.offset(matrix.get(1).get(0));
        worldIn.setBlock(magmaPos, ModBlocks.ALCHEMY_FURNACE.get().defaultBlockState(), 3);

        worldIn.getBlockEntity(magmaPos, ModEntities.ALCHEMY_FURNACE.get()).ifPresent(alchemyFurnace -> {
                alchemyFurnace.render(true, direction);
                worldIn.sendBlockUpdated(magmaPos, worldIn.getBlockState(magmaPos), worldIn.getBlockState(magmaPos), 3);
        });
    }

    private void clearOriginalStructure(Level world, BlockPos posStart, List<List<BlockPos>> matrix) {
        for(int layer = 0; layer < 3; layer++) {
            for(int i = 0; i < 9; i++) {
                BlockPos pos = posStart.offset(matrix.get(layer).get(i));
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    public void reset(Level level, BlockPos pos) {
        level.getBlockEntity(pos, ModEntities.ALCHEMY_FURNACE.get()).ifPresent(alchemyFurnace -> {
            Direction direction = alchemyFurnace.getDirection();
            List<List<BlockPos>> matrix = getTransferMatrix(direction);
            if(level instanceof ServerLevel server) {
                for(int layer = 0; layer < 3; layer++) {
                    BlockPos centerPos = getReactorPos(direction, pos);
                    if(layer == 0) {
                        server.setBlock(centerPos.offset(matrix.get(layer).get(0)),REQUIRE_BLOCKS.get(layer).defaultBlockState().setValue(ReactorBlock.FACING, direction),3);
                    } else {
                        server.setBlock(centerPos.offset(matrix.get(layer).get(0)),REQUIRE_BLOCKS.get(layer).defaultBlockState(),3);
                    }
                    for(int i = 1; i < 9; i++) {
                        server.setBlock(centerPos.offset(matrix.get(layer).get(i)), REQUIRE_BLOCKS.get(2).defaultBlockState(), 3);
                    }
                }
            }
        });
    }

    private BlockPos getReactorPos(Direction direction, BlockPos magaPos) {
        switch (direction) {
            case EAST:
                return magaPos.offset(1,0,0);
            case WEST:
                return magaPos.offset(-1,0,0);
            case NORTH:
                return magaPos.offset(0,0,-1);
            case SOUTH:
                return magaPos.offset(0,0,1);
            default:
                return magaPos;
        }
    }

    /**
     * 根据方向获取转换矩阵
     *
     * @param direction 方块朝向
     *
     * @return 转换矩阵 [中心，上，下，左，右，左上，右上，右下，左下】
     * */
    private List<List<BlockPos>> getTransferMatrix(Direction direction) {
        List<List<BlockPos>> returnList = new ArrayList<>();
        switch (direction) {
            case EAST:
                for(int layer = 0; layer < 3; layer++) {
                    List<BlockPos> layer_matrix = List.of(
                            new BlockPos(-1*layer,0,0),
                            new BlockPos(-1*layer,1,0),
                            new BlockPos(-1*layer,-1,0),
                            new BlockPos(-1*layer,0,1),
                            new BlockPos(-1*layer,0,-1),
                            new BlockPos(-1*layer, 1,1),
                            new BlockPos(-1*layer, 1,-1),
                            new BlockPos(-1*layer, -1,-1),
                            new BlockPos(-1*layer, -1,1)
                    );
                    returnList.add(layer_matrix);
                }
                break;
            case WEST:
                for(int layer = 0; layer < 3; layer++) {
                    List<BlockPos> layer_matrix = List.of(
                            new BlockPos(layer,0,0),
                            new BlockPos(layer,1,0),
                            new BlockPos(layer,-1,0),
                            new BlockPos(layer,0,-1),
                            new BlockPos(layer,0,1),
                            new BlockPos(layer, 1,-1),
                            new BlockPos(layer, 1,1),
                            new BlockPos(layer, -1,1),
                            new BlockPos(layer, -1,-1)
                    );
                    returnList.add(layer_matrix);
                }
                break;
            case NORTH:
                for(int layer = 0; layer < 3; layer++) {
                    List<BlockPos> layer_matrix = List.of(
                            new BlockPos(0,0,layer),
                            new BlockPos(0,1,layer),
                            new BlockPos(0,-1,layer),
                            new BlockPos(1,0,layer),
                            new BlockPos(-1,0,layer),
                            new BlockPos(1,1,layer),
                            new BlockPos(-1,1,layer),
                            new BlockPos(-1,-1,layer),
                            new BlockPos(1,-1,layer)
                    );
                    returnList.add(layer_matrix);
                }
                break;
            case SOUTH:
                for(int layer = 0; layer < 3; layer++) {
                    List<BlockPos> layer_matrix = List.of(
                            new BlockPos(0,0,-1*layer),
                            new BlockPos(0,1,-1*layer),
                            new BlockPos(0,-1,-1*layer),
                            new BlockPos(-1,0,-1*layer),
                            new BlockPos(1,0,-1*layer),
                            new BlockPos(-1,1,-1*layer),
                            new BlockPos(1,1,-1*layer),
                            new BlockPos(1,-1,-1*layer),
                            new BlockPos(-1,-1,-1*layer)
                    );
                    returnList.add(layer_matrix);
                }
                break;
            default:
                break;
        }

        return returnList;
    }


    /**
     * 根据坐标获取对应位置需要的方块
     * @param matrix 位置矩阵
     */
    private boolean checkRequireBlock(Level world, BlockPos posStart, List<List<BlockPos>> matrix) {
        for(int layer = 0; layer < 3; layer++) {
            List<BlockPos> layer_matrix = matrix.get(layer);
            BlockPos centerBlockPos = posStart.offset(layer_matrix.get(0));
            BlockState centerBlock = world.getBlockState(centerBlockPos);
            if(centerBlock.is(REQUIRE_BLOCKS.get(layer))) {
                if(!checkSurrounding(world, posStart, layer_matrix)) return false;
            } else {
                DanDao.LOGGER.debug("[alchemy furnace muti] 中心方块不匹配， pos: " + centerBlockPos + " 需要：" + REQUIRE_BLOCKS.get(layer) + " 实际：" + centerBlock.getBlock());
                return false;
            }
        }
        return true;
    }

    private boolean checkSurrounding(Level world, BlockPos posStart, List<BlockPos> layer_matrix) {
        for(int pos = 1; pos < 9; pos++) {
            BlockPos offsetPos = posStart.offset(layer_matrix.get(pos));
            BlockState surroundingBlock = world.getBlockState(offsetPos);
            if(!surroundingBlock.is(REQUIRE_BLOCKS.get(2))) {
                DanDao.LOGGER.debug("[alchemy furnace muti] 检测到非铁块方块：" + surroundingBlock + " 在" + offsetPos);
                return false;
            }
        }
        return true;
    }
}
