package com.hughie.dandao.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.util.MedicinalProperties;
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

public class AlchemyFurnaceRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final JsonObject medicinalProperties = new JsonObject();

    private AlchemyFurnaceRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
    }

    public static AlchemyFurnaceRecipeBuilder alchemyFurnace(RecipeCategory category, ItemLike result, int count) {
        return new AlchemyFurnaceRecipeBuilder(category, result, count);
    }

    public AlchemyFurnaceRecipeBuilder requires(ItemLike item) {
        return this.requires(Ingredient.of(item));
    }

    public AlchemyFurnaceRecipeBuilder requires(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public AlchemyFurnaceRecipeBuilder medicinalProperty(MedicinalProperties medProp, int min, int max, float time, int heat) {
        if (min > max) {
            throw new IllegalArgumentException("min level is over the max level");
        }

        if (min < medProp.getMinLevel() || max > medProp.getMaxLevel()) {
            throw new IllegalArgumentException("medicinal property level is out of range");
        }

        if (heat < 0 || heat > 3) {
            throw new IllegalArgumentException("heat must be in range 0 to 3");
        }

        JsonObject prop = new JsonObject();
        prop.addProperty("min", min);
        prop.addProperty("max", max);
        prop.addProperty("time", time);
        prop.addProperty("heat", heat);
        this.medicinalProperties.add(medProp.getName(), prop);
        return this;
    }

    @Override
    public AlchemyFurnaceRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterion) {
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
        String recipePath = defaultId.getPath() + "_from_alchemy_furnace";
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
                json.addProperty("type", "dandao:alchemy_furnace_recipe");

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

                // 写入药用属性
                json.add("medicinal_properties", medicinalProperties);
            }
            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ModRecipes.ALCHEMY_FURNACE_SERIALIZER.get();
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
