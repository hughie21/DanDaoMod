package com.hughie.dandao.common.util;

import com.hughie.dandao.DanDao;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Item> HERBS = Items.tag("herbs");

    public static final TagKey<Item> MINERAL = Items.tag("mineral");

    public static final TagKey<Item> SUPPORT_POTION = Items.tag("support_potion");

    public static final TagKey<Item> FUELS = Items.tag("fuels");
    public static final TagKey<Item> FUELS_V1 = Items.tag("fuels_v1");
    public static final TagKey<Item> FUELS_V2 = Items.tag("fuels_v2");
    public static final TagKey<Item> FUELS_V4 = Items.tag("fuels_v4");
    public static final TagKey<Item> FUELS_V6 = Items.tag("fuels_v6");
    public static final TagKey<Item> FUELS_V20 = Items.tag("fuels_v20");
    public static final TagKey<Item> FUELS_V50 = Items.tag("fuels_v50");
    public static final TagKey<Item> FUELS_V80 = Items.tag("fuels_v80");
    public static final TagKey<Item> FUELS_V100 = Items.tag("fuels_v100");

    public static final TagKey<Item> COOLING = Items.tag("cooling");
    public static final TagKey<Item> COOLING_V20 = Items.tag("cooling_v20");
    public static final TagKey<Item> COOLING_V50 = Items.tag("cooling_v50");
    public static final TagKey<Item> COOLING_V100 = Items.tag("cooling_v100");

    public static class Blocks {
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, name));
        }
    }

    public static class Items {
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, name));
        }
    }
}
