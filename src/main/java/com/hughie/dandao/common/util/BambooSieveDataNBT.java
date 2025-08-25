package com.hughie.dandao.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BambooSieveDataNBT {
    private static final String PROPERTIES_TAG = "BambooSieveData";
    private static final String MENU_INVENTORY = "MenuInventory";

    public static void setMenuInventory(ItemStack stack, ItemStackHandler itemStackHandler) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag propsTag = tag.getCompound(PROPERTIES_TAG);

        propsTag.put(MENU_INVENTORY, itemStackHandler.serializeNBT());
        tag.put(PROPERTIES_TAG, propsTag);
    }

    public static CompoundTag getInventory(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if (tag.contains(PROPERTIES_TAG, Tag.TAG_COMPOUND)) {
                CompoundTag propsTag = tag.getCompound(PROPERTIES_TAG);
                return propsTag.getCompound(MENU_INVENTORY);
            }
        }
        return new CompoundTag();
    }
}
