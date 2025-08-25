package com.hughie.dandao.common.entity;

import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.recipe.BambooSieveRecipe;
import com.hughie.dandao.common.screen.BambooSieveMenu;
import com.hughie.dandao.common.util.BambooSieveDataNBT;
import com.hughie.dandao.common.util.PlayerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BambooSieveVirtualBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inventory = new ItemStackHandler(3);
    private ItemStack linkedItem;
    public static final int INPUT_SLOT = 0;
    public static final int SIDE_INPUT_SLOT = 1;
    public static final int PREVIEW_SLOT = 2;

    private LazyOptional<IItemHandler> lazyItemHandler;

    public BambooSieveVirtualBlockEntity() {
        super(ModEntities.BAMBOO_SIEVE_VIRTUAL_BLOCK_ENTITY.get(), BlockPos.ZERO, ModBlocks.BAMBOO_SIEVE_VIRTUAL_BLOCK.get().defaultBlockState());
        lazyItemHandler = LazyOptional.of(() -> inventory);
    }

    public void setLinkedItem(ItemStack stack) {
        this.linkedItem = stack;
        // 从物品加载数据
        loadFromItemNBT();
    }

    public void loadFromItemNBT() {
        if (linkedItem == null) return;

        // 加载库存
        CompoundTag invTag = BambooSieveDataNBT.getInventory(linkedItem);
        inventory.deserializeNBT(invTag);
    }

    public void saveToItemNBT() {
        if (linkedItem == null) return;
        // 保存前清空预览槽，避免持久化临时数据
        ItemStack preview = inventory.getStackInSlot(PREVIEW_SLOT);
        inventory.setStackInSlot(PREVIEW_SLOT, ItemStack.EMPTY);

        BambooSieveDataNBT.setMenuInventory(linkedItem, inventory);
        inventory.setStackInSlot(PREVIEW_SLOT, preview);
    }

    public void craftItem(Player player) {
        Level level = player.level();
        SimpleContainer container = new SimpleContainer(2);
        container.setItem(0, inventory.getStackInSlot(INPUT_SLOT));
        container.setItem(1, inventory.getStackInSlot(SIDE_INPUT_SLOT));

        List<BambooSieveRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(BambooSieveRecipe.Type.INSTANCE)
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .toList();

        if (recipes.isEmpty()) return;

        ItemStack output = recipes.get(0).getResultItem(level.registryAccess());
        PlayerUtils.givePlayerItems(player, output);
        inventory.extractItem(INPUT_SLOT, 1, false);
        inventory.extractItem(SIDE_INPUT_SLOT, 1, false);

        saveToItemNBT();
    }

    public boolean hasValidRecipe(Level level) {
        SimpleContainer container = new SimpleContainer(2);
        container.setItem(0, inventory.getStackInSlot(INPUT_SLOT));
        container.setItem(1, inventory.getStackInSlot(SIDE_INPUT_SLOT));
        List<BambooSieveRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(BambooSieveRecipe.Type.INSTANCE)
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .toList();
        return !recipes.isEmpty();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("item.dandao.bamboo_sieve");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player p_39956_) {
        return new BambooSieveMenu(containerId, inventory, this);
    }


}
