package com.hughie.dandao.common.compat.jei;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.recipe.MortarRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MortarCategory implements IRecipeCategory<MortarRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "mortar_recipe");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID,
            "textures/gui/mortar_gui.png");
    public static final RecipeType<MortarRecipe> MORTAR_RECIPE_TYPE =
            new RecipeType<>(UID, MortarRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public MortarCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MORTAR.get()));
    }

    @Override
    public RecipeType<MortarRecipe> getRecipeType() {
        return MORTAR_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.dandao.mortar");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, MortarRecipe mortarRecipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 76, 7).addIngredients(mortarRecipe.getIngredients().get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 76, 55).addItemStack(mortarRecipe.getResultItem(null));
    }
}
