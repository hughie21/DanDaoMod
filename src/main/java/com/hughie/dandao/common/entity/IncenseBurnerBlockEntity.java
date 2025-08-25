package com.hughie.dandao.common.entity;

import com.hughie.dandao.api.IncenseStateChangeListener;
import com.hughie.dandao.common.block.custom.IncenseBurnerBlock;
import com.hughie.dandao.common.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.*;

public class IncenseBurnerBlockEntity extends BlockEntity {
    private int burningIncenseIndex = 0; // 当前燃烧的香的索引
    private int burnTime = 0; // 燃烧时间
    private static final int BURN_DURATION = 30*20; // 每个燃烧阶段的持续时间（30s）
    private Boolean isBurning = false;

    // 存储订阅该香炉的玩家监听器
    private final Set<IncenseStateChangeListener> listeners = new HashSet<>();

    public IncenseBurnerBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.INCENSE_BURNER.get(), pos, state);
    }

    public void setIsBurning(boolean isBurning) {
        this.isBurning = isBurning;
    }

    public boolean isBurning() {
        return this.isBurning;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, IncenseBurnerBlockEntity be) {
        int count = state.getValue(IncenseBurnerBlock.COUNT);
        int currentBurnState = state.getValue(IncenseBurnerBlock.BURNING_STAGE);
        if (count == 0 || !be.isBurning || currentBurnState == 0) return;

        be.burningIncenseIndex = count;
        be.burnTime++;

        if (be.burnTime >= BURN_DURATION) {
            be.burnTime = 0;
            if (currentBurnState < 2) {
                level.setBlock(pos, state.setValue(IncenseBurnerBlock.BURNING_STAGE, currentBurnState + 1), 3);
            } else  {
                be.burningIncenseIndex--;
                if (be.burningIncenseIndex == 0) {
                    level.setBlock(pos, state.setValue(IncenseBurnerBlock.COUNT, 0)
                            .setValue(IncenseBurnerBlock.BURNING_STAGE, 0), 3);
                    be.burningIncenseIndex = 0;
                    be.setIsBurning(false);
                } else {
                    BlockState newState = state.setValue(IncenseBurnerBlock.BURNING_STAGE, 1).setValue(IncenseBurnerBlock.COUNT, be.burningIncenseIndex);
                    level.setBlock(pos, newState, 3);
                }
            }
            be.notifyListeners();
        }
        be.setChanged();
    }

    public void setIncense(Level level, Player player, ItemStack itemStack, BlockState state, BlockPos pos) {
        int count = state.getValue(IncenseBurnerBlock.COUNT);
        if (count == 3 || isBurning) {
            return;
        }
        if (!player.isCreative()) {
            itemStack.shrink(1);
        }
        level.setBlock(pos, state.setValue(IncenseBurnerBlock.COUNT, count + 1), 3);
    }

    public void popIncense(Level level, BlockState state, BlockPos pos) {
        int count = state.getValue(IncenseBurnerBlock.COUNT);
        if (count == 0 || isBurning) {
            return;
        }
        level.setBlock(pos, state.setValue(IncenseBurnerBlock.COUNT, count - 1), 3);
        Block.popResource(level, pos, new ItemStack(ModItems.INCENSE_STICK.get(), 1));
    }

    public boolean lighton(Level level, BlockState state, BlockPos pos) {
        int count = state.getValue(IncenseBurnerBlock.COUNT);
        if(count == 0 || isBurning) {
            return false;
        }
        isBurning = true;
        level.setBlock(pos, state.setValue(IncenseBurnerBlock.BURNING_STAGE, 1), 3);
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BurningIndex", burningIncenseIndex);
        tag.putInt("BurnTime", burnTime);
        tag.putBoolean("isBurning", isBurning);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        burningIncenseIndex = tag.getInt("BurningIndex");
        burnTime = tag.getInt("BurnTime");
        isBurning = tag.getBoolean("isBurning");
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, IncenseBurnerBlockEntity be) {
        int count = state.getValue(IncenseBurnerBlock.COUNT);
        int currentBurnState = state.getValue(IncenseBurnerBlock.BURNING_STAGE);
        // 客户端仅在燃烧状态下生成粒子
        if (count > 0 && be.isBurning && currentBurnState > 0) {
            be.spawnSmokeParticles(level, pos, state);
        }
    }

    public void spawnSmokeParticles(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) {

            int count = state.getValue(IncenseBurnerBlock.COUNT);
            int stage = state.getValue(IncenseBurnerBlock.BURNING_STAGE);

            DustParticleOptions smallSmoke = new DustParticleOptions(
                    new Vector3f(1.0F, 1.0F, 1.0F),
                    0.3F // 粒子大小（原版约0.2-0.3）
            );

            if (level.getGameTime() % 10 == 0) {
                double speedY = 20.0D + level.random.nextDouble() * 0.2D;

                if (count > 0) {
                    double[] positions = getIncensePositions(count, stage, pos);
                    level.addParticle(
                            smallSmoke,
                            positions[0] + level.random.nextDouble() * 0.1 - 0.05,
                            positions[1] + level.random.nextDouble() * 0.05,
                            positions[2] + level.random.nextDouble() * 0.1 - 0.05,
                            0.05D, speedY, 0.0D
                    );
                }
            }
        }
    }

    private double[] getIncensePositions(int count, int stage, BlockPos pos) {
        double x = pos.getX() + 0.5; // 方块中心X
        double y = pos.getY();
        double z = pos.getZ() + 0.5; // 方块中心Z

        if (count == 1 && stage == 1) {
            return new double[]{x, y + 0.7, z};
        }else if (count == 1 && stage == 2) {
            return new double[]{x, y + 0.55, z};
        }else if (count == 2 && stage == 1) {
            return new double[]{x, y + 0.7, z - 0.04};
        } else if (count == 2 && stage == 2) {
            return new double[]{x, y + 0.55, z - 0.04};
        } else if (count == 3 && stage == 1) {
            return new double[]{x, y + 0.65, z - 0.23};
        } else if (count == 3 && stage == 2) {
            return new double[]{x, y + 0.5, z - 0.17};
        }
        return new double[]{x, y + 0.7, z};
    }

    public void addListener(IncenseStateChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IncenseStateChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            if (state.hasProperty(IncenseBurnerBlock.COUNT)) {
                int remainingCount = state.getValue(IncenseBurnerBlock.COUNT);
                int burningStage = state.getValue(IncenseBurnerBlock.BURNING_STAGE);
                new ArrayList<>(listeners).forEach(listener ->
                        listener.onIncenseStateChanged(worldPosition, remainingCount, burningStage)
                );
            }
        }
    }

    public void removeListenerByPlayer(Player player) {
        listeners.removeIf(listener ->
                listener instanceof PlayerIncenseListener playerListener &&
                        playerListener.getPlayer().getUUID().equals(player.getUUID())
        );
    }

    public static class PlayerIncenseListener implements IncenseStateChangeListener {
        private final Player player;

        public PlayerIncenseListener(Player player) {
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }

        @Override
        public void onIncenseStateChanged(BlockPos pos, int remainingCount, int burningStage) {
            MutableComponent message;
            switch (remainingCount) {
                case 0:
                    message = Component.translatable("message.dandao.zero_incense");
                    break;
                case 1:
                    if (burningStage == 2) {
                        message = Component.translatable("message.dandao.half_incense");
                    } else if (burningStage == 1) {
                        message = Component.translatable("message.dandao.one_incense");
                    } else {
                        message = Component.translatable("message.dandao.one_incense_not_burn");
                    }
                    break;
                case 2:
                    if (burningStage == 2) {
                        message = Component.translatable("message.dandao.one_half_incense");
                    } else if (burningStage == 1) {
                        message = Component.translatable("message.dandao.two_incense");
                    } else {
                        message = Component.translatable("message.dandao.two_incense_not_burn");
                    }
                    break;
                case 3:
                    if (burningStage == 2) {
                        message = Component.translatable("message.dandao.two_half_incense");
                    } else if (burningStage == 1) {
                        message = Component.translatable("message.dandao.three_incense");
                    } else {
                        message = Component.translatable("message.dandao.three_incense_not_burn");
                    }
                    break;
                default:
                    message = Component.translatable("message.dandao.incnese_state_error");
            }

            // 发送消息
            player.sendSystemMessage(Component.translatable(
                    "message.dandao.incense_subsribe",
                    pos.getX(), pos.getY(), pos.getZ(), message
            ));
        }
    }
}
