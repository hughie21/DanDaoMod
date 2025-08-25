package com.hughie.dandao.common.recipe;

import com.hughie.dandao.DanDao;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DanDao.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, DanDao.MOD_ID);

    public static final RegistryObject<RecipeSerializer<MortarRecipe>> MORTAR_SERIALIZER =
            SERIALIZERS.register("mortar_recipe", () -> MortarRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<MortarRecipe>> MORTAR_RECIPE_TYPE =
            RECIPE_TYPE.register("mortar_recipe", () -> MortarRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<AlchemyFurnaceRecipe>> ALCHEMY_FURNACE_SERIALIZER =
            SERIALIZERS.register("alchemy_furnace_recipe", () -> AlchemyFurnaceRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<AlchemyFurnaceRecipe>> ALCHEMY_FURNACE_RECIPE_TYPE =
            RECIPE_TYPE.register("alchemy_furnace_recipe", () -> AlchemyFurnaceRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<BambooSieveRecipe>> BAMBOO_SIEVE_SERIALIZER =
            SERIALIZERS.register("bamboo_sieve_recipe", () -> BambooSieveRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<BambooSieveRecipe>> BAMBOO_SIEVE_RECIPE_TYPE =
            RECIPE_TYPE.register("bamboo_sieve_recipe", () -> BambooSieveRecipe.Type.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        RECIPE_TYPE.register(eventBus);
    }
}
