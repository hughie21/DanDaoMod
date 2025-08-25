package com.hughie.dandao.common.screen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.entity.BambooSieveVirtualBlockEntity;
import com.hughie.dandao.common.recipe.BambooSieveRecipe;
import com.hughie.dandao.common.util.LockedSlot;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class BambooSieveMenu extends AbstractContainerMenu {
    private final BambooSieveVirtualBlockEntity blockEntity;
    private final ContainerLevelAccess access;
    private ItemStack sieveStack;
    private Player player;
    private UUID uuid;
    private static final Map<UUID, SimpleContainer> containerItems = new HashMap<>();

    public BambooSieveMenu(int windowId, Inventory playerInventory, BambooSieveVirtualBlockEntity blockEntity) {
        super(ModMenuTypes.BAMBOO_SIEVE_MENU.get(), windowId);

        this.blockEntity = blockEntity;
        this.access = ContainerLevelAccess.NULL;
        this.player = playerInventory.player;
        this.sieveStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        this.uuid = UUID.randomUUID();

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            addSlot(new SlotItemHandler(iItemHandler, 0, 45, 39));
            addSlot(new SlotItemHandler(iItemHandler, 1, 79,14));
            addSlot(new LockedSlot(iItemHandler, 2, 120,38, true));
        });

        this.addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu menu, int index, ItemStack stack) {
                if(menu instanceof BambooSieveMenu bambooSieveMenu && bambooSieveMenu.getUuid().equals(uuid)) {
                    if(index == BambooSieveVirtualBlockEntity.INPUT_SLOT + 36) {
                        checkMap(uuid, 0, stack);
                    } else if (index == BambooSieveVirtualBlockEntity.SIDE_INPUT_SLOT + 36) {
                        checkMap(uuid, 1, stack);
                    }
                    Level level = player.level();
                    if (!level.isClientSide()) {
                        checkRecipe(level, uuid);
                    }
                }
            }

            private void checkMap(UUID uuid, int index, ItemStack stack) {
                if(containerItems.containsKey(uuid)) {
                    SimpleContainer temp = containerItems.get(uuid);
                    temp.setItem(index, stack);
                    containerItems.put(uuid, temp);
                } else {
                    SimpleContainer temp = new SimpleContainer(2);
                    temp.setItem(index, stack);
                    containerItems.put(uuid, temp);
                }
            }

            private void checkRecipe(Level level, UUID uuid) {
                SimpleContainer simpleContainer = containerItems.getOrDefault(uuid, new SimpleContainer(2));
                List<BambooSieveRecipe> recipes = level.getRecipeManager()
                        .getAllRecipesFor(BambooSieveRecipe.Type.INSTANCE)
                        .stream()
                        .filter(recipe -> recipe.matches(simpleContainer, level))
                        .toList();
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
                    if (!recipes.isEmpty()) {
                        iItemHandler.insertItem(BambooSieveVirtualBlockEntity.PREVIEW_SLOT, recipes.get(0).getResultItem(level.registryAccess()), false);
                    }else {
                        iItemHandler.extractItem(BambooSieveVirtualBlockEntity.PREVIEW_SLOT,1, false);
                    }
                });
            }

            @Override
            public void dataChanged(AbstractContainerMenu p_150524_, int p_150525_, int p_150526_) {

            }
        });

        checkInitialRecipe(player.level());
    }

    public UUID getUuid() {
        return uuid;
    }

    private void checkInitialRecipe(Level level) {
        SimpleContainer container = new SimpleContainer(2);
        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            container.setItem(0, iItemHandler.getStackInSlot(BambooSieveVirtualBlockEntity.INPUT_SLOT));
            container.setItem(1, iItemHandler.getStackInSlot(BambooSieveVirtualBlockEntity.SIDE_INPUT_SLOT));
            List<BambooSieveRecipe> recipes = level.getRecipeManager()
                    .getAllRecipesFor(BambooSieveRecipe.Type.INSTANCE)
                    .stream()
                    .filter(recipe -> recipe.matches(container, level))
                    .toList();
            if (!recipes.isEmpty()) {
                iItemHandler.insertItem(BambooSieveVirtualBlockEntity.PREVIEW_SLOT, recipes.get(0).getResultItem(level.registryAccess()), false);
            } else {
                iItemHandler.extractItem(BambooSieveVirtualBlockEntity.PREVIEW_SLOT, 1, false);
            }
        });
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for(int i = 0; i < 3; ++i) {
            for(int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).equals(sieveStack);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 3;
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public void removed(Player p_38940_) {
        super.removed(p_38940_);
        containerItems.remove(this.uuid);
        blockEntity.saveToItemNBT();
    }

}
