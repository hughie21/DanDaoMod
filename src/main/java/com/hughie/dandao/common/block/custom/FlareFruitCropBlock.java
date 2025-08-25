package com.hughie.dandao.common.block.custom;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.api.ICustomCrop;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.Nullable;

public class FlareFruitCropBlock extends CropBlock implements ICustomCrop {
    public static final int FIRST_STAGE_MAX_AGE = 7;
    public static final int SECOND_STAGE_MAX_AGE = 1;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public static final IntegerProperty AGE = IntegerProperty.create("age",0, 8);

    public FlareFruitCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide) {
            int currentAge = this.getAge(state);

            if (currentAge == this.getMaxAge()) {
                BlockPos belowPos = pos.below(1);
                BlockState belowState = level.getBlockState(belowPos);

                if (belowState.getBlock() == this && belowState.getValue(AGE) == FIRST_STAGE_MAX_AGE) {
                    popResource(level, pos, new ItemStack(ModItems.FLARE_FRUIT.get(), 3));
                    level.destroyBlock(pos, false);

                    level.setBlock(belowPos, this.getStateForAge(2), 2);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, level, pos, player, hand, result);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        if(!level.isClientSide()) {
            int currentAge = this.getAge(state);

            if(currentAge == this.getMaxAge()) {
                BlockPos belowPos = pos.below();
                BlockState belowState = level.getBlockState(belowPos);

                if (belowState.getBlock() == this && belowState.getValue(AGE) == FIRST_STAGE_MAX_AGE) {
                    // 将下方作物回退到age=2
                    level.setBlock(belowPos, this.getStateForAge(2), 2);
                }
            }
        }

        super.playerDestroy(level, player, pos, state, entity, stack);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter p_52303_, BlockPos p_52304_) {
        return state.is(Blocks.NETHERRACK);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return;
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int currentAge = this.getAge(pState);

            if (currentAge < this.getMaxAge()) {
                float growthSpeed = getGrowthSpeed(this, pLevel, pPos);

                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int)(25.0F / growthSpeed) + 1) == 0)) {
//                    if(currentAge == FIRST_STAGE_MAX_AGE) {
//                        if(pLevel.getBlockState(pPos.above(1)).is(Blocks.AIR)) {
//                            pLevel.setBlock(pPos.above(1), this.getStateForAge(currentAge + 1), 2);
//                        }
//                    } else {
//                        pLevel.setBlock(pPos, this.getStateForAge(currentAge + 1), 2);
//                    }
                    int nextAge = currentAge + 1;
                    if (currentAge == FIRST_STAGE_MAX_AGE - 1) {
                        pLevel.setBlock(pPos, this.getStateForAge(FIRST_STAGE_MAX_AGE), 2);
                        if (pLevel.getBlockState(pPos.above(1)).isAir()) {
                            pLevel.setBlock(pPos.above(1), this.getStateForAge(FIRST_STAGE_MAX_AGE + 1), 2);
                        }
                    } else if(currentAge == FIRST_STAGE_MAX_AGE) {
                        if (pLevel.getBlockState(pPos.above(1)).isAir()) {
                            pLevel.setBlock(pPos.above(1), this.getStateForAge(nextAge), 2);
                        }
                    }else {
                        pLevel.setBlock(pPos, this.getStateForAge(nextAge), 2);
                    }

                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return this.mayPlaceOn(state, world, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockState belowState = levelReader.getBlockState(pos.below());
        return belowState.is(Blocks.NETHERRACK) || (levelReader.getBlockState(pos.below(1)).is(this) &&
                levelReader.getBlockState(pos.below(1)).getValue(AGE) == FIRST_STAGE_MAX_AGE);
    }

    @Override
    public VoxelShape getShape(BlockState p_52297_, BlockGetter p_52298_, BlockPos p_52299_, CollisionContext p_52300_) {
        return SHAPE_BY_AGE[this.getAge(p_52297_)];
    }

    @Override
    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        int currentAge = this.getAge(pState);
        int nextAge = currentAge + this.getBonemealAgeIncrease(pLevel);
        int maxAge = this.getMaxAge();
        if(nextAge > maxAge) {
            nextAge = maxAge;
        }

//        if(this.getAge(pState) == FIRST_STAGE_MAX_AGE && pLevel.getBlockState(pPos.above(1)).is(Blocks.AIR)) {
//            pLevel.setBlock(pPos.above(1), this.getStateForAge(nextAge), 2);
//        } else {
//            pLevel.setBlock(pPos, this.getStateForAge(nextAge - SECOND_STAGE_MAX_AGE), 2);
//        }
        if (currentAge == FIRST_STAGE_MAX_AGE) {
            if (pLevel.getBlockState(pPos.above(1)).isAir()) {
                pLevel.setBlock(pPos.above(1), this.getStateForAge(nextAge), 2);
            }
        } else if (nextAge >= FIRST_STAGE_MAX_AGE) {
            pLevel.setBlock(pPos, this.getStateForAge(FIRST_STAGE_MAX_AGE), 2);
            if (pLevel.getBlockState(pPos.above(1)).isAir()) {
                pLevel.setBlock(pPos.above(1), this.getStateForAge(FIRST_STAGE_MAX_AGE + 1), 2);
            }
        } else {
            pLevel.setBlock(pPos, this.getStateForAge(nextAge), 2);
        }
    }

    @Override
    public int getMaxAge() {
        return FIRST_STAGE_MAX_AGE + SECOND_STAGE_MAX_AGE;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.FLARE_FRUIT_SEEDS.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
