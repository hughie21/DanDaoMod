package com.hughie.dandao.setup.dataGen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.setup.worldGen.ModBiomeModifiers;
import com.hughie.dandao.setup.worldGen.ModConfigureFeatures;
import com.hughie.dandao.setup.worldGen.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfigureFeatures::boostrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::boostrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(DanDao.MOD_ID));
    }
}
