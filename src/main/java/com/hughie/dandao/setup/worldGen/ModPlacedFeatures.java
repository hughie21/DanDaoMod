package com.hughie.dandao.setup.worldGen;

import com.hughie.dandao.DanDao;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> SPIRIT_STONE_ORE_PLACED_KEY = registerKey("spirit_stone_ore_placed");
    public static final ResourceKey<PlacedFeature> REALGAR_ORE_PLACED_KEY = registerKey("realgar_ore_placed");
    public static final ResourceKey<PlacedFeature> CINNABAR_ORE_PLACED_KEY = registerKey("cinnabar_ore_placed");
    public static final ResourceKey<PlacedFeature> SULFUR_ORE_PLACED_KEY = registerKey("sulfur_ore_placed");
    public static final ResourceKey<PlacedFeature> MICA_ORE_PLACED_KEY = registerKey("mica_ore_placed");
    public static final ResourceKey<PlacedFeature> NETHER_SULFUR_PLACED_KEY = registerKey("nether_sulfur_placed");

    public static final ResourceKey<PlacedFeature> OVERWORLD_GINSENG_CROP_PLACED_KEY = registerKey("ginseng_crop_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_BLOOD_BERRY_CROP_PLACED_KEY = registerKey("blood_berry_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_BLOOD_VINE_PLACED_KEY = registerKey("blood_vine_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_GROUND_MOSS_PLACED_KEY = registerKey("ground_moss_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_STONE_FLOWER_PLACED_KEY = registerKey("stone_flower_placed");
    public static final ResourceKey<PlacedFeature> NETHER_FLARE_FRUIT_CROP_PLACED_KEY = registerKey("flare_fruit_placed");
    public static final ResourceKey<PlacedFeature> NETHER_SUN_FLOWER_CROP_PLACED_KEY = registerKey("sun_flower_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_SNOW_LOTUS_CROP_PLACED_KEY = registerKey("snow_lotus_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_SNOW_GINSENG_CROP_PLACED_KEY = registerKey("snow_ginseng_placed");

    public static void boostrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, SPIRIT_STONE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_SPIRIT_STONE_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));

        register(context, REALGAR_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_REALGAR_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));

        register(context, CINNABAR_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_CINNABAR_ORE_KEY),
                ModOrePlacement.commonOrePlacement(20,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(256))));

        register(context, SULFUR_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_SULFUR_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(12))));

        register(context, MICA_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_MICA_ORE_KEY),
                ModOrePlacement.commonOrePlacement(30,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256))));

        register(context, NETHER_SULFUR_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.NETHER_SULFUR_ORE_KEY),
                ModOrePlacement.commonOrePlacement(25,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(256))));

        register(context, OVERWORLD_GINSENG_CROP_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_GINSENG_CROP_KEY),
                List.of(RarityFilter.onAverageOnceEvery(52), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, OVERWORLD_BLOOD_BERRY_CROP_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_BLOOD_BERRY_CROP_KEY),
                List.of(RarityFilter.onAverageOnceEvery(52), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, OVERWORLD_GROUND_MOSS_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_GROUND_MOSS_KEY),
                List.of(RarityFilter.onAverageOnceEvery(40), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(128)), BiomeFilter.biome()));

        register(context, OVERWORLD_STONE_FLOWER_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_STONE_FLOWER_KEY),
                List.of(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(128)), BiomeFilter.biome()));

        register(context, NETHER_FLARE_FRUIT_CROP_PLACED_KEY,configuredFeatures.getOrThrow(ModConfigureFeatures.NETHER_FLARE_FRUIT_CROP_KEY),
                List.of(RarityFilter.onAverageOnceEvery(52), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(128)), BiomeFilter.biome()));

        register(context, NETHER_SUN_FLOWER_CROP_PLACED_KEY,configuredFeatures.getOrThrow(ModConfigureFeatures.NETHER_SUN_FLOWER_CROP_KEY),
                List.of(RarityFilter.onAverageOnceEvery(52), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(128)), BiomeFilter.biome()));

        register(context, OVERWORLD_BLOOD_VINE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_BLOOD_VINE_KEY),
                List.of(CountPlacement.of(100), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(128)), BiomeFilter.biome()));

        register(context, OVERWORLD_SNOW_LOTUS_CROP_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_SNOW_LOTUS_CROP_KEY),
                List.of(RarityFilter.onAverageOnceEvery(52), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, OVERWORLD_SNOW_GINSENG_CROP_PLACED_KEY, configuredFeatures.getOrThrow(ModConfigureFeatures.OVERWORLD_SNOW_GINSENG_CROP_KEY),
                List.of(RarityFilter.onAverageOnceEvery(52), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
