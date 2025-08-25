package com.hughie.dandao.setup.dataGen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {

    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DanDao.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SPIRIT_STONE_ORE.get())
                .add(ModBlocks.CINNABAR_ORE.get())
                .add(ModBlocks.REALGAR_ORE.get())
                .add(ModBlocks.MICA_ORE.get())
                .add(ModBlocks.SULFUR_ORE.get())
                .add(ModBlocks.ALCHEMY_FURNACE.get())
                .add(ModBlocks.DEEPSLATE_SULFUR_ORE.get())
                .add(ModBlocks.DEEPSLATE_CINNABAR_ORE.get())
                .add(ModBlocks.DEEPSLATE_REALGAR_ORE.get())
                .add(ModBlocks.DEEPSLATE_SPIRIT_STONE_ORE.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.SPIRIT_STONE_ORE.get())
                .add(ModBlocks.CINNABAR_ORE.get())
                .add(ModBlocks.REALGAR_ORE.get())
                .add(ModBlocks.SULFUR_ORE.get())
                .add(ModBlocks.DEEPSLATE_SULFUR_ORE.get())
                .add(ModBlocks.DEEPSLATE_CINNABAR_ORE.get())
                .add(ModBlocks.DEEPSLATE_REALGAR_ORE.get())
                .add(ModBlocks.DEEPSLATE_SPIRIT_STONE_ORE.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.MICA_ORE.get());
    }
}
