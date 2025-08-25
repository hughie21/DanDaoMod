package com.hughie.dandao.common.entity;

import com.hughie.dandao.api.IMedicalItem;
import com.hughie.dandao.common.block.custom.IronMillBlock;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.screen.IronMillMenu;
import com.hughie.dandao.common.sound.ModSounds;
import com.hughie.dandao.common.sound.SoundControl;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.hughie.dandao.common.util.MedicinalPropertiesNBT;
import com.hughie.dandao.common.util.ModTags;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
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
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;

public class IronMillBlockEntity extends BlockEntity implements MenuProvider, GeoBlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4);
    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int INPUT_SLOT_3 = 2;
    private static final int PREVIEW_SLOT = 3;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int process = 0;
    private int maxProcess = 12;

    private long lastPlayTime = 0;
    private static final long DEBOUNCE_TIME = 5000;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final RawAnimation rollAnim = RawAnimation.begin().then("animation.iron_mill.roll", Animation.LoopType.PLAY_ONCE);
    private final RawAnimation idleAnim = RawAnimation.begin().then("animation.iron_mill.idle", Animation.LoopType.LOOP);

    private final String CONTROLLER_NAME = "iron_mill_controller";
    private final SoundControl soundControl = new SoundControl();

    public IronMillBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.IRON_MILL_ENT.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> IronMillBlockEntity.this.process;
                    case 1 -> IronMillBlockEntity.this.maxProcess;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> IronMillBlockEntity.this.process = pValue;
                    case 1 -> IronMillBlockEntity.this.maxProcess = pValue;
                    default -> {}
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(3);
        inventory.setItem(INPUT_SLOT_1, itemHandler.getStackInSlot(INPUT_SLOT_1));
        inventory.setItem(INPUT_SLOT_2, itemHandler.getStackInSlot(INPUT_SLOT_2));
        inventory.setItem(INPUT_SLOT_3, itemHandler.getStackInSlot(INPUT_SLOT_3));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public Direction getFacing() {
        BlockState state = this.getBlockState();
        if (state.getBlock() instanceof IronMillBlock) {
            return state.getValue(IronMillBlock.FACING);
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
        return Component.translatable("block.dandao.iron_mill");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player p_39956_) {
        return new IronMillMenu(containerId, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("iron_mill_inventory", itemHandler.serializeNBT());
        tag.putInt("iron_mill_process", process);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("iron_mill_inventory"));
        process = tag.getInt("iron_mill_process");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(isValidTags()) {
            showPreview();
            setChanged(level, pos, state);

            if(hasProcessFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetPreview();
            resetProgress();
        }
    }

    private void resetPreview() {
        itemHandler.extractItem(PREVIEW_SLOT, 1, false);
    }

    private void showPreview() {
        ItemStack result = calculateResult();
        itemHandler.setStackInSlot(PREVIEW_SLOT, result);
    }

    private void resetProgress() {
        process = 0;
    }

    private void craftItem() {
        ItemStack result = calculateResult();
        itemHandler.extractItem(INPUT_SLOT_1, 1, false);
        itemHandler.extractItem(INPUT_SLOT_2, 1, false);
        itemHandler.extractItem(INPUT_SLOT_3, 1, false);
        Level world = this.level;
        BlockPos pos = this.worldPosition;
        soundControl.stopCurrentSound();
        if (world != null && !world.isClientSide) {
            Block.popResource(world, pos, result);
            triggerAnim(CONTROLLER_NAME, "idle");
        }
    }

    private ItemStack calculateResult() {
        int resultLevel = -1;
        MedicinalProperties resultMedProperty = MedicinalProperties.DEFAULT;

        for (int i = 0; i < 3; i++) {
            ItemStack inputStack = itemHandler.getStackInSlot(i);
            if(inputStack.isEmpty()) {
                continue;
            }
            IMedicalItem inputItem = (IMedicalItem) inputStack.getItem();
            Pair<MedicinalProperties, Integer> medProperty;

            if(inputStack.is(ModItems.MEDICINAL_POWDER.get())) {
                medProperty = MedicinalPropertiesNBT.loadProperties(inputStack);
            }else {
                medProperty =inputItem.getMedicalProperty();
            }

            if(resultMedProperty == MedicinalProperties.DEFAULT) {
                resultMedProperty = medProperty.getFirst();
                resultLevel = medProperty.getSecond();
                continue;
            }
            if(medProperty.getSecond() > resultLevel) {
                Map<MedicinalProperties, Integer> relationship = resultMedProperty.getRelationship();
                resultLevel = medProperty.getSecond() + relationship.get(resultMedProperty);
                resultMedProperty = medProperty.getFirst();
            }else {
                Map<MedicinalProperties, Integer> relationship = medProperty.getFirst().getRelationship();
                resultLevel = resultLevel + relationship.get(medProperty.getFirst());
            }

            if(resultLevel < resultMedProperty.getMinLevel()) {
                resultLevel = resultMedProperty.getMinLevel();
            }else if(resultLevel > resultMedProperty.getMaxLevel()) {
                resultLevel = resultMedProperty.getMaxLevel();
            }
        }
        ItemStack result = new ItemStack(ModItems.MEDICINAL_POWDER.get());

        MedicinalPropertiesNBT.setPropertyLevel(result, resultMedProperty, resultLevel);

        return result;
    }

    private boolean hasProcessFinished() {
        return process >= maxProcess;
    }

    public void increaseCraftingProgress(Level level, BlockPos pos, Player player) {
        if(isValidTags()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPlayTime >= DEBOUNCE_TIME) {
                soundControl.playControllableSound(player, ModSounds.GRIND_MEDICINE.get());
                lastPlayTime = currentTime;
                if(level instanceof ServerLevel) {
                    triggerAnim(CONTROLLER_NAME, "roll");
                }
            }
            process++;
        }
    }

    private boolean isValidTags() {
        ItemStack input1 = itemHandler.getStackInSlot(INPUT_SLOT_1);
        ItemStack input2 = itemHandler.getStackInSlot(INPUT_SLOT_2);
        ItemStack input3 = itemHandler.getStackInSlot(INPUT_SLOT_3);

        boolean containAtLeastOneHerb = input1.is(ModTags.HERBS) || input2.is(ModTags.HERBS) || input3.is(ModTags.HERBS);
        boolean containAtLeastOneMineral = input1.is(ModTags.MINERAL) || input2.is(ModTags.MINERAL) || input3.is(ModTags.MINERAL);

        return containAtLeastOneHerb && containAtLeastOneMineral;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<IronMillBlockEntity> controller = new AnimationController<>(this, CONTROLLER_NAME, state -> PlayState.STOP)
                .triggerableAnim("roll", rollAnim)
                .triggerableAnim("idle", idleAnim);

        controllerRegistrar.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object o) {
        return level == null ? 0 : level.getGameTime();
    }
}
