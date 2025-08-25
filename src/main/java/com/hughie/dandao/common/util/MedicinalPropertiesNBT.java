package com.hughie.dandao.common.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class MedicinalPropertiesNBT {
    private static final String PROPERTIES_TAG = "MedicinalProps";

    public static void saveProperties(ItemStack stack, MedicinalProperties property, int level) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag propsTag = new CompoundTag();

        propsTag.putInt(property.getName(), level);

        tag.put(PROPERTIES_TAG, propsTag);
    }

    public static Pair<MedicinalProperties, Integer> loadProperties(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();

            if (tag.contains(PROPERTIES_TAG, Tag.TAG_COMPOUND)) {
                CompoundTag propsTag = tag.getCompound(PROPERTIES_TAG);

                for (MedicinalProperties prop : MedicinalProperties.values()) {
                    String key = prop.getName();
                    if (propsTag.contains(key, Tag.TAG_INT)) {
                        int level = propsTag.getInt(key);
                        return Pair.of(prop, level);
                    }
                }
            }
        }
        return Pair.of(MedicinalProperties.DEFAULT, 0);
    }

    public static int getPropertyLevel(ItemStack stack) {
        Pair<MedicinalProperties, Integer> props = loadProperties(stack);
        return props.getSecond();
    }

    public static void setPropertyLevel(ItemStack stack, MedicinalProperties property, int level) {
        saveProperties(stack, property, level);
    }
}
