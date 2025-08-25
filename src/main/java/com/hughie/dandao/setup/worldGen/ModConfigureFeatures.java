package com.hughie.dandao.setup.worldGen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.api.ICustomCrop;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.block.custom.FlareFruitCropBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfigureFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SPIRIT_STONE_ORE_KEY = registerKey("spirit_stone_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_REALGAR_ORE_KEY = registerKey("realgar_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_CINNABAR_ORE_KEY = registerKey("cinnabar_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SULFUR_ORE_KEY = registerKey("sulfur_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_MICA_ORE_KEY = registerKey("mica_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_SULFUR_ORE_KEY = registerKey("nether_sulfur_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_GINSENG_CROP_KEY = registerKey("ginseng_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_BLOOD_BERRY_CROP_KEY = registerKey("blood_berry");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_BLOOD_VINE_KEY = registerKey("blood_vine");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_GROUND_MOSS_KEY = registerKey("ground_moss");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_STONE_FLOWER_KEY = registerKey("stone_flower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_FLARE_FRUIT_CROP_KEY = registerKey("flare_fruit_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_SUN_FLOWER_CROP_KEY = registerKey("sun_flower_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SNOW_LOTUS_CROP_KEY = registerKey("snow_lotus");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SNOW_GINSENG_CROP_KEY = registerKey("snow_ginseng");

    public static void boostrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplaceable = new BlockMatchTest(Blocks.NETHERRACK);

        List<OreConfiguration.TargetBlockState> overworldSpiritStoneOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.SPIRIT_STONE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DEEPSLATE_SPIRIT_STONE_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldRealgarOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.REALGAR_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DEEPSLATE_REALGAR_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldCinnabarOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.CINNABAR_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DEEPSLATE_CINNABAR_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldSulfurOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.SULFUR_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DEEPSLATE_SULFUR_ORE.get().defaultBlockState())
        );

        register(context, OVERWORLD_SPIRIT_STONE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSpiritStoneOres, 2));
        register(context, OVERWORLD_REALGAR_ORE_KEY, Feature.ORE, new OreConfiguration(overworldRealgarOres, 6));
        register(context, OVERWORLD_CINNABAR_ORE_KEY, Feature.ORE, new OreConfiguration(overworldCinnabarOres, 9));
        register(context, OVERWORLD_SULFUR_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSulfurOres, 5));
        register(context, OVERWORLD_MICA_ORE_KEY, Feature.ORE, new OreConfiguration(stoneReplaceable, ModBlocks.MICA_ORE.get().defaultBlockState(), 12));
        register(context, NETHER_SULFUR_ORE_KEY, Feature.ORE, new OreConfiguration(netherrackReplaceable, ModBlocks.NETHER_SULFUR_ORE.get().defaultBlockState(), 12));

        register(context, OVERWORLD_GINSENG_CROP_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(ModBlocks.WILD_GINSENG_CROP.get().defaultBlockState())
                        ),
                        List.of(Blocks.GRASS_BLOCK)
                ));

        register(context, OVERWORLD_BLOOD_BERRY_CROP_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(ModBlocks.WILD_BLOOD_BERRY_CROP.get().defaultBlockState())
                        ),
                        List.of(Blocks.GRASS_BLOCK)
                ));

        register(context, OVERWORLD_BLOOD_VINE_KEY, Feature.BLOCK_COLUMN,
                new BlockColumnConfiguration(
                        List.of(BlockColumnConfiguration.layer(
                                new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(UniformInt.of(0, 19), 2)
                                        .add(UniformInt.of(0, 2), 3)
                                        .add(UniformInt.of(0, 6), 10).build()),
                                BlockStateProvider.simple(ModBlocks.BLOOD_VINES_PLANT.get().defaultBlockState())), BlockColumnConfiguration.layer(ConstantInt.of(1),
                                BlockStateProvider.simple(ModBlocks.BLOOD_VINES_PLANT.get().defaultBlockState()))),
                        Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true));

        register(context, OVERWORLD_GROUND_MOSS_KEY, Feature.FLOWER,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(matureCropState(ModBlocks.GROUND_MOSS.get()))
                        ),
                        List.of(Blocks.STONE)
                ));

        register(context, OVERWORLD_STONE_FLOWER_KEY, Feature.FLOWER,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(matureCropState(ModBlocks.STONE_FLOWER_CROP.get()))
                        ),
                        List.of(Blocks.STONE)
                ));

        register(context, NETHER_FLARE_FRUIT_CROP_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(ModBlocks.FLARE_FRUIT_CROP.get().defaultBlockState().setValue(FlareFruitCropBlock.AGE, 5))
                        ),
                        List.of(Blocks.NETHERRACK)
                ));

        register(context, NETHER_SUN_FLOWER_CROP_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(matureCropState(ModBlocks.SUN_FLOWER_CROP.get()))
                        ),
                        List.of(Blocks.NETHERRACK)
                ));

        register(context, OVERWORLD_SNOW_LOTUS_CROP_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(matureCropState(ModBlocks.SNOW_LOTUS_CROP.get()))
                        ),
                        List.of(Blocks.SNOW_BLOCK)
                ));

        register(context, OVERWORLD_SNOW_GINSENG_CROP_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(matureCropState(ModBlocks.SNOW_GINSENG_CROP.get()))
                        ),
                        List.of(Blocks.SNOW_BLOCK)
                ));
    }

    public static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

    private static BlockState matureCropState(Block block) {
        if (block instanceof ICustomCrop cropBlock) {
            return block.defaultBlockState().setValue(cropBlock.getAgeProperty(), cropBlock.getMaxAge());
        }
        throw new IllegalArgumentException("Block is not a custom crop");
    }
}
