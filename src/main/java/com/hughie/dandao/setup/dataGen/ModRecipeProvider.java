package com.hughie.dandao.setup.dataGen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.recipe.AlchemyFurnaceRecipeBuilder;
import com.hughie.dandao.common.recipe.BambooSieveRecipeBuilder;
import com.hughie.dandao.common.recipe.MortarRecipeBuilder;
import com.hughie.dandao.common.util.MedicinalProperties;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> SPIRIT_MINE_SMELTABLE = List.of(ModBlocks.SPIRIT_STONE_ORE.get(), ModBlocks.DEEPSLATE_SPIRIT_STONE_ORE.get());
    private static final List<ItemLike> ORPIMENT_SMELTABLE = List.of(ModItems.ORPIMENT_POWDER.get());
    private static final List<ItemLike> LIME_SMELTABLE = List.of(Items.BONE_MEAL);
    private static final List<ItemLike> MICA_ORE_SMELTABLE = List.of(ModBlocks.MICA_ORE.get());
    private static final List<ItemLike> SULFUR_SMELTABLE = List.of(ModBlocks.SULFUR_ORE.get(), ModBlocks.DEEPSLATE_SULFUR_ORE.get(), ModBlocks.NETHER_SULFUR_ORE.get());
    private static final List<ItemLike> REALGAR_ORE_SMELTABLE = List.of(ModBlocks.REALGAR_ORE.get(), ModBlocks.DEEPSLATE_REALGAR_ORE.get());
    private static final List<ItemLike> CINNABAR_ORE_SMELTABLE = List.of(ModBlocks.CINNABAR_ORE.get(), ModBlocks.DEEPSLATE_CINNABAR_ORE.get());

    public ModRecipeProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreBlasting(pWriter, SPIRIT_MINE_SMELTABLE, RecipeCategory.MISC, ModItems.SPIRIT_STONE.get(), 0.25f, 100, "ling_ore");
        oreSmelting(pWriter, SPIRIT_MINE_SMELTABLE, RecipeCategory.MISC, ModItems.SPIRIT_STONE.get(), 0.25f, 200, "ling_ore");

        oreBlasting(pWriter, ORPIMENT_SMELTABLE, RecipeCategory.MISC, ModItems.ASADIN.get(), 0.5f, 150, "asadin");
        oreSmelting(pWriter, ORPIMENT_SMELTABLE, RecipeCategory.MISC, ModItems.ASADIN.get(), 0.5f, 250, "asadin");

        oreBlasting(pWriter, LIME_SMELTABLE, RecipeCategory.MISC, ModItems.LIME.get(), 0.25f, 50, "lime");
        oreSmelting(pWriter, LIME_SMELTABLE, RecipeCategory.MISC, ModItems.LIME.get(), 0.25f, 150, "lime");

        oreBlasting(pWriter, MICA_ORE_SMELTABLE, RecipeCategory.MISC, ModItems.MICA.get(), 0.25f, 50, "mica");
        oreSmelting(pWriter, MICA_ORE_SMELTABLE, RecipeCategory.MISC, ModItems.MICA.get(), 0.25f, 150, "mica");

        oreBlasting(pWriter, SULFUR_SMELTABLE, RecipeCategory.MISC, ModItems.SULFUR.get(), 0.25f, 50, "sulfur");
        oreSmelting(pWriter, SULFUR_SMELTABLE, RecipeCategory.MISC, ModItems.SULFUR.get(), 0.25f, 150, "sulfur");

        oreBlasting(pWriter, REALGAR_ORE_SMELTABLE, RecipeCategory.MISC, ModItems.REALGAR.get(), 0.25f, 50, "realgar");
        oreSmelting(pWriter, REALGAR_ORE_SMELTABLE, RecipeCategory.MISC, ModItems.REALGAR.get(), 0.25f, 150, "realgar");

        oreBlasting(pWriter, CINNABAR_ORE_SMELTABLE, RecipeCategory.MISC, ModItems.CINNABAR.get(), 0.25f, 50, "cinnabar");
        oreSmelting(pWriter, CINNABAR_ORE_SMELTABLE, RecipeCategory.MISC, ModItems.CINNABAR.get(), 0.25f, 150, "cinnabar");

        smoking(pWriter, ModItems.GINSENG.get(), RecipeCategory.MISC, ModItems.PROCESSED_GINSENG.get(), 0.45f, 300, "processed_herbs");
        smoking(pWriter, ModItems.BLOOD_BERRY.get(), RecipeCategory.MISC, ModItems.PROCESSED_BLOOD_BERRY.get(), 0.45f, 300, "processed_herbs");
        smoking(pWriter, ModItems.BLOOD_VINES_ITEM.get(), RecipeCategory.MISC, ModItems.PROCESSED_BLOOD_VINES_ITEM.get(), 0.45f, 300, "processed_herbs");
        smoking(pWriter, ModItems.GROUND_MOSS.get(), RecipeCategory.MISC, ModItems.PROCESSED_GROUND_MOSS.get(), 0.45f, 300, "processed_herbs");
        smoking(pWriter, ModItems.STONE_FLOWER.get(), RecipeCategory.MISC, ModItems.PROCESSED_STONE_FLOWER.get(), 0.45f, 300, "processed_herbs");
        smoking(pWriter, ModItems.FLARE_FRUIT.get(), RecipeCategory.MISC, ModItems.PROCESSED_FLARE_FRUIT.get(), 0.45f, 300, "processed_herbs");
        smoking(pWriter, ModItems.SUN_LEAF.get(), RecipeCategory.MISC, ModItems.PROCESSED_SUN_LEAF.get(), 0.45f, 300, "processed_herbs");
        smoking(pWriter, ModItems.SNOW_LOTUS.get(), RecipeCategory.MISC, ModItems.PROCESSED_SNOW_LOTUS.get(), 0.45f, 300, "processed_herbs");
        smoking(pWriter, ModItems.SNOW_GINSENG.get(), RecipeCategory.MISC, ModItems.PROCESSED_SNOW_GINSENG.get(), 0.45f, 300, "processed_herbs");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLOOD_BERRY_SEEDS.get())
                .requires(ModItems.BLOOD_BERRY.get())
                .unlockedBy(getHasName(ModItems.BLOOD_BERRY.get()), has(ModItems.BLOOD_BERRY.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FLARE_FRUIT_SEEDS.get())
                .requires(ModItems.FLARE_FRUIT.get())
                .unlockedBy(getHasName(ModItems.FLARE_FRUIT.get()), has(ModItems.FLARE_FRUIT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.PUTUAN.get())
                .define('#', Blocks.HAY_BLOCK)
                .group("dandao")
                .pattern("###")
                .unlockedBy("has_hay_block", has(Blocks.HAY_BLOCK))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.MORTAR.get())
                .define('A', Blocks.SMOOTH_STONE)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.STICK)
                .pattern(" C ")
                .pattern("ABA")
                .pattern(" A ")
                .group("dandao")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.IRON_MILL.get())
                .define('A', Items.STICK)
                .define('B', Blocks.IRON_BLOCK)
                .define('C', Items.IRON_INGOT)
                .group("dandao")
                .unlockedBy("has_iron_block", has(Blocks.IRON_BLOCK))
                .pattern("ABA")
                .pattern("C C")
                .pattern(" C ")
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.REACTOR.get())
                .define('A', Blocks.OBSIDIAN)
                .define('B', Blocks.IRON_BLOCK)
                .define('C', ModItems.SPIRIT_STONE.get())
                .group("dandao")
                .pattern("ACA")
                .pattern("BCB")
                .pattern("ACA")
                .unlockedBy(getHasName(ModItems.SPIRIT_STONE.get()), has(ModItems.SPIRIT_STONE.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.WHISK.get())
                .define('A', Items.STICK)
                .define('B', Items.IRON_INGOT)
                .define('C', Blocks.WHITE_WOOL)
                .group("dandao")
                .pattern("  B")
                .pattern(" AC")
                .pattern("A C")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.KNEADING_BOARD.get())
                .define('#', ItemTags.WOODEN_SLABS)
                .group("dandao")
                .pattern("###")
                .pattern("   ")
                .pattern("###")
                .unlockedBy("wooden_slabs", has(ItemTags.WOODEN_SLABS))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INCENSE_STICK.get())
                .define('A', Items.STICK)
                .define('B', Items.STRING)
                .define('C', Items.ORANGE_DYE)
                .pattern("CBC")
                .pattern("CBC")
                .pattern(" A ")
                .unlockedBy(getHasName(Items.ORANGE_DYE), has(Items.ORANGE_DYE))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.INCENSE_BURNER.get())
                .define('#', Items.STONE)
                .pattern("# #")
                .pattern(" # ")
                .unlockedBy(getHasName(Items.STONE), has(Items.STONE))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BAMBOO_SIEVE.get())
                .define('A', Items.BAMBOO)
                .define('B', Items.STICK)
                .pattern("BAB")
                .pattern("AAA")
                .pattern("BAB")
                .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.TAOIST_ROBE_HELMET.get())
                .define('A', ModItems.SPIRIT_STONE.get())
                .define('B', Items.BLACK_WOOL)
                .pattern("BBB")
                .pattern("BAB")
                .unlockedBy(getHasName(ModItems.SPIRIT_STONE.get()), has(ModItems.SPIRIT_STONE.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.TAOIST_ROBE_CHESTPLATE.get())
                .define('A', ModItems.SPIRIT_STONE.get())
                .define('B', Items.CYAN_WOOL)
                .pattern("B B")
                .pattern("BAB")
                .pattern("BBB")
                .unlockedBy(getHasName(ModItems.SPIRIT_STONE.get()), has(ModItems.SPIRIT_STONE.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.TAOIST_ROBE_LEGGINGS.get())
                .define('B', Items.WHITE_WOOL)
                .pattern("B B")
                .pattern("B B")
                .pattern("B B")
                .unlockedBy(getHasName(Items.WHITE_WOOL), has(Items.WHITE_WOOL))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.TAOIST_ROBE_BOOTS.get())
                .define('B', Items.BLACK_WOOL)
                .pattern("B B")
                .pattern("B B")
                .unlockedBy(getHasName(Items.BLACK_WOOL), has(Items.BLACK_WOOL))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.FUTIDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.WARM, 3, 5, 0.5F, 0)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.HUICHUNDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.WARM, 5, 6, 2.5F, 1)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.JIEDUDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.HOT, 3, 5, 1F, 0)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.LONGLIDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.HOT, 8, 9, 3F, 3)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.SHOULIDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.HOT, 6, 7, 2.5F, 3)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.BIGUDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.WARM, 3, 5, 2F, 1)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.JULINGDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.COOL, 5, 7, 3F, 1)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.FENGXINGDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.COOL, 6, 8, 1.5F, 2)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.JIYUNDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.COLD, 8, 9, 3F, 3)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.HUOHUANDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.HOT, 8, 9, 1F, 3)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.JINHUANDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.WARM, 8, 9, 1.5F, 2)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.SHUIHUANDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.COOL, 7, 9, 2.5F, 1)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.MUHUANDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.COOL, 5, 7, 1F, 0)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.TUHUANDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.COLD, 4, 6, 2F, 2)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.FANMINGDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.COLD, 2, 4, 1F, 2)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.HUIGUANGDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.HOT, 3, 5, 3F, 3)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.XUEHUNDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.COOL, 6, 8, 2.5F, 1)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        AlchemyFurnaceRecipeBuilder.alchemyFurnace(RecipeCategory.MISC, ModItems.JIUSIPOYUANDAN.get(), 1)
                .requires(ModItems.DAN_EMBRYO.get())
                .medicinalProperty(MedicinalProperties.WARM, 7, 8, 2F, 3)
                .unlockedBy(getHasName(ModItems.DAN_EMBRYO.get()), has(ModItems.DAN_EMBRYO.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.XIAOYAOWAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.POTION)
                .medicinalProperty(MedicinalProperties.COOL, 1)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.XINGJUNSAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.POTION)
                .medicinalProperty(MedicinalProperties.WARM, 1)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.SUIXINGDAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.HONEY_BOTTLE)
                .medicinalProperty(MedicinalProperties.HOT, 2)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.HUISHENGDAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.POTION)
                .medicinalProperty(MedicinalProperties.WARM, 0)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.BIXIESAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.POTION)
                .medicinalProperty(MedicinalProperties.HOT, 0)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.HEIXIDAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.HONEY_BOTTLE)
                .medicinalProperty(MedicinalProperties.COLD, 2)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.CHIYANDAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.HONEY_BOTTLE)
                .medicinalProperty(MedicinalProperties.HOT, 3)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.NINGBIDAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.HONEY_BOTTLE)
                .medicinalProperty(MedicinalProperties.COOL, 2)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.WUSHISAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.POTION)
                .medicinalProperty(MedicinalProperties.WARM, 3)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        BambooSieveRecipeBuilder.bambooSieve(RecipeCategory.MISC, ModItems.DUANHUNSAN.get(), 1)
                .requires(ModItems.MEDICINAL_POWDER.get())
                .requires(Items.HONEY_BOTTLE)
                .medicinalProperty(MedicinalProperties.COLD, 3)
                .unlockedBy(getHasName(ModItems.MEDICINAL_POWDER.get()), has(ModItems.MEDICINAL_POWDER.get()))
                .save(pWriter);

        MortarRecipeBuilder.mortar(RecipeCategory.MISC, ModItems.CINNABAR_POWDER.get(), 1)
                .requires(ModItems.CINNABAR.get())
                .unlockedBy(getHasName(ModItems.CINNABAR.get()), has(ModItems.CINNABAR.get()))
                .save(pWriter);

        MortarRecipeBuilder.mortar(RecipeCategory.MISC, ModItems.COAL_POWDER.get(), 1)
                .requires(Items.COAL)
                .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
                .save(pWriter);

        MortarRecipeBuilder.mortar(RecipeCategory.MISC, ModItems.ORPIMENT_POWDER.get(), 1)
                .requires(ModItems.ORPIMENT_POWDER.get())
                .unlockedBy(getHasName(ModItems.ORPIMENT_POWDER.get()), has(ModItems.ORPIMENT_POWDER.get()))
                .save(pWriter);

        MortarRecipeBuilder.mortar(RecipeCategory.MISC, ModItems.REALGAR_POWDER.get(), 1)
                .requires(ModItems.REALGAR.get())
                .unlockedBy(getHasName(ModItems.REALGAR.get()), has(ModItems.REALGAR.get()))
                .save(pWriter);

        MortarRecipeBuilder.mortar(RecipeCategory.MISC, ModItems.SULFUR_POWDER.get(), 1)
                .requires(ModItems.SULFUR.get())
                .unlockedBy(getHasName(ModItems.SULFUR.get()), has(ModItems.SULFUR.get()))
                .save(pWriter);
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> finishedRecipeConsumer, List<ItemLike> input, RecipeCategory category, ItemLike output, float exp, int time, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, input, category, output, exp, time, group, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> finishedRecipeConsumer, List<ItemLike> input, RecipeCategory category, ItemLike output, float exp, int time, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, input, category, output, exp, time, group, "_from_blasting");
    }

    protected static void smoking(Consumer<FinishedRecipe> finishedRecipeConsumer, List<ItemLike> input, RecipeCategory category, ItemLike output, float exp, int time, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, input, category, output, exp, time, group, "_from_smoking");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> serializer, List<ItemLike> inputList, RecipeCategory category, ItemLike output, float exp, int time, String group, String suffix) {
        for(ItemLike itemlike : inputList) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, output, exp, time, serializer).group(group).unlockedBy(getHasName(itemlike), has(itemlike)).save(finishedRecipeConsumer, DanDao.MOD_ID + ":" + getItemName(output) + suffix + "_" + getItemName(itemlike));
        }
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> finishedRecipeConsumer, ItemLike input, RecipeCategory category, ItemLike output, float exp, int time, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, input, category, output, exp, time, group, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> finishedRecipeConsumer, ItemLike input, RecipeCategory category, ItemLike output, float exp, int time, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, input, category, output, exp, time, group, "_from_blasting");
    }

    protected static void smoking(Consumer<FinishedRecipe> finishedRecipeConsumer, ItemLike input, RecipeCategory category, ItemLike output, float exp, int time, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.SMOKING_RECIPE, input, category, output, exp, time, group, "_from_smoking");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> serializer, ItemLike input, RecipeCategory category, ItemLike output, float exp, int time, String group, String suffix) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(input), category, output, exp, time, serializer).group(group).unlockedBy(getHasName(input), has(input)).save(finishedRecipeConsumer, DanDao.MOD_ID + ":" + getItemName(output) + suffix + "_" + getItemName(input));
    }
}
