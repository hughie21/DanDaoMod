package com.hughie.dandao.common.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CoolingValues {
    private Map<TagKey<Item>, Integer> cooling_values;

    public CoolingValues() {
        cooling_values = new HashMap<>();

        cooling_values.put(ModTags.COOLING_V20, 20);
        cooling_values.put(ModTags.COOLING_V50, 50);
        cooling_values.put(ModTags.COOLING_V100, 100);
    }

    public int getValue(ItemStack item) {
        for(TagKey<Item> tag : cooling_values.keySet()) {
            if(item.is(tag)) {
                return cooling_values.get(tag);
            }
        }
        return 0;
    }
}
