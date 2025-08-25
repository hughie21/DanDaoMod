package com.hughie.dandao.common.block;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.custom.*;
import com.hughie.dandao.common.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DanDao.MOD_ID);

    public static final RegistryObject<Block> SPIRIT_STONE_ORE = registerBlock("spirit_stone_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3f, 1.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> DEEPSLATE_SPIRIT_STONE_ORE = registerBlock("deepslate_spirit_stone_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.5f, 2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> CINNABAR_ORE = registerBlock("cinnabar_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> DEEPSLATE_CINNABAR_ORE = registerBlock("deepslate_cinnabar_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.5f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> REALGAR_ORE = registerBlock("realgar_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> DEEPSLATE_REALGAR_ORE = registerBlock("deepslate_realgar_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.5f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> MICA_ORE = registerBlock("mica_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> SULFUR_ORE = registerBlock("sulfur_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> DEEPSLATE_SULFUR_ORE = registerBlock("deepslate_sulfur_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.5f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> NETHER_SULFUR_ORE = registerBlock("nether_sulfur_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> PUTUAN = registerBlock("putuan",
            ()-> new PutuanBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .sound(SoundType.WOOL)
            ));

    public static final RegistryObject<Block> MORTAR = registerBlock("mortar",
            ()-> new MortarBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> IRON_MILL = registerBlock("iron_mill",
            ()-> new IronMillBlock(BlockBehaviour.Properties.of()
                    .strength(1f, 0.5f)
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> KNEADING_BOARD = registerBlock("kneading_board",
            ()-> new KneadingBoardBlock(BlockBehaviour.Properties.of()
                    .strength(1f, 0.5f)
                    .sound(SoundType.WOOD)
            ));

    public static final RegistryObject<Block> INCENSE_BURNER = registerBlock("incense_burner",
            ()-> new IncenseBurnerBlock(BlockBehaviour.Properties.of()
                    .strength(1f, 0.5f)
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> ALCHEMY_FURNACE = registerBlock("alchemy_furnace", AlchemyFurnaceBlock::new);

    public static final RegistryObject<Block> REACTOR = registerBlock("reactor",
            ()-> new ReactorBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.STONE))
    );

    public static final RegistryObject<Block> GINSENG_CROP = BLOCKS.register("ginseng_crop",
            ()-> new GinsengCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> WILD_GINSENG_CROP = BLOCKS.register("wild_ginseng_crop",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> BLOOD_BERRY_CROP = BLOCKS.register("blood_berry_crop",
            ()-> new BloodBerryCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> WILD_BLOOD_BERRY_CROP = BLOCKS.register("wild_blood_berry_crop",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> BLOOD_VINES = BLOCKS.register("blood_vine",
            ()-> new BloodVineBlock(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES)
                    .noCollission()
                    .noOcclusion()
            ));
    public static final RegistryObject<Block> BLOOD_VINES_PLANT = BLOCKS.register("blood_vine_plant",
            ()-> new BloodVinePlantBlock(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> GROUND_MOSS = BLOCKS.register("ground_moss",
            ()-> new GroundMossCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> STONE_FLOWER_CROP = BLOCKS.register("stone_flower_crop",
            ()-> new StoneFlowerCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> FLARE_FRUIT_CROP = BLOCKS.register("flare_fruit_crop",
            ()-> new FlareFruitCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> SUN_FLOWER_CROP = BLOCKS.register("sun_flower_crop",
            ()-> new SunFlowerCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));
    public static final RegistryObject<Block> SNOW_LOTUS_CROP = BLOCKS.register("snow_lotus_crop",
            ()-> new SnowLotusCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> SNOW_GINSENG_CROP = BLOCKS.register("snow_ginseng_crop",
            ()-> new SnowGinsengCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)
                    .noCollission()
                    .noOcclusion()
            ));

    public static final RegistryObject<Block> BAMBOO_SIEVE_VIRTUAL_BLOCK = BLOCKS.register("bamboo_sieve_virtual_block",
            ()-> new BambooSieveVirtualBlock(BlockBehaviour.Properties.copy(Blocks.AIR).noCollission().noOcclusion().noLootTable()));

    // 注册方法
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
