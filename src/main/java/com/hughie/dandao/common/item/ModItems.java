package com.hughie.dandao.common.item;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.effect.ModEffects;
import com.hughie.dandao.common.item.custom.*;
import com.hughie.dandao.common.sound.ModSounds;
import com.hughie.dandao.common.util.MedicinalProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DanDao.MOD_ID);

    // 矿物
    public static final RegistryObject<Item> SPIRIT_STONE = ITEMS.register("spirit_stone",
            ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CINNABAR = ITEMS.register("cinnabar",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.HOT, 1));
    public static final RegistryObject<Item> REALGAR = ITEMS.register("realgar",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.HOT, 0));
    public static final RegistryObject<Item> ORPIMENT = ITEMS.register("orpiment",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.WARM, 1));
    public static final RegistryObject<Item> ASADIN = ITEMS.register("asadin",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.COLD, 2));
    public static final RegistryObject<Item> CINNABAR_POWDER = ITEMS.register("cinnabar_powder",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.HOT, 2));
    public static final RegistryObject<Item> COAL_POWDER = ITEMS.register("coal_powder",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.COOL, 1));
    public static final RegistryObject<Item> LIME = ITEMS.register("lime",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.HOT, 0));
    public static final RegistryObject<Item> ORPIMENT_POWDER = ITEMS.register("orpiment_powder",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.WARM, 2));
    public static final RegistryObject<Item> REALGAR_POWDER = ITEMS.register("realgar_powder",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.HOT, 1));
    public static final RegistryObject<Item> MICA = ITEMS.register("mica",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.COOL, 0));
    public static final RegistryObject<Item> SULFUR = ITEMS.register("sulfur",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.WARM, 0));
    public static final RegistryObject<Item> SULFUR_POWDER = ITEMS.register("sulfur_powder",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.WARM, 1));

    // 丹药
    // 大蜜丸
    public static final RegistryObject<Item> FUTIDAN = ITEMS.register("fu_ti_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.FUTIDAN).stacksTo(16)));
    public static final RegistryObject<Item> HUICHUNDAN = ITEMS.register("hui_chun_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.HUICHUNDAN).stacksTo(16)));
    public static final RegistryObject<Item> LONGLIDAN = ITEMS.register("long_li_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.LONGLIDAN).stacksTo(16)));
    public static final RegistryObject<Item> JIEDUDAN = ITEMS.register("jie_du_dan",
            ()-> new JieDuDanItem(new Item.Properties().food(ModFoodProperties.JIEDUDAN).stacksTo(16)));
    public static final RegistryObject<Item> SHOULIDAN = ITEMS.register("shou_li_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.SHOULIDAN).stacksTo(16)));
    public static final RegistryObject<Item> BIGUDAN = ITEMS.register("bi_gu_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.BIGUDAN).stacksTo(16)));
    public static final RegistryObject<Item> JIYUNDAN = ITEMS.register("ji_yun_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.JIYUNDAN).stacksTo(16)));
    public static final RegistryObject<Item> JULINGDAN = ITEMS.register("ju_ling_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.JULINGDAN).stacksTo(16)));
    public static final RegistryObject<Item> HUIGUANGDAN = ITEMS.register("hui_guang_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.HUIGUANGDAN).stacksTo(16)));
    public static final RegistryObject<Item> JIUSIPOYUANDAN = ITEMS.register("jiu_si_po_yuan_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.JIUSIPOYUANDAN).stacksTo(16)));
    public static final RegistryObject<Item> FANMINGDAN = ITEMS.register("fan_ming_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.FANMINGDAN).stacksTo(16)));
    public static final RegistryObject<Item> XUEHUNDAN = ITEMS.register("xue_hun_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.XUEHUNDAN).stacksTo(16)));
    public static final RegistryObject<Item> HUOHUANDAN = ITEMS.register("huo_huan_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.HUOHUANDAN).stacksTo(16)));
    public static final RegistryObject<Item> MUHUANDAN = ITEMS.register("mu_huan_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.MUHUANDAN).stacksTo(16)));
    public static final RegistryObject<Item> SHUIHUANDAN = ITEMS.register("shui_huan_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.SHUIHUANDAN).stacksTo(16)));
    public static final RegistryObject<Item> JINHUANDAN = ITEMS.register("jin_huan_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.JINHUANDAN).stacksTo(16)));
    public static final RegistryObject<Item> TUHUANDAN = ITEMS.register("tu_huan_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.TUHUANDAN).stacksTo(16)));
    public static final RegistryObject<Item> FENGXINGDAN = ITEMS.register("feng_xing_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.FENGXINGDAN).stacksTo(16)));
    public static final RegistryObject<Item> HUAIDAN = ITEMS.register("huai_dan",
            ()-> new DanItem(new Item.Properties().food(ModFoodProperties.HUAIDAN).stacksTo(16)));

    // 水丸
    public static final RegistryObject<Item> XIAOYAOWAN = ITEMS.register("xiao_yao_wan",
            ()-> new SmallDanItem(List.of(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2*60*20, 1),
                    new MobEffectInstance(MobEffects.JUMP, 2*60*20, 0)
            )));
    public static final RegistryObject<Item> BIXIESAN = ITEMS.register("bi_xie_san",
            ()-> new BiXieSanItem(new Item.Properties()));
    public static final RegistryObject<Item> XINGJUNSAN = ITEMS.register("xing_jun_san",
            ()-> new SmallDanItem(List.of(
                    new MobEffectInstance(MobEffects.SATURATION, 2*60*20)
            )));
    public static final RegistryObject<Item> SUIXINGDAN = ITEMS.register("sui_xing_dan",
            ()-> new SmallDanItem(List.of(
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2*60*20, 1),
                    new MobEffectInstance(MobEffects.ABSORPTION, 2*60*20)
            )));
    public static final RegistryObject<Item> HUISHENGDAN = ITEMS.register("hui_sheng_dan",
            ()-> new SmallDanItem(List.of(
                    new MobEffectInstance(MobEffects.REGENERATION, 30*20)
            )));
    public static final RegistryObject<Item> HEIXIDAN = ITEMS.register("hei_xi_dan",
            ()-> new SmallDanItem(List.of(
                    new MobEffectInstance(MobEffects.NIGHT_VISION, 2*60*20),
                    new MobEffectInstance(MobEffects.DIG_SPEED, 2*60*20)
            )));
    public static final RegistryObject<Item> CHIYANDAN = ITEMS.register("chi_yan_dan",
            ()-> new SmallDanItem(List.of(
                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2*60*20, 1)
            )));
    public static final RegistryObject<Item> NINGBIDAN = ITEMS.register("ning_bi_dan",
            ()-> new SmallDanItem(List.of(
                    new MobEffectInstance(MobEffects.CONDUIT_POWER, 2*60*20),
                    new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 2*60*20)
            )));
    public static final RegistryObject<Item> WUSHISAN = ITEMS.register("wu_shi_san",
            ()-> new SmallDanItem(List.of(
                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2*60*20, 1)
            )));
    public static final RegistryObject<Item> DUANHUNSAN = ITEMS.register("duan_hun_san",
            ()-> new SmallDanItem(List.of(
                    new MobEffectInstance(ModEffects.TO_DIE_HARD_INACTIVE.get(), 60*20)
            )));

    // 作物
    public static final RegistryObject<Item> GINSENG_SEEDS = ITEMS.register("ginseng_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.GINSENG_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> GINSENG = ITEMS.register("ginseng",
            ()-> new MedicalItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.5f)
                    .build()
            ), MedicinalProperties.WARM, 1));
    public static final RegistryObject<Item> BLOOD_BERRY_SEEDS = ITEMS.register("blood_berry_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.BLOOD_BERRY_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLOOD_BERRY = ITEMS.register("blood_berry",
            ()-> new MedicalItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationMod(0.25f)
                    .build()
            ), MedicinalProperties.WARM, 2));
    public static final RegistryObject<Item> BLOOD_VINES_ITEM = ITEMS.register(
            "blood_vine_plant",
            () -> new MedicalBlockItem(ModBlocks.BLOOD_VINES.get(), new Item.Properties(), MedicinalProperties.WARM, 3)
    );
    public static final RegistryObject<Item> GROUND_MOSS = ITEMS.register("ground_moss",
            ()-> new MedicalBlockItem(ModBlocks.GROUND_MOSS.get(), new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationMod(0.25f)
                    .build()
            ), MedicinalProperties.COOL, 2));
    public static final RegistryObject<Item> STONE_FLOWER_SEED = ITEMS.register("stone_flower_seed",
            ()-> new ItemNameBlockItem(ModBlocks.STONE_FLOWER_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> STONE_FLOWER = ITEMS.register("stone_flower",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.COOL, 3));

    public static final RegistryObject<Item> FLARE_FRUIT_SEEDS = ITEMS.register("flare_fruit_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.FLARE_FRUIT_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> FLARE_FRUIT = ITEMS.register("flare_fruit",
            ()-> new MedicalItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.5f)
                    .build()
            ), MedicinalProperties.HOT, 1));

    public static final RegistryObject<Item> SUN_FLOWER_SEEDS = ITEMS.register("sun_flower_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.SUN_FLOWER_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUN_LEAF = ITEMS.register("sun_leaf",
            ()-> new MedicalItem(new Item.Properties(), MedicinalProperties.HOT, 3));

    public static final RegistryObject<Item> SNOW_LOTUS = ITEMS.register("snow_lotus",
            ()-> new MedicalBlockItem(ModBlocks.SNOW_LOTUS_CROP.get(), new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.25f).build()), MedicinalProperties.COLD, 1));

    public static final RegistryObject<Item> SNOW_GINSENG_SEEDS = ITEMS.register("snow_ginseng_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.SNOW_GINSENG_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SNOW_GINSENG = ITEMS.register("snow_ginseng",
            ()-> new MedicalItem(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.5f)
                    .build()
            ), MedicinalProperties.COLD, 2));
    public static RegistryObject<Item> PROCESSED_GINSENG = ITEMS.register("processed_ginseng",()-> new MedicalItem(new Item.Properties(), MedicinalProperties.WARM, 2));
    public static RegistryObject<Item> PROCESSED_BLOOD_BERRY = ITEMS.register("processed_blood_berry",()-> new MedicalItem(new Item.Properties(), MedicinalProperties.WARM, 3));
    public static RegistryObject<Item> PROCESSED_BLOOD_VINES_ITEM = ITEMS.register("processed_blood_vine_plant",()-> new MedicalItem(new Item.Properties(), MedicinalProperties.WARM, 4));
    public static RegistryObject<Item> PROCESSED_GROUND_MOSS = ITEMS.register("processed_ground_moss",()-> new MedicalItem(new Item.Properties(), MedicinalProperties.COOL, 3));
    public static RegistryObject<Item> PROCESSED_STONE_FLOWER = ITEMS.register("processed_stone_flower",()-> new MedicalItem(new Item.Properties(), MedicinalProperties.COOL, 4));
    public static RegistryObject<Item> PROCESSED_FLARE_FRUIT = ITEMS.register("processed_flare_fruit",()-> new MedicalItem(new Item.Properties(), MedicinalProperties.HOT, 2));
    public static RegistryObject<Item> PROCESSED_SUN_LEAF = ITEMS.register("processed_sun_leaf",()-> new MedicalItem(new Item.Properties(), MedicinalProperties.HOT, 3));
    public static RegistryObject<Item> PROCESSED_SNOW_LOTUS = ITEMS.register("processed_snow_lotus",()-> new MedicalItem(new Item.Properties(), MedicinalProperties.COLD, 2));
    public static RegistryObject<Item> PROCESSED_SNOW_GINSENG = ITEMS.register("processed_snow_ginseng",()-> new MedicalItem(new Item.Properties(), MedicinalProperties.COLD, 3));

    public static RegistryObject<Item> MEDICINAL_POWDER = ITEMS.register("medicinal_powder", ()-> new MedicinalPowderItem(new Item.Properties().stacksTo(1)));
    public static RegistryObject<Item> DAN_EMBRYO = ITEMS.register("dan_embryo", ()-> new DanEmbryoItem(new Item.Properties().stacksTo(1)));

    public static RegistryObject<Item> INCENSE_STICK = ITEMS.register("incense_stick", ()-> new Item(new Item.Properties()));

    // 工具
    public static final RegistryObject<Item> WHISK = ITEMS.register("whisk",
            ()-> new WhiskItem(new Item.Properties()));
    public static final RegistryObject<Item> BAMBOO_SIEVE = ITEMS.register("bamboo_sieve",
            ()-> new BambooSieveItem(new Item.Properties().stacksTo(1).durability(200)));

    public static final RegistryObject<Item> ALCHEMY_FURNACE_ICON = ITEMS.register("alchemy_furnace_icon",
            ()-> new BambooSieveItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> TAOIST_ROBE_HELMET = ITEMS.register("taoist_robe_helmet",
            ()-> new ModArmorItem(ModArmorMaterials.ROBE, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> TAOIST_ROBE_CHESTPLATE = ITEMS.register("taoist_robe_chestplate",
            ()-> new ArmorItem(ModArmorMaterials.ROBE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> TAOIST_ROBE_LEGGINGS = ITEMS.register("taoist_robe_leggings",
            ()-> new ArmorItem(ModArmorMaterials.ROBE, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> TAOIST_ROBE_BOOTS = ITEMS.register("taoist_robe_boots",
            ()-> new ArmorItem(ModArmorMaterials.ROBE, ArmorItem.Type.BOOTS, new Item.Properties()));

    // 注册方法
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
