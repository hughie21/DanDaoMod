package com.hughie.dandao.common.compat.patchouli;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.recipe.AlchemyFurnaceRecipe;
import com.hughie.dandao.common.recipe.ModRecipes;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;

public class AlchemyFurnaceRecipeComponent implements IComponentProcessor {
    private static final String RECIPE_ID = "recipe_id";
    private static final String OUTPUT = "output";
    private static final String PROPERTY = "property";
    private @NotNull AlchemyFurnaceRecipe recipe;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        ResourceLocation recipeId = ResourceLocation.parse(variables.get(RECIPE_ID).asString());
        List<AlchemyFurnaceRecipe> allAlchemyFurnaceRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.ALCHEMY_FURNACE_RECIPE_TYPE.get());
        for (AlchemyFurnaceRecipe recipe : allAlchemyFurnaceRecipes) {
            if (recipe.getId().equals(recipeId)) {
                this.recipe = recipe;
                return;
            }
        }
        this.recipe = new AlchemyFurnaceRecipe(
                NonNullList.of(Ingredient.EMPTY),
                ItemStack.EMPTY,
                ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "empty"),
                null
        );
        DanDao.LOGGER.error("Alchemy furnace recipe not found: {}", recipeId);
    }


    @Override
    public @NotNull IVariable process(Level level, String key) {
        if (key.equals(OUTPUT)) {
            if (recipe == null) {
                return IVariable.from(ItemStack.EMPTY);
            }
            return IVariable.from(recipe.getResultItem(level.registryAccess()));
        }

        Pair<MedicinalProperties, AlchemyFurnaceRecipe.PropertiesData> properties = recipe.getMedicinalProperties();
        if (properties == null) {
            return IVariable.empty();
        }
        MedicinalProperties recipeProp = properties.getFirst();
        AlchemyFurnaceRecipe.PropertiesData propData = properties.getSecond();

        String propName = recipeProp.getName();

        if(key.equals(PROPERTY)) {
            Component c = Component.translatable("patchouli.dandao.book.template.alchemy_furnace", Component.translatable("properties.dandao."+ propName +"_name"), propData.getMinLevel() + 1, propData.getMaxLevel() + 1, propData.getHeat() + 1, propData.getTime());
            return IVariable.wrap(c.getString());
        }

        return IVariable.empty();
    }
}
