package com.hughie.dandao.setup.dataGen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_,  @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, DanDao.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.HERBS)
        .add(ModItems.GINSENG.get())
        .add(ModItems.BLOOD_BERRY.get())
        .add(ModItems.BLOOD_VINES_ITEM.get())
        .add(ModItems.GROUND_MOSS.get())
        .add(ModItems.STONE_FLOWER.get())
        .add(ModItems.FLARE_FRUIT.get())
        .add(ModItems.SUN_LEAF.get())
        .add(ModItems.SNOW_LOTUS.get())
        .add(ModItems.SNOW_GINSENG.get())
        .add(ModItems.PROCESSED_GINSENG.get())
        .add(ModItems.PROCESSED_BLOOD_BERRY.get())
        .add(ModItems.PROCESSED_BLOOD_VINES_ITEM.get())
        .add(ModItems.PROCESSED_GROUND_MOSS.get())
        .add(ModItems.PROCESSED_STONE_FLOWER.get())
        .add(ModItems.PROCESSED_FLARE_FRUIT.get())
        .add(ModItems.PROCESSED_SUN_LEAF.get())
        .add(ModItems.PROCESSED_SNOW_LOTUS.get())
        .add(ModItems.PROCESSED_SNOW_GINSENG.get())
        .add(ModItems.MEDICINAL_POWDER.get());

        this.tag(ModTags.MINERAL)
        .add(ModItems.CINNABAR_POWDER.get())
        .add(ModItems.COAL_POWDER.get())
        .add(ModItems.ORPIMENT_POWDER.get())
        .add(ModItems.REALGAR_POWDER.get())
        .add(ModItems.CINNABAR_POWDER.get())
        .add(ModItems.ASADIN.get())
        .add(ModItems.SULFUR_POWDER.get())
        .add(ModItems.MICA.get())
        .add(ModItems.LIME.get())
        .add(ModItems.SULFUR.get())
        .add(ModItems.CINNABAR.get())
        .add(ModItems.REALGAR.get())
        .add(ModItems.ORPIMENT.get())
        .add(ModItems.MEDICINAL_POWDER.get());

        this.tag(ModTags.SUPPORT_POTION)
        .add(Items.HONEY_BOTTLE)
        .add(Items.DRAGON_BREATH)
        .add(Items.POTION);

        this.tag(ModTags.FUELS_V1)
                .addTag(ItemTags.WOOL_CARPETS)
                .add(Items.SCAFFOLDING)
                .add(Items.BAMBOO)
                .addTag(ItemTags.WOODEN_DOORS)
                .addTag(ItemTags.SIGNS)
                .add(Items.WOODEN_SWORD)
                .add(Items.WOODEN_AXE)
                .add(Items.WOODEN_HOE)
                .add(Items.WOODEN_PICKAXE)
                .add(Items.WOODEN_SHOVEL)
                .add(Items.BAMBOO_DOOR)
                .add(Items.BAMBOO_SIGN)
                .add(Items.AZALEA)
                .addTag(ItemTags.WOOL)
                .add(Items.STICK)
                .addTag(ItemTags.SAPLINGS)
                .add(Items.BOWL)
                .addTag(ItemTags.BUTTONS)
                .addTag(ItemTags.WOODEN_SLABS)
                .add(Items.BAMBOO_SLAB)
                .add(Items.BAMBOO_MOSAIC_SLAB);

        this.tag(ModTags.FUELS_V2)
                .add(Items.CHISELED_BOOKSHELF)
                .add(Items.MANGROVE_ROOTS)
                .add(Items.LADDER)
                .add(Items.FISHING_ROD)
                .add(Items.CROSSBOW)
                .add(Items.BOW)
                .addTag(ItemTags.BANNERS)
                .add(Items.NOTE_BLOCK)
                .add(Items.JUKEBOX)
                .add(Items.DAYLIGHT_DETECTOR)
                .add(Items.BARREL)
                .add(Items.TRAPPED_CHEST)
                .add(Items.CHEST)
                .add(Items.COMPOSTER)
                .add(Items.LECTERN)
                .add(Items.BOOKSHELF)
                .add(Items.LOOM)
                .add(Items.SMITHING_TABLE)
                .add(Items.FLETCHING_TABLE)
                .add(Items.CARTOGRAPHY_TABLE)
                .add(Items.CRAFTING_TABLE)
                .addTag(ItemTags.WOODEN_TRAPDOORS)
                .add(Items.BAMBOO_TRAPDOOR)
                .addTag(ItemTags.WOODEN_STAIRS)
                .add(Items.BAMBOO_STAIRS)
                .add(Items.BAMBOO_MOSAIC_STAIRS)
                .addTag(ItemTags.WOODEN_FENCES)
                .addTag(ItemTags.FENCE_GATES)
                .addTag(ItemTags.WOODEN_PRESSURE_PLATES)
                .addTag(ItemTags.PLANKS)
                .add(Items.BAMBOO_PLANKS)
                .add(Items.BAMBOO_MOSAIC)
                .addTag(ItemTags.LOGS)
                .add(Items.BAMBOO_BLOCK)
                .add(Items.BAMBOO_PRESSURE_PLATE);

        this.tag(ModTags.FUELS_V4)
                .add(Items.BAMBOO_HANGING_SIGN)
                .addTag(ItemTags.HANGING_SIGNS);

        this.tag(ModTags.FUELS_V6)
                .addTag(ItemTags.CHEST_BOATS)
                .addTag(ItemTags.BOATS)
                .add(Items.DRIED_KELP_BLOCK);

        this.tag(ModTags.FUELS_V20)
                .add(Items.COAL)
                .add(Items.CHARCOAL);

        this.tag(ModTags.FUELS_V50)
                .add(Items.BLAZE_ROD);

        this.tag(ModTags.FUELS_V80)
                .add(Items.COAL_BLOCK);

        this.tag(ModTags.FUELS_V100)
                .add(Items.LAVA_BUCKET);

        this.tag(ModTags.FUELS)
                .addTag(ModTags.FUELS_V1)
                .addTag(ModTags.FUELS_V2)
                .addTag(ModTags.FUELS_V4)
                .addTag(ModTags.FUELS_V6)
                .addTag(ModTags.FUELS_V20)
                .addTag(ModTags.FUELS_V50)
                .addTag(ModTags.FUELS_V80)
                .addTag(ModTags.FUELS_V100);

        this.tag(ModTags.COOLING_V20)
                .add(Items.POTION)
                .add(Items.SNOWBALL);

        this.tag(ModTags.COOLING_V50)
                .add(Items.SNOW_BLOCK)
                .add(Items.POWDER_SNOW_BUCKET);

        this.tag(ModTags.COOLING_V100)
                .add(Items.WATER_BUCKET)
                .add(Items.ICE)
                .add(Items.PACKED_ICE)
                .add(Items.BLUE_ICE);

        this.tag(ModTags.COOLING)
                .addTag(ModTags.COOLING_V20)
                .addTag(ModTags.COOLING_V50)
                .addTag(ModTags.COOLING_V100);

        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.TAOIST_ROBE_HELMET.get(), ModItems.TAOIST_ROBE_CHESTPLATE.get(), ModItems.TAOIST_ROBE_LEGGINGS.get(),ModItems.TAOIST_ROBE_BOOTS.get());
    }
}
