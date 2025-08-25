package com.hughie.dandao.setup.dataGen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output,  ExistingFileHelper existingFileHelper) {
        super(output, DanDao.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // 丹药
        simpleItem(ModItems.FUTIDAN);
        simpleItem(ModItems.HUICHUNDAN);
        simpleItem(ModItems.LONGLIDAN);
        simpleItem(ModItems.JIEDUDAN);
        simpleItem(ModItems.BIGUDAN);
        simpleItem(ModItems.SHOULIDAN);
        simpleItem(ModItems.JIYUNDAN);
        simpleItem(ModItems.JULINGDAN);
        simpleItem(ModItems.HUIGUANGDAN);
        simpleItem(ModItems.JIUSIPOYUANDAN);
        simpleItem(ModItems.FANMINGDAN);
        simpleItem(ModItems.XUEHUNDAN);
        simpleItem(ModItems.FENGXINGDAN);
        simpleItem(ModItems.JINHUANDAN);
        simpleItem(ModItems.SHUIHUANDAN);
        simpleItem(ModItems.HUOHUANDAN);
        simpleItem(ModItems.TUHUANDAN);
        simpleItem(ModItems.MUHUANDAN);
        simpleItem(ModItems.HUAIDAN);
        simpleItem(ModItems.XIAOYAOWAN);
        simpleItem(ModItems.BIXIESAN);
        simpleItem(ModItems.XINGJUNSAN);
        simpleItem(ModItems.SUIXINGDAN);
        simpleItem(ModItems.HUISHENGDAN);
        simpleItem(ModItems.HEIXIDAN);
        simpleItem(ModItems.CHIYANDAN);
        simpleItem(ModItems.NINGBIDAN);
        simpleItem(ModItems.WUSHISAN);
        simpleItem(ModItems.DUANHUNSAN);

        // 作物
        simpleItem(ModItems.GINSENG);
        simpleItem(ModItems.GINSENG_SEEDS);
        simpleItem(ModItems.BLOOD_BERRY);
        simpleItem(ModItems.BLOOD_BERRY_SEEDS);
        simpleItem(ModItems.GROUND_MOSS);
        simpleItem(ModItems.STONE_FLOWER);
        simpleItem(ModItems.STONE_FLOWER_SEED);
        simpleItem(ModItems.FLARE_FRUIT_SEEDS);
        simpleItem(ModItems.FLARE_FRUIT);
        simpleItem(ModItems.SUN_FLOWER_SEEDS);
        simpleItem(ModItems.SUN_LEAF);
        simpleItem(ModItems.SNOW_LOTUS);
        simpleItem(ModItems.SNOW_GINSENG);
        simpleItem(ModItems.SNOW_GINSENG_SEEDS);

        // 原料
        simpleItem(ModItems.CINNABAR_POWDER);
        simpleItem(ModItems.ASADIN);
        simpleItem(ModItems.COAL_POWDER);
        simpleItem(ModItems.LIME);
        simpleItem(ModItems.ORPIMENT_POWDER);
        simpleItem(ModItems.REALGAR_POWDER);
        simpleItem(ModItems.MICA);
        simpleItem(ModItems.CINNABAR);
        simpleItem(ModItems.REALGAR);
        simpleItem(ModItems.ORPIMENT);
        simpleItem(ModItems.SPIRIT_STONE);
        simpleItem(ModItems.SULFUR);
        simpleItem(ModItems.SULFUR_POWDER);
        simpleItem(ModItems.MEDICINAL_POWDER);
        simpleItem(ModItems.DAN_EMBRYO);

        // 炮制的原料
        simpleItem(ModItems.PROCESSED_GINSENG);
        simpleItem(ModItems.PROCESSED_BLOOD_BERRY);
        simpleItem(ModItems.PROCESSED_BLOOD_VINES_ITEM);
        simpleItem(ModItems.PROCESSED_GROUND_MOSS);
        simpleItem(ModItems.PROCESSED_STONE_FLOWER);
        simpleItem(ModItems.PROCESSED_FLARE_FRUIT);
        simpleItem(ModItems.PROCESSED_SUN_LEAF);
        simpleItem(ModItems.PROCESSED_SNOW_LOTUS);
        simpleItem(ModItems.PROCESSED_SNOW_GINSENG);
        simpleItem(ModItems.INCENSE_STICK);

        simpleItem(ModItems.ALCHEMY_FURNACE_ICON);

        trimmedArmorItem(ModItems.TAOIST_ROBE_HELMET);
        trimmedArmorItem(ModItems.TAOIST_ROBE_LEGGINGS);
        trimmedArmorItem(ModItems.TAOIST_ROBE_CHESTPLATE);
        trimmedArmorItem(ModItems.TAOIST_ROBE_BOOTS);
    }

    // Shoutout to El_Redstoniano for making this
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = DanDao.MOD_ID;

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.fromNamespaceAndPath(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.fromNamespaceAndPath(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                ResourceLocation.fromNamespaceAndPath(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> itemRegistryObject) {
        return withExistingParent(itemRegistryObject.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "item/" + itemRegistryObject.getId().getPath()));
    }
}
