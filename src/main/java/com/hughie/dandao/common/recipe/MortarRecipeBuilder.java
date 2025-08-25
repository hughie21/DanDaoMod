package com.hughie.dandao.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hughie.dandao.DanDao;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MortarRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    private MortarRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
    }

    public static MortarRecipeBuilder mortar(RecipeCategory category, ItemLike result, int count) {
        return new MortarRecipeBuilder(category, result, count);
    }

    public MortarRecipeBuilder requires(ItemLike item) {
        return this.requires(Ingredient.of(item));
    }

    public MortarRecipeBuilder requires(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    @Override
    public MortarRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterion) {
        this.advancement.addCriterion(criterionName, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@org.jetbrains.annotations.Nullable String p_176495_) {
        return null;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer) {
        ResourceLocation defaultId = BuiltInRegistries.ITEM.getKey(result);
        String recipePath = defaultId.getPath() + "_from_mortar";
        this.save(consumer, ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, recipePath));
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.advancement.parent(ResourceLocation.parse("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        consumer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                // 写入配方类型
                json.addProperty("type", "dandao:mortar_recipe");

                // 写入原料
                JsonArray ingredientsArray = new JsonArray();
                for (Ingredient ingredient : ingredients) {
                    ingredientsArray.add(ingredient.toJson());
                }
                json.add("ingredients", ingredientsArray);

                // 写入输出
                JsonObject output = new JsonObject();
                output.addProperty("item", BuiltInRegistries.ITEM.getKey(result).toString());
                output.addProperty("count", count);
                json.add("output", output);
            }
            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ModRecipes.BAMBOO_SIEVE_SERIALIZER.get();
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return advancement.serializeToJson();
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "recipes/" + category.getFolderName() + "/" + id.getPath());
            }
        });
    }
}
