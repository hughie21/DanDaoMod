package com.hughie.dandao.setup.dataGen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.loot.AddItemModifier;
import net.minecraft.core.HolderSet;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, DanDao.MOD_ID);
    }

    @Override
    protected void start() {
        add("ginseng_seeds_from_jungle_temples", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/jungle_temple")).build(), LootItemRandomChanceCondition.randomChance(0.75f).build()}, ModItems.GINSENG_SEEDS.get()));

        add("blood_berry_seeds_from_jungle_temples", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/jungle_temple")).build(), LootItemRandomChanceCondition.randomChance(0.35f).build()}, ModItems.BLOOD_BERRY_SEEDS.get()));

        add("stone_flower_from_ancient_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/ancient_city")).build(), LootItemRandomChanceCondition.randomChance(0.5f).build()}, ModItems.STONE_FLOWER_SEED.get()));

        add("ground_moss_from_ancient_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/ancient_city")).build(), LootItemRandomChanceCondition.randomChance(0.5f).build()}, ModItems.GROUND_MOSS.get()));

        add("stone_flower_from_abandoned_mineshaft", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/abandoned_mineshaft")).build(), LootItemRandomChanceCondition.randomChance(0.5f).build()}, ModItems.STONE_FLOWER_SEED.get()));

        add("ground_moss_from_abandoned_mineshaft", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/abandoned_mineshaft")).build(), LootItemRandomChanceCondition.randomChance(0.5f).build()}, ModItems.GROUND_MOSS.get()));

        add("spirit_stone_from_ancient_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/ancient_city")).build(), LootItemRandomChanceCondition.randomChance(0.5f).build()}, ModItems.SPIRIT_STONE.get()));

        add("spirit_stone_from_buried_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/buried_treasure")).build(), LootItemRandomChanceCondition.randomChance(0.35f).build()}, ModItems.SPIRIT_STONE.get()));

        add("flare_fruit_from_nether_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/nether_bridge")).build(), LootItemRandomChanceCondition.randomChance(0.75f).build()}, ModItems.FLARE_FRUIT_SEEDS.get()));

        add("sun_leaf_from_nether_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/nether_bridge")).build(), LootItemRandomChanceCondition.randomChance(0.25f).build()}, ModItems.SUN_FLOWER_SEEDS.get()));

        add("snow_lotus_from_igloo", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/igloo_chest")).build(), LootItemRandomChanceCondition.randomChance(0.25f).build()}, ModItems.SNOW_LOTUS.get()));

        add("snow_ginseng_from_igloo", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("chests/igloo_chest")).build(), LootItemRandomChanceCondition.randomChance(0.25f).build()}, ModItems.SNOW_LOTUS.get()));

    }
}
