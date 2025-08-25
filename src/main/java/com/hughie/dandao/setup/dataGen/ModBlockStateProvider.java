package com.hughie.dandao.setup.dataGen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.block.custom.*;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;


public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DanDao.MOD_ID, exFileHelper);
    }

    private final ModelFile CrossCropTemplate = new ModelFile.UncheckedModelFile(modLoc("block/crop_cross"));
    private final ModelFile CrossBlockTemplate = new ModelFile.UncheckedModelFile(modLoc("block/cross"));
    private final ModelFile BlockCropTemplate = new ModelFile.UncheckedModelFile(modLoc("block/block_crop"));
    private final ModelFile FlowerCropTemplate = new ModelFile.UncheckedModelFile(modLoc("block/flower_crop"));


    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SPIRIT_STONE_ORE);
        putuanBlock(ModBlocks.PUTUAN);
        simpleBlockWithItem(ModBlocks.MORTAR.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/mortar")) {
        });
        blockWithItem(ModBlocks.REALGAR_ORE);
        blockWithItem(ModBlocks.CINNABAR_ORE);
        blockWithItem(ModBlocks.MICA_ORE);
        blockWithItem(ModBlocks.SULFUR_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SULFUR_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_CINNABAR_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_REALGAR_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SPIRIT_STONE_ORE);
        blockWithItem(ModBlocks.NETHER_SULFUR_ORE);

        String reactorPath = ModBlocks.REACTOR.getId().getPath();
        ModelFile reactorModel = models()
                .withExistingParent(reactorPath, "block/orientable")
                .texture("front", modLoc("block/reactor_front"))
                .texture("side", modLoc("block/reactor_side"))
                .texture("top", modLoc("block/reactor_side"));
        horizontalBlock(ModBlocks.REACTOR.get(), reactorModel);
        simpleBlockItem(ModBlocks.REACTOR.get(), reactorModel);

        simpleBlock(ModBlocks.ALCHEMY_FURNACE.get(), new ModelFile.UncheckedModelFile(modLoc("block/alchemy_furnace")));

        simpleBlock(ModBlocks.IRON_MILL.get(), new ModelFile.UncheckedModelFile(modLoc("block/iron_mill_particle")));
        simpleBlockItem(ModBlocks.IRON_MILL.get(),
                models().withExistingParent("iron_mill", "item/handheld")
                        .texture("layer0", "item/iron_mill").texture("particle","block/iron_mill"));

        simpleBlock(ModBlocks.KNEADING_BOARD.get(), new ModelFile.UncheckedModelFile(modLoc("block/kneading_board_particle")));
        simpleBlockItem(ModBlocks.KNEADING_BOARD.get(),
                models().withExistingParent("kneading_board", "item/handheld")
                        .texture("layer0", "item/kneading_board").texture("particle","block/kneading_board"));

        createCropBlockStates(ModBlocks.GINSENG_CROP.get(), GinsengCropBlock.AGE,"ginseng_stage", "ginseng_stage", CrossCropTemplate);
        createCropBlockStates(ModBlocks.BLOOD_BERRY_CROP.get(), BloodBerryCropBlock.AGE, "blood_berry_stage", "blood_berry_stage", CrossCropTemplate);

        createVineBlockStates(ModBlocks.BLOOD_VINES, "block/blood_vine", false);
        createVineBlockStates(ModBlocks.BLOOD_VINES_PLANT, "block/blood_vine_plant", true);

        createCropBlockStates(ModBlocks.GROUND_MOSS.get(), GroundMossCropBlock.AGE, "ground_moss_stage", "ground_moss_stage", "ground_moss_side", BlockCropTemplate);

        createCropBlockStates(ModBlocks.STONE_FLOWER_CROP.get(), StoneFlowerCropBlock.AGE,"stone_flower_stage", "stone_flower_stage", CrossCropTemplate);

        createCropBlockStates(ModBlocks.FLARE_FRUIT_CROP.get(), FlareFruitCropBlock.AGE, "flare_fruit_stage","flare_fruit_stage", CrossCropTemplate);

        createCropBlockStates(ModBlocks.SUN_FLOWER_CROP.get(), SunFlowerCropBlock.AGE, "sun_flower_stage", "sun_flower_stage", FlowerCropTemplate);

        createCropBlockStates(ModBlocks.SNOW_LOTUS_CROP.get(), SnowLotusCropBlock.AGE, "snow_lotus_stage", "snow_lotus_stage", CrossCropTemplate);

        createCropBlockStates(ModBlocks.SNOW_GINSENG_CROP.get(), SnowGinsengCropBlock.AGE, "snow_ginseng_stage", "snow_ginseng_stage", CrossCropTemplate);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void createVineBlockStates(RegistryObject<Block> blockRegistryObject, String textureName, boolean isItem) {
        ModelFile model = models().withExistingParent(blockRegistryObject.getId().getPath(), "block/block").parent(CrossBlockTemplate).texture("cross", textureName).renderType("cutout");
        if(isItem) {
            simpleBlock(blockRegistryObject.get(), model);
            itemModels().getBuilder(blockRegistryObject.getId().getPath())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, textureName));
        }else  {
            simpleBlock(blockRegistryObject.get(), model);
        }
    }

    public void createCropBlockStates(Block pBlock, IntegerProperty pCropAgeProperty, String pModelName, String pTextureName, ModelFile model) {
        getVariantBuilder(pBlock).forAllStates(state ->
                generateCropModel(state, pCropAgeProperty, pModelName, pTextureName, model));
    }

    public void createCropBlockStates(Block pBlock, IntegerProperty pCropAgeProperty, String pModelName, String TextureNameTop, String TextureNameSide, ModelFile model) {
        getVariantBuilder(pBlock).forAllStates(state ->
                generateCropModel(state, pCropAgeProperty, pModelName, TextureNameTop, TextureNameSide, model));
    }

    private ConfiguredModel[] generateCropModel(BlockState state, IntegerProperty pCropAgeProperty, String modelName, String textureName, ModelFile model) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(pCropAgeProperty),
                ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "block/" + textureName + state.getValue(pCropAgeProperty))).renderType("cutout").parent(model));
        return models;
    }

    private ConfiguredModel[] generateCropModel(BlockState state, IntegerProperty pCropAgeProperty, String modelName, String textureNameTop,String textureNameSide , ModelFile model) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(pCropAgeProperty),
                ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "block/" + textureNameTop + state.getValue(pCropAgeProperty))).renderType("cutout").parent(model).texture("crop_side", ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "block/" + textureNameSide)));
        return models;
    }

    private void putuanBlock(RegistryObject<Block> blockRegistryObject) {
        Block block = blockRegistryObject.get();

        String textureTop = "block/putuan_top";
        String textureSide = "block/putuan_side";
        String textureBottom = "block/putuan_top";

        ModelFile model = models().withExistingParent(blockRegistryObject.getId().getPath(), "block/block")
                .texture("top", textureTop)
                .texture("side", textureSide)
                .texture("bottom", textureBottom)
                .texture("particle", textureSide)
                // 添加元素
                .element()
                .from(1, 0, 1)
                .to(15, 4, 15)
                // 下表面
                .face(Direction.DOWN)
                .texture("#bottom")
                .cullface(Direction.DOWN)
                .uvs(1, 1, 15, 15)
                .end()
                // 上表面
                .face(Direction.UP)
                .texture("#top")
                .uvs(1, 1, 15, 15)
                .end()
                // 北表面
                .face(Direction.NORTH)
                .texture("#side")
                .uvs(1, 12, 15, 16)
                .end()
                // 南表面
                .face(Direction.SOUTH)
                .texture("#side")
                .uvs(1, 12, 15, 16)
                .end()
                // 西表面
                .face(Direction.WEST)
                .texture("#side")
                .uvs(1, 12, 15, 16)
                .end()
                // 东表面
                .face(Direction.EAST)
                .texture("#side")
                .uvs(1, 12, 15, 16)
                .end()
                .end();
        simpleBlockWithItem(block, model);
    }
}
