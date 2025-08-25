package com.hughie.dandao.common.compat.jei;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.recipe.BambooSieveRecipe;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.hughie.dandao.common.util.MedicinalPropertiesNBT;
import com.mojang.datafixers.util.Pair;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class BambooSieveCategory implements IRecipeCategory<BambooSieveRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "bamboo_sieve_recipe");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID,
            "textures/gui/bamboo_sieve_gui.png");
    public static final RecipeType<BambooSieveRecipe> BAMBOO_SIEVE_RECIPE_TYPE =
            new RecipeType<>(UID, BambooSieveRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public BambooSieveCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 32, 6, 119, 64);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.BAMBOO_SIEVE.get()));
    }

    @Override
    public RecipeType<BambooSieveRecipe> getRecipeType() {
        return BAMBOO_SIEVE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.dandao.bamboo_sieve");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BambooSieveRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 13, 33);
        IRecipeSlotBuilder sideInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 46, 7);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 88, 32).addItemStack(recipe.getResultItem(null));

        Pair<MedicinalProperties, Integer> properties = recipe.getMedicinalProperties();

        ItemStack input = new ItemStack(ModItems.MEDICINAL_POWDER.get());

        MedicinalPropertiesNBT.setPropertyLevel(input, properties.getFirst(), properties.getSecond());

        inputSlot.addItemStack(input);

        ItemStack sideInput =  recipe.getIngredients().get(1).getItems()[0];
        if (sideInput.is(Items.HONEY_BOTTLE)) {
            sideInputSlot.addIngredients(recipe.getIngredients().get(1));
        }else {
            ItemStack waterBottle = new ItemStack(Items.POTION);
            waterBottle.getOrCreateTag().putString("Potion", "minecraft:water");
            sideInputSlot.addItemStack(waterBottle);
        }
    }
}
