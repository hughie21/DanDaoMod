package com.hughie.dandao.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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

public class AlchemyFurnaceRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack outPut;
    private final ResourceLocation id;
    // 属性 -> 最小等级/最大等级/所需时间/所需热量
    private final Pair<MedicinalProperties, PropertiesData> medicinalProperties;


    public AlchemyFurnaceRecipe(NonNullList<Ingredient> inputItems, ItemStack outPut,
                                ResourceLocation id, Pair<MedicinalProperties, PropertiesData> medicinalProperties) {
        this.inputItems = inputItems;
        this.outPut = outPut;
        this.id = id;
        this.medicinalProperties = medicinalProperties;
    }

    public Pair<MedicinalProperties, PropertiesData> getMedicinalProperties() {
        return medicinalProperties;
    }


    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level level) {
        if(level.isClientSide()) {
            return false;
        }
        return inputItems.get(0).test(pContainer.getItem(0));
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
        return AlchemyFurnaceRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return AlchemyFurnaceRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<AlchemyFurnaceRecipe> {
        public static final AlchemyFurnaceRecipe.Type INSTANCE = new AlchemyFurnaceRecipe.Type();
        public static final String ID = "alchemy_furnace_recipe";
    }

    public static class Serializer implements RecipeSerializer<AlchemyFurnaceRecipe> {
        public static final AlchemyFurnaceRecipe.Serializer INSTANCE = new AlchemyFurnaceRecipe.Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "alchemy_furnace_recipe");

        @Override
        public AlchemyFurnaceRecipe fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");

            NonNullList<Ingredient> inputs = NonNullList.create();
            for (int i = 0; i < ingredients.size(); i++) {
                inputs.add(Ingredient.fromJson(ingredients.get(i)));
            }

            if(jsonObject.has("medicinal_properties")) {
                JsonObject propsObj = GsonHelper.getAsJsonObject(jsonObject, "medicinal_properties");
                if (propsObj.size() == 0) {
                    throw new JsonSyntaxException("medicinal_properties must contain at least one property");
                }

                String key = propsObj.keySet().iterator().next();
                MedicinalProperties prop = MedicinalProperties.getFromName(key);

                JsonObject propObj = GsonHelper.getAsJsonObject(propsObj, key);
                int min = GsonHelper.getAsInt(propObj, "min");
                int max = GsonHelper.getAsInt(propObj, "max");
                float time = GsonHelper.getAsFloat(propObj, "time");
                int heat = GsonHelper.getAsInt(propObj, "heat");
                Pair<MedicinalProperties, PropertiesData> medicinalProps = new Pair<>(prop, new PropertiesData(min, max, time, heat));
                return new AlchemyFurnaceRecipe(inputs, output, pRecipeId, medicinalProps);
            }

            return new AlchemyFurnaceRecipe(inputs, output, pRecipeId, null);
        }

        @Override
        public AlchemyFurnaceRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));
            ItemStack output = pBuffer.readItem();

            String propName = pBuffer.readUtf();
            int min = pBuffer.readInt();
            int max = pBuffer.readInt();
            float time = pBuffer.readFloat();
            int heat = pBuffer.readInt();

            Pair<MedicinalProperties, PropertiesData> props = new Pair<>(MedicinalProperties.getFromName(propName), new PropertiesData(min, max, time, heat));
            return new AlchemyFurnaceRecipe(inputs, output, pRecipeId, props);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, AlchemyFurnaceRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());
            for (Ingredient ing : pRecipe.inputItems) {
                ing.toNetwork(pBuffer);
            }
            pBuffer.writeItem(pRecipe.outPut);

            pBuffer.writeUtf(pRecipe.medicinalProperties.getFirst().getName());
            pBuffer.writeInt(pRecipe.medicinalProperties.getSecond().getMinLevel()); // min
            pBuffer.writeInt(pRecipe.medicinalProperties.getSecond().getMaxLevel()); // max
            pBuffer.writeFloat(pRecipe.medicinalProperties.getSecond().getTime()); // time
            pBuffer.writeInt(pRecipe.medicinalProperties.getSecond().getHeat()); // heat

        }
    }

    public static class PropertiesData {
        private final int minLevel;
        private final int maxLevel;
        private final float time;
        private final int heat;

        public PropertiesData(int minLevel, int maxLevel, float time, int heat) {
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
            this.time = time;
            this.heat = heat;
        }

        public int getMinLevel() {
            return minLevel;
        }

        public int getMaxLevel() {
            return maxLevel;
        }

        public float getTime() {
            return time;
        }

        public int getHeat() {
            return heat;
        }
    }
}
