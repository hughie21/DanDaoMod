package com.hughie.dandao.setup.dataGen.loot;

import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.block.custom.*;
import com.hughie.dandao.common.item.ModItems;
import com.ibm.icu.impl.Pair;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.PUTUAN.get());
        this.dropSelf(ModBlocks.MORTAR.get());
        this.dropSelf(ModBlocks.IRON_MILL.get());
        this.dropSelf(ModBlocks.REACTOR.get());
        this.dropSelf(ModBlocks.KNEADING_BOARD.get());
        this.dropSelf(ModBlocks.INCENSE_BURNER.get());

        this.add(ModBlocks.SPIRIT_STONE_ORE.get(),
                block -> createOreDrops(ModBlocks.SPIRIT_STONE_ORE.get(), ModItems.SPIRIT_STONE.get(), 2f, 3f));
        this.add(ModBlocks.DEEPSLATE_SPIRIT_STONE_ORE.get(),
                block -> createOreDrops(ModBlocks.SPIRIT_STONE_ORE.get(), ModItems.SPIRIT_STONE.get(), 2f, 3f));
        this.add(ModBlocks.CINNABAR_ORE.get(),
                block -> createOreDrops(ModBlocks.CINNABAR_ORE.get(), ModItems.CINNABAR.get(),3f, 5f));
        this.add(ModBlocks.DEEPSLATE_CINNABAR_ORE.get(),
                block -> createOreDrops(ModBlocks.CINNABAR_ORE.get(), ModItems.CINNABAR.get(),3f, 5f));
        this.add(ModBlocks.MICA_ORE.get(),
                block -> createOreDrops(ModBlocks.MICA_ORE.get(), ModItems.MICA.get(), 3, 4));
        this.add(ModBlocks.REALGAR_ORE.get(),
                block -> createOreDrops(ModBlocks.REALGAR_ORE.get(), ModItems.REALGAR.get(), ModItems.ORPIMENT.get(),1f, 2f, 0f, 1f));
        this.add(ModBlocks.DEEPSLATE_REALGAR_ORE.get(),
                block -> createOreDrops(ModBlocks.REALGAR_ORE.get(), ModItems.REALGAR.get(), ModItems.ORPIMENT.get(),1f, 2f, 0f, 1f));
        this.add(ModBlocks.SULFUR_ORE.get(),
                block -> createOreDrops(ModBlocks.SULFUR_ORE.get(), ModItems.SULFUR.get(), 2f, 4f));
        this.add(ModBlocks.DEEPSLATE_SULFUR_ORE.get(),
                block -> createOreDrops(ModBlocks.SULFUR_ORE.get(), ModItems.SULFUR.get(), 2f, 4f));
        this.add(ModBlocks.NETHER_SULFUR_ORE.get(),
                block -> createOreDrops(ModBlocks.SULFUR_ORE.get(), ModItems.SULFUR.get(), 2f, 4f));

        this.add(ModBlocks.GINSENG_CROP.get(),createMutiCropsDrops(ModBlocks.GINSENG_CROP.get(), ModItems.GINSENG.get(), ModItems.GINSENG_SEEDS.get(), Pair.of(1,2), Pair.of(1, 3), 5, GinsengCropBlock.AGE, simpleCropCondition(ModBlocks.GINSENG_CROP.get(), 5, GinsengCropBlock.AGE)));
        this.add(ModBlocks.WILD_GINSENG_CROP.get(),
                block -> createOreDrops(ModBlocks.WILD_GINSENG_CROP.get(), ModItems.GINSENG.get(), ModItems.GINSENG_SEEDS.get(), 1, 2, 1, 3));
        this.add(ModBlocks.WILD_BLOOD_BERRY_CROP.get(),
                block -> createOreDrops(ModBlocks.WILD_BLOOD_BERRY_CROP.get(), ModItems.BLOOD_BERRY.get(),2, 3));
        this.add(ModBlocks.BLOOD_BERRY_CROP.get(), createMutiCropsDrops(ModBlocks.BLOOD_BERRY_CROP.get(), ModItems.BLOOD_BERRY.get(), 2, 3, simpleCropCondition(ModBlocks.BLOOD_BERRY_CROP.get(), 5, BloodBerryCropBlock.AGE)));
        this.add(ModBlocks.BLOOD_VINES.get(), createVineDrops(ModBlocks.BLOOD_VINES.get(), ModItems.BLOOD_VINES_ITEM.get()));
        this.add(ModBlocks.BLOOD_VINES_PLANT.get(), createVineDrops(ModBlocks.BLOOD_VINES_PLANT.get(), ModItems.BLOOD_VINES_ITEM.get()));
        this.add(ModBlocks.GROUND_MOSS.get(), createMutiCropsDrops(ModBlocks.GROUND_MOSS.get(), ModItems.GROUND_MOSS.get(), 2, 3, simpleCropCondition(ModBlocks.GROUND_MOSS.get(), 2, GroundMossCropBlock.AGE)));
        this.add(ModBlocks.STONE_FLOWER_CROP.get(), createMutiCropsDrops(ModBlocks.STONE_FLOWER_CROP.get(), ModItems.STONE_FLOWER.get(), ModItems.STONE_FLOWER_SEED.get(), Pair.of(1,1), Pair.of(1, 2), 3, StoneFlowerCropBlock.AGE, simpleCropCondition(ModBlocks.STONE_FLOWER_CROP.get(), 3, StoneFlowerCropBlock.AGE)));
        this.add(ModBlocks.FLARE_FRUIT_CROP.get(), createMutiCropsDrops(ModBlocks.FLARE_FRUIT_CROP.get(), ModItems.FLARE_FRUIT.get(), 2, 3, simpleCropCondition(ModBlocks.FLARE_FRUIT_CROP.get(), 8,  FlareFruitCropBlock.AGE)));
        this.add(ModBlocks.SUN_FLOWER_CROP.get(), createMutiCropsDrops(ModBlocks.SUN_FLOWER_CROP.get(), ModItems.SUN_LEAF.get(), ModItems.SUN_FLOWER_SEEDS.get(), Pair.of(3, 1), Pair.of(4, 2), 5, SunFlowerCropBlock.AGE, simpleCropCondition(ModBlocks.SUN_FLOWER_CROP.get(), 5, SunFlowerCropBlock.AGE)));
        this.add(ModBlocks.SNOW_LOTUS_CROP.get(), createMutiCropsDrops(ModBlocks.STONE_FLOWER_CROP.get(), ModItems.SNOW_LOTUS.get(), 2,3, simpleCropCondition(ModBlocks.SNOW_LOTUS_CROP.get(), 7, SnowLotusCropBlock.AGE)));
        this.add(ModBlocks.SNOW_GINSENG_CROP.get(),createMutiCropsDrops(ModBlocks.SNOW_GINSENG_CROP.get(), ModItems.SNOW_GINSENG.get(), ModItems.SNOW_GINSENG_SEEDS.get(), Pair.of(1,2), Pair.of(1, 3), 5, SnowGinsengCropBlock.AGE, simpleCropCondition(ModBlocks.SNOW_GINSENG_CROP.get(), 5, SnowGinsengCropBlock.AGE)));
    }

    private LootTable.Builder createOreDrops(Block block, Item item, float min, float max) {
        return createSilkTouchDispatchTable(block,
                this.applyExplosionDecay(block, LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                ));
    }

    private LootTable.Builder createVineDrops(Block vines, Item result) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .setBonusRolls(ConstantValue.exactly(0.0F))
                        .add(AlternativesEntry.alternatives(
                                // 第一个子项：使用剪刀或精准采集时直接掉落藤蔓方块
                                LootItem.lootTableItem(result)
                                        .when(AnyOfCondition.anyOf(
                                                MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)),
                                                MatchTool.toolMatches(ItemPredicate.Builder.item()
                                                        .hasEnchantment(new EnchantmentPredicate(
                                                                Enchantments.SILK_TOUCH,
                                                                MinMaxBounds.Ints.atLeast(1)
                                                        ))
                                                )
                                        )),
                                // 第二个子项：普通采集（受时运影响概率）
                                LootItem.lootTableItem(result)
                                        .when(BonusLevelTableCondition.bonusLevelFlatChance(
                                                Enchantments.BLOCK_FORTUNE,
                                                0.33F, // 时运0级概率
                                                0.55F, // 时运1级概率
                                                0.77F, // 时运2级概率
                                                1.0F   // 时运3级概率
                                        ))
                        ))
                );
    }

    private LootItemCondition.Builder simpleCropCondition(Block crop, int whenToHarvest, IntegerProperty age) {
        return  LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(crop)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(age, whenToHarvest));
    }

    private LootItemCondition.Builder twoBlockCropCondition(Block crop, int firstStage, int secondStage, IntegerProperty age) {
        return LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(crop)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(age, firstStage))
                .or(LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(crop)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(age, secondStage)));
    }

    // 不掉落种子
    private LootTable.Builder createMutiCropsDrops(Block crop, Item fruit, int minDrops, int maxDrops, LootItemCondition.Builder condition) {
        LootTable.Builder builder = LootTable.lootTable();
        builder.withPool(fruitDrops(condition,fruit,minDrops,maxDrops)).withPool(dropSelf(crop, condition));
        return builder;
    }

    // 掉落种子
    private LootTable.Builder createMutiCropsDrops(Block crop, Item fruit, Item seed, Pair<Integer, Integer> minDrops, Pair<Integer,Integer> maxDrops, int whenToHarvest, IntegerProperty age, LootItemCondition.Builder condition) {
        LootTable.Builder builder = LootTable.lootTable();
        builder.withPool(fruitDrops(condition,fruit,minDrops.first,maxDrops.first)).withPool(seedDrops(crop, seed, age, minDrops.second, maxDrops.second, whenToHarvest)).withPool(dropSelf(crop, condition));
        return builder;
    }

    private LootPool.Builder fruitDrops(LootItemCondition.Builder condition, Item fruit, int minDrops, int maxDrops) {
        return LootPool.lootPool()
                .add(LootItem.lootTableItem(fruit)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5f, 1)))
                .when(condition);
    }

    private LootPool.Builder seedDrops(Block crop, Item seed, IntegerProperty age, int minDrops, int maxDrops, int whenToHarvest) {
        return LootPool.lootPool()
                .add(LootItem.lootTableItem(seed)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops))))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(age, whenToHarvest)) // 成熟时必掉种子
                        .or(LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(age, whenToHarvest - 1))));
    }

    private LootPool.Builder dropSelf(Block crop, LootItemCondition.Builder condition) {
        return LootPool.lootPool()
                .add(LootItem.lootTableItem(crop))
                .when(condition.invert());
    }

    private LootTable.Builder createOreDrops(Block block, Item item1, Item item2, float min1, float max1, float min2, float max2) {
        LootTable.Builder builder = LootTable.lootTable();
        LootItemCondition.Builder silkTouchCondition = MatchTool.toolMatches(ItemPredicate.Builder.item()
                .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

        LootPool.Builder normalDrops = LootPool.lootPool()
                .add(LootItem.lootTableItem(item1)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(min1, max1)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))
                .add(LootItem.lootTableItem(item2)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(min2, max2)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))
                .when(silkTouchCondition.invert());

        LootPool.Builder silkTouchDrop = LootPool.lootPool()
                .add(LootItem.lootTableItem(block))
                .when(silkTouchCondition);

        return builder
                .withPool(applyExplosionDecay(block, normalDrops))
                .withPool(silkTouchDrop);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
