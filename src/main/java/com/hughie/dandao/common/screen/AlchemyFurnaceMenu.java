package com.hughie.dandao.common.screen;

import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.entity.AlchemyFurnaceBlockEntity;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.util.LockedSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class AlchemyFurnaceMenu extends AbstractContainerMenu {
    public final AlchemyFurnaceBlockEntity alchemyFurnaceBlockEntity;
    private final Level level;
    private final ContainerData data;

    public AlchemyFurnaceMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(7));
    }

    public AlchemyFurnaceMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.ALCHEMY_FURNACE_MENU.get(), pContainerId);
        checkContainerSize(inv, 12);

        alchemyFurnaceBlockEntity = (AlchemyFurnaceBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.alchemyFurnaceBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 10, 23));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 10, 44));
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 10, 64));
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 10, 84));

            this.addSlot(new LockedSlot(iItemHandler, 4, 69, 22, false));
            this.addSlot(new LockedSlot(iItemHandler, 5, 69, 43, false));
            this.addSlot(new LockedSlot(iItemHandler, 6, 69, 63, false));
            this.addSlot(new LockedSlot(iItemHandler, 7, 69, 83, false));

            this.addSlot(new LockedSlot(iItemHandler, 8, 162, 22, false));
            this.addSlot(new LockedSlot(iItemHandler, 9, 162, 43, false));
            this.addSlot(new LockedSlot(iItemHandler, 10, 162, 63, false));
            this.addSlot(new LockedSlot(iItemHandler, 11, 162, 83, false));
        });

        int playerSlotsCount = 36;
        for(int i = 4; i < 12; i++) {
            Slot slot = this.getSlot(i + playerSlotsCount);
            if (slot instanceof LockedSlot lockedSlot) {
                lockedSlot.checkType(itemStack -> {
                    if(itemStack.is(ModItems.DAN_EMBRYO.get())) {
                        return true;
                    }else {
                        return false;
                    }
                });
            }
        }

        addDataSlots(data);
    }

    public boolean isBurning() { return data.get(0) > 0; }
    public boolean isHeated() { return data.get(2) > 0; }
    public boolean isCooling() { return data.get(5) > 0; }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 12;
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
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, alchemyFurnaceBlockEntity.getBlockPos()),
                player, ModBlocks.ALCHEMY_FURNACE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for(int i = 0; i < 3; ++i) {
            for(int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 13 + l * 18,  110 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 13 + i * 18, 168));
        }
    }

    @Override
    public void removed(Player p_38940_) {
        super.removed(p_38940_);
    }

    public int getButtonState() {
        return data.get(4);
    }

    public int getScaledBurningProcess() {
        int process = this.data.get(0);
        int maxProcess = this.data.get(1);
        int barSize = 78;
        return maxProcess != 0 && process != 0 ? process * barSize / maxProcess : 0;
    }

    public int getScaledHeatProcess() {
        int process = this.data.get(2);
        int maxProcess = this.data.get(3);
        int barSize = 79;
        return maxProcess != 0 && process != 0 ? process * barSize / maxProcess : 0;
    }

    public int getScaledCoolingProcess() {
        int process = this.data.get(5);
        int maxProcess = this.data.get(6);
        int barSize = 78;
        return maxProcess != 0 && process != 0 ? process * barSize / maxProcess : 0;
    }

    public ContainerData getData() {
        return this.data;
    }
}
