package com.hughie.dandao.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class BambooSieveRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack outPut;
    private final ResourceLocation id;
    private final Pair<MedicinalProperties, Integer> medicinalProperties;

    public BambooSieveRecipe(NonNullList<Ingredient> inputItems, ItemStack outPut,
                                ResourceLocation id, Pair<MedicinalProperties, Integer> medicinalProperties) {
        this.inputItems = inputItems;
        this.outPut = outPut;
        this.id = id;
        this.medicinalProperties = medicinalProperties;
    }

    public Pair<MedicinalProperties, Integer> getMedicinalProperties() {
        return medicinalProperties;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level level) {
        if (level.isClientSide()) return false;
        if (pContainer.isEmpty()) return false;

        for (int i = 0; i < inputItems.size(); i++) {
            if (!inputItems.get(i).test(pContainer.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return outPut.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return outPut.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BambooSieveRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return BambooSieveRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<BambooSieveRecipe> {
        public static final BambooSieveRecipe.Type INSTANCE = new BambooSieveRecipe.Type();
        public static final String ID = "bamboo_sieve_recipe";
    }

    public static class Serializer implements RecipeSerializer<BambooSieveRecipe> {
        public static final BambooSieveRecipe.Serializer INSTANCE = new BambooSieveRecipe.Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "bamboo_sieve_recipe");

        @Override
        public BambooSieveRecipe fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");

            NonNullList<Ingredient> inputs = NonNullList.create();
            for (int i = 0; i < ingredients.size(); i++) {
                inputs.add(Ingredient.fromJson(ingredients.get(i)));
            }

            if(jsonObject.has("medicinal_properties")) {
                JsonObject propsObj = GsonHelper.getAsJsonObject(jsonObject, "medicinal_properties");
                String key = propsObj.keySet().iterator().next();
                    MedicinalProperties prop = MedicinalProperties.getFromName(key);

                    JsonObject propObj = GsonHelper.getAsJsonObject(propsObj, key);
                    int medPropLevel = GsonHelper.getAsInt(propObj, "level");

//                    medicinalProps.put(prop, medPropLevel);
                Pair<MedicinalProperties, Integer> medicinalProps = new Pair<>(prop, medPropLevel);
                return new BambooSieveRecipe(inputs, output, pRecipeId, medicinalProps);
            }

            return new BambooSieveRecipe(inputs, output, pRecipeId, null);
        }

        @Override
        public BambooSieveRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));
            ItemStack output = pBuffer.readItem();

            String propName = pBuffer.readUtf();
            int medPropLevel = pBuffer.readInt();
            Pair<MedicinalProperties, Integer> props = new Pair<>(MedicinalProperties.getFromName(propName), medPropLevel);

            return new BambooSieveRecipe(inputs, output, pRecipeId, props);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BambooSieveRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());
            for (Ingredient ing : pRecipe.inputItems) {
                ing.toNetwork(pBuffer);
            }
            pBuffer.writeItem(pRecipe.outPut);

            pBuffer.writeUtf(pRecipe.medicinalProperties.getFirst().getName());
            pBuffer.writeInt(pRecipe.medicinalProperties.getSecond());
        }
    }
}
