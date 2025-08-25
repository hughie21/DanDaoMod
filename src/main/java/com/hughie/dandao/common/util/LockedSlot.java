package com.hughie.dandao.common.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class LockedSlot extends SlotItemHandler {
    private Boolean isLock;
    private Predicate<ItemStack> supplier = (item) -> true;
    public LockedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean isLock) {
        super(itemHandler, index, xPosition, yPosition);
        this.isLock = isLock;
    }

    public void checkType(Predicate<ItemStack> supplier) {
        this.supplier = supplier;
    }

    public void lock() {
        this.isLock = true;
    }

    public void unlock() {
        this.isLock = false;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return !isLock && supplier.test(stack);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !isLock;
    }
}
