package com.hughie.dandao.common.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FuelValues {
    private Map<TagKey<Item>, Integer> fuel_values;

    public FuelValues() {
        fuel_values = new HashMap<>();
        fuel_values.put(ModTags.FUELS_V1, 1);
        fuel_values.put(ModTags.FUELS_V2, 2);
        fuel_values.put(ModTags.FUELS_V4, 4);
        fuel_values.put(ModTags.FUELS_V6, 6);
        fuel_values.put(ModTags.FUELS_V20, 20);
        fuel_values.put(ModTags.FUELS_V50, 50);
        fuel_values.put(ModTags.FUELS_V80, 80);
        fuel_values.put(ModTags.FUELS_V100, 100);
    }

    public int getValue(ItemStack item) {
        for(TagKey<Item> tag : fuel_values.keySet()) {
            if(item.is(tag)) {
                return fuel_values.get(tag);
            }
        }
        return 0;
    }
}
