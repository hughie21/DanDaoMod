package com.hughie.dandao.common.compat.jei;


import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.recipe.AlchemyFurnaceRecipe;
import com.hughie.dandao.common.recipe.BambooSieveRecipe;
import com.hughie.dandao.common.recipe.MortarRecipe;
import com.hughie.dandao.common.screen.AlchemyFurnaceScreen;
import com.hughie.dandao.common.screen.BambooSieveScreen;
import com.hughie.dandao.common.screen.MortarScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID,"jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MortarCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AlchemyFurnaceCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BambooSieveCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            RecipeManager manager = minecraft.level.getRecipeManager();

            List<MortarRecipe> mortarRecipeList = manager.getAllRecipesFor(MortarRecipe.Type.INSTANCE);
            registration.addRecipes(MortarCategory.MORTAR_RECIPE_TYPE, mortarRecipeList);

            List<AlchemyFurnaceRecipe> alchemyFurnaceRecipeList = manager.getAllRecipesFor(AlchemyFurnaceRecipe.Type.INSTANCE);
            registration.addRecipes(AlchemyFurnaceCategory.ALCHEMY_FURNACE_RECIPE_TYPE, alchemyFurnaceRecipeList);

            List<BambooSieveRecipe> bambooSieveRecipeList = manager.getAllRecipesFor(BambooSieveRecipe.Type.INSTANCE);
            registration.addRecipes(BambooSieveCategory.BAMBOO_SIEVE_RECIPE_TYPE, bambooSieveRecipeList);
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MortarScreen.class, 60, 30, 20, 30,
                MortarCategory.MORTAR_RECIPE_TYPE);

        registration.addRecipeClickArea(AlchemyFurnaceScreen.class, 60, 30, 20, 30,
                AlchemyFurnaceCategory.ALCHEMY_FURNACE_RECIPE_TYPE);

        registration.addRecipeClickArea(BambooSieveScreen.class, 60, 30, 20, 30,
                BambooSieveCategory.BAMBOO_SIEVE_RECIPE_TYPE);
    }
}
