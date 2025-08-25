package com.hughie.dandao.common.compat.patchouli;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.recipe.AlchemyFurnaceRecipe;
import com.hughie.dandao.common.recipe.BambooSieveRecipe;
import com.hughie.dandao.common.recipe.ModRecipes;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.hughie.dandao.common.util.MedicinalPropertiesNBT;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;

public class BambooSieveRecipeComponent implements IComponentProcessor {
    private static final String RECIPE_ID = "recipe_id";
    private static final String OUTPUT = "output";
    private static final String INPUT = "input";
    private static final String SIDE_INPUT = "side_input";
    private @NotNull BambooSieveRecipe recipe;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        ResourceLocation recipeId = ResourceLocation.parse(variables.get(RECIPE_ID).asString());
        List<BambooSieveRecipe> allBambooSieveRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.BAMBOO_SIEVE_RECIPE_TYPE.get());
        for (BambooSieveRecipe recipe : allBambooSieveRecipes) {
            if (recipe.getId().equals(recipeId)) {
                this.recipe = recipe;
                return;
            }
        }
        this.recipe = new BambooSieveRecipe(
                NonNullList.of(Ingredient.EMPTY),
                ItemStack.EMPTY,
                ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "empty"),
                null
        );
        DanDao.LOGGER.error("bamboo sieve recipe not found: {}", recipeId);
    }

    @Override
    public IVariable process(Level level, String key) {
        if (key.equals(OUTPUT)) {
            if (recipe == null) {
                return IVariable.from(ItemStack.EMPTY);
            }
            return IVariable.from(recipe.getResultItem(level.registryAccess()));
        }

        Pair<MedicinalProperties, Integer> properties = recipe.getMedicinalProperties();
        if (properties == null) {
            return IVariable.empty();
        }
        MedicinalProperties recipeProp = properties.getFirst();
        int propLevel = properties.getSecond();
        NonNullList<Ingredient> ingredients = recipe.getIngredients();

        for (Ingredient ingredient : ingredients) {
            if (key.equals(INPUT)) {
                if(ingredient.getItems()[0].is(ModItems.MEDICINAL_POWDER.get())) {
                    ItemStack result = new ItemStack(ModItems.MEDICINAL_POWDER.get());
                    MedicinalPropertiesNBT.setPropertyLevel(result, recipeProp, propLevel);
                    return IVariable.from(result);
                }
            }else if (key.equals(SIDE_INPUT)) {
                if (ingredient.getItems()[0].is(Items.POTION)) {
                    ItemStack waterBottle = new ItemStack(Items.POTION);
                    waterBottle.getOrCreateTag().putString("Potion", "minecraft:water");
                    return IVariable.from(waterBottle);
                }else if (ingredient.getItems()[0].is(Items.HONEY_BOTTLE)) {
                    return IVariable.from(ingredient.getItems()[0]);
                }
            }
        }
        return IVariable.empty();
    }
}
