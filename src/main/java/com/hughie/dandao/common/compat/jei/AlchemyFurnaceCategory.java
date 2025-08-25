package com.hughie.dandao.common.compat.jei;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.recipe.AlchemyFurnaceRecipe;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.hughie.dandao.common.util.MedicinalPropertiesNBT;
import com.mojang.datafixers.util.Pair;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class AlchemyFurnaceCategory implements IRecipeCategory<AlchemyFurnaceRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "alchemy_furnace_recipe");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID,
            "textures/gui/jei/alchemy_furnace_gui.png");
    public static final RecipeType<AlchemyFurnaceRecipe> ALCHEMY_FURNACE_RECIPE_TYPE =
            new RecipeType<>(UID, AlchemyFurnaceRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final DynamicHeatBarDrawable heatBar;
    private final TimeTextDrawable timeText;
    private static final int HEAT_BAR_X = 8;
    private static final int HEAT_BAR_Y = 10;

    private static final int TIME_TEXT_X = 37;
    private static final int TIME_TEXT_Y = 51;

    public AlchemyFurnaceCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 128, 72);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.ALCHEMY_FURNACE_ICON.get()));
        this.heatBar = new DynamicHeatBarDrawable(TEXTURE, 0);
        this.timeText = new TimeTextDrawable();
    }

    @Override
    public RecipeType<AlchemyFurnaceRecipe> getRecipeType() {
        return ALCHEMY_FURNACE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.dandao.alchemy_furnace");
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
    public void draw(AlchemyFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        heatBar.draw(guiGraphics, HEAT_BAR_X, HEAT_BAR_Y);
        timeText.draw(guiGraphics, TIME_TEXT_X, TIME_TEXT_Y);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, AlchemyFurnaceRecipe alchemyFurnaceRecipe, IFocusGroup iFocusGroup) {
        IRecipeSlotBuilder inputBuilder = iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 30, 26);
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 104, 26).addItemStack(alchemyFurnaceRecipe.getResultItem(null));
        Pair<MedicinalProperties, AlchemyFurnaceRecipe.PropertiesData> properties = alchemyFurnaceRecipe.getMedicinalProperties();
        int requireHeat = properties.getSecond().getHeat();
        int minLevel = properties.getSecond().getMinLevel();
        int maxLevel = properties.getSecond().getMaxLevel();
        float time = properties.getSecond().getTime();

        for(int i = minLevel; i < maxLevel + 1; i++) {
            ItemStack input = new ItemStack(ModItems.DAN_EMBRYO.get());
            MedicinalPropertiesNBT.setPropertyLevel(input, properties.getFirst(), i);
            inputBuilder.addIngredients(Ingredient.of(input));
        }

        heatBar.reset(requireHeat*100);
        timeText.setText(String.format("x%.1f", time));
    }

    public static class DynamicHeatBarDrawable implements IDrawable {
        private final ResourceLocation TEXTURE;
        private int targetHeat;
        private int maxReachHeat;
        private final int maxHeat = 400;
        private long lastUpdateTime;
        private static final int UPDATE_INTERVAL = 100;
        private final int barTotalHeight = 54;

        public DynamicHeatBarDrawable(ResourceLocation texture, int targetHeat) {
            this.TEXTURE = texture;
            this.targetHeat = targetHeat;
            this.maxReachHeat = targetHeat + 100;
            this.lastUpdateTime = System.currentTimeMillis();
        }

        private void updateHeat() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime < UPDATE_INTERVAL) {
                return; // 未到更新时间，不执行
            }

            // 温度未达到目标时，每次增加5（可调整增速）
            if (targetHeat < maxReachHeat) {
                targetHeat = Math.min(targetHeat + 5, maxReachHeat);
            }

            if (targetHeat == maxReachHeat) {
                targetHeat = maxReachHeat - 100;
            }
            lastUpdateTime = currentTime;
        }

        private int getScaledHeat() {
            return targetHeat != 0 && maxReachHeat != 0 ? targetHeat * barTotalHeight / maxHeat : 0;
        }

        public void reset(int targetHeat) {
            this.targetHeat = targetHeat;
            this.maxReachHeat = targetHeat + 100;
        }

        @Override
        public int getWidth() {
            return 12;
        }

        @Override
        public int getHeight() {
            return barTotalHeight;
        }

        @Override
        public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
            updateHeat();

            int scaledHeight = getScaledHeat();
            if (scaledHeight <= 0) {
                return;
            }

            int textureY = 75 + (barTotalHeight - scaledHeight);
            int drawY = yOffset + (barTotalHeight - scaledHeight);

            guiGraphics.blit(TEXTURE, xOffset, drawY, 2, textureY, getWidth(), scaledHeight);
        }
    }

    public static class TimeTextDrawable implements IDrawable {
        private String text;
        private static final int TEXT_COLOR = 0xFFFFFFFF;

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public int getWidth() {
            if (text == null || text.isEmpty()) {
                return 0;
            }
            Font font = Minecraft.getInstance().font;
            return font.width(text);
        }

        @Override
        public int getHeight() {
            return Minecraft.getInstance().font.lineHeight;
        }

        @Override
        public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
            if (text == null || text.isEmpty()) {
                return;
            }

            Font font = Minecraft.getInstance().font;

            guiGraphics.drawString(
                    font,
                    text,
                    xOffset,
                    yOffset,
                    TEXT_COLOR
            );
        }
    }
}
