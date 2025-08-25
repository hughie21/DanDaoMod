package com.hughie.dandao.common.entity;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.client.network.AnimationFinishedPacket;
import com.hughie.dandao.common.block.custom.KneadingBoardBlock;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.screen.KneadingBoardMenu;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.hughie.dandao.common.util.MedicinalPropertiesNBT;
import com.hughie.dandao.common.util.ModTags;
import com.hughie.dandao.common.util.PlayerUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class KneadingBoardEntity extends BlockEntity implements MenuProvider, GeoBlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4);
    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int SIDE_INPUT_SLOT = 2;
    private static final int PREVIEW_SLOT = 3;

    private long lastPlayTime = 0;
    private static final long DEBOUNCE_TIME = 10000;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final RawAnimation kneadingAnim = RawAnimation.begin().then("animation.kneading_board.make_medicine", Animation.LoopType.PLAY_ONCE);
    private final RawAnimation kneadingIdle = RawAnimation.begin().then("animation.kneading_board.idle", Animation.LoopType.LOOP);
    private static final String CONTROLLER_NAME = "kneading_board_controller";

//    private boolean shouldPlayKnead = false;
    private AnimationController.State lastFrameState = AnimationController.State.STOPPED;

    private Boolean canOpenMenu = true;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public KneadingBoardEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.KNEADING_BOARD.get(), pPos, pBlockState);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(3);
        inventory.setItem(INPUT_SLOT_1, itemHandler.getStackInSlot(INPUT_SLOT_1));
        inventory.setItem(INPUT_SLOT_2, itemHandler.getStackInSlot(INPUT_SLOT_2));
        inventory.setItem(SIDE_INPUT_SLOT, itemHandler.getStackInSlot(SIDE_INPUT_SLOT));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public Boolean canOpenMenu() {
        return this.canOpenMenu;
    }

    public Direction getFacing() {
        BlockState state = this.getBlockState ();
        if (state.getBlock() instanceof KneadingBoardBlock) {
            return state.getValue(KneadingBoardBlock.FACING);
        }
        return Direction.NORTH;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.dandao.kneading_board");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player p_39956_) {
        return new KneadingBoardMenu(containerId, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("kneading_board_inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("kneading_board_inventory"));
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(this.level == null) {
            this.setLevel(level);
        }
        if(isValidTags()) {
            showPreview();
            setChanged(level, pos, state);
        }else {
            resetPreview();
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putBoolean("CanOpenMenu", canOpenMenu);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        canOpenMenu = tag.getBoolean("CanOpenMenu");
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide) {
            // 标记方块状态已变化，触发数据同步
            canOpenMenu = false;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            setChanged();
        }
    }

    public void userClick(Player player, BlockPos pos) {
        if(isValidTags()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPlayTime >= DEBOUNCE_TIME) {
                lastPlayTime = currentTime;
                if (level instanceof ServerLevel) {
                    DanDao.LOGGER.debug("test");
                    triggerAnim(CONTROLLER_NAME, "kneading");
                }
            }
            syncToClient();
        }
    }

    private void resetPreview() {
        itemHandler.extractItem(PREVIEW_SLOT, 1, false);
    }

    private void showPreview() {
        ItemStack result = calculateResult(checkSupportEffect());
        itemHandler.setStackInSlot(PREVIEW_SLOT, result);
    }

    private int checkSupportEffect() {
        ItemStack sideInput = itemHandler.getStackInSlot(SIDE_INPUT_SLOT);
        int effect = 0;
        if (sideInput.is(Items.HONEY_BOTTLE)) {
            effect = 1;
        }else if (sideInput.is(Items.DRAGON_BREATH)) {
            effect = 2;
        }
        return effect;
    }

    private ItemStack calculateResult(int effect) {
        ItemStack inputStack1 = itemHandler.getStackInSlot(INPUT_SLOT_1);
        ItemStack inputStack2 = itemHandler.getStackInSlot(INPUT_SLOT_2);

        Pair<MedicinalProperties, Integer> medPro1 = MedicinalPropertiesNBT.loadProperties(inputStack1);
        Pair<MedicinalProperties, Integer> medPro2 = MedicinalPropertiesNBT.loadProperties(inputStack2);

        ItemStack result = new ItemStack(ModItems.DAN_EMBRYO.get());

        switch (effect) {
            case 1:
                if (medPro1.getSecond() > medPro2.getSecond()) {
                    MedicinalPropertiesNBT.setPropertyLevel(result, medPro1.getFirst(), medPro1.getSecond());
                }else {
                    MedicinalPropertiesNBT.setPropertyLevel(result, medPro2.getFirst(), medPro2.getSecond());
                }
                return result;
            case 2:
                if (medPro1.getSecond() > medPro2.getSecond()) {
                    int resultLevel = medPro1.getFirst().getMaxLevel();
                    MedicinalPropertiesNBT.setPropertyLevel(result, medPro1.getFirst(), resultLevel);
                }else {
                    int resultLevel = medPro2.getFirst().getMaxLevel();
                    MedicinalPropertiesNBT.setPropertyLevel(result, medPro2.getFirst(), resultLevel);
                }
                return result;
            case 0:
                if (medPro1.getSecond() > medPro2.getSecond()) {
                    int resultLevel = medPro1.getSecond() + medPro2.getFirst().getRelationship().get(medPro1.getFirst());
                    resultLevel = checkLevelBound(resultLevel, medPro1.getFirst());
                    MedicinalPropertiesNBT.setPropertyLevel(result, medPro1.getFirst(), resultLevel);
                }else {
                    int resultLevel = medPro2.getSecond() + medPro1.getFirst().getRelationship().get(medPro2.getFirst());
                    resultLevel = checkLevelBound(resultLevel, medPro2.getFirst());
                    MedicinalPropertiesNBT.setPropertyLevel(result, medPro2.getFirst(), resultLevel);
                }
                return result;
            default:
                return result;
        }
    }

    private int checkLevelBound(int level, MedicinalProperties medicinalProperty) {
        if (level > medicinalProperty.getMaxLevel()) {
            return medicinalProperty.getMaxLevel();
        }else if (level < medicinalProperty.getMinLevel()) {
            return medicinalProperty.getMinLevel();
        }

        return level;
    }

    private boolean isValidTags() {
        ItemStack input1 = itemHandler.getStackInSlot(INPUT_SLOT_1);
        ItemStack input2 = itemHandler.getStackInSlot(INPUT_SLOT_2);
        ItemStack side_input = itemHandler.getStackInSlot(SIDE_INPUT_SLOT);

        boolean isValidInput = input1.is(ModItems.MEDICINAL_POWDER.get()) && input2.is(ModItems.MEDICINAL_POWDER.get());

        boolean isValidSideInput = side_input.is(ModTags.SUPPORT_POTION);

        return isValidInput && isValidSideInput;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<KneadingBoardEntity> controller = new AnimationController<>(this, CONTROLLER_NAME, 0, state -> animate(state, this::onAnimationFinished));
        controller.triggerableAnim("kneading", kneadingAnim);
        controller.triggerableAnim("idle", kneadingIdle);
        controllerRegistrar.add(controller);
    }

    private void onAnimationFinished() {
        DanDao.LOGGER.debug("[客户端] 动画结束，发送网络包");
        Player player = Minecraft.getInstance().player;
        if (level != null && level.isClientSide) {
            DanDao.NETWORK.sendToServer(new AnimationFinishedPacket(worldPosition, player.getUUID()));
        }
    }

    private void giveResultToPlayer(Player player) {
        ItemStack itemStack = calculateResult(checkSupportEffect());
        for (int i = 0; i < 4; i++) {
            PlayerUtils.givePlayerItems(player, itemStack);
        }
    }

    public void serverSideFinish(Player player) {
        DanDao.LOGGER.debug("[服务端] 执行合成逻辑");
        if (level != null && !level.isClientSide) {
            giveResultToPlayer(player);
            itemHandler.extractItem(INPUT_SLOT_1, 1, false);
            itemHandler.extractItem(INPUT_SLOT_2, 1, false);
            itemHandler.extractItem(SIDE_INPUT_SLOT, 1, false);
            itemHandler.setStackInSlot(SIDE_INPUT_SLOT, new ItemStack(Items.GLASS_BOTTLE, 1));
            canOpenMenu = true;
            // 同步物品栏变化到客户端
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private <E extends GeoAnimatable> PlayState animate(AnimationState<E> event, Caller caller) {
        AnimationController<?> controller = event.getController();
        AnimationController.State currentState = controller.getAnimationState();
        if (controller.getCurrentRawAnimation() == kneadingAnim && currentState == AnimationController.State.STOPPED && controller.hasAnimationFinished()) {
            DanDao.LOGGER.debug("[Kneading Board] 动画执行完毕");
            caller.onAnimationStop();
            controller.setAnimation(kneadingIdle);
            return PlayState.STOP;
        }

        return PlayState.STOP;
    }

    @Override
    public double getTick(Object o) {
        return level == null ? 0 : level.getGameTime();
    }

    private interface Caller {
        void onAnimationStop();
    }
}
