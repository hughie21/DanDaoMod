package com.hughie.dandao.common.item;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import org.spongepowered.asm.util.ObfuscationUtil;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DanDao.MOD_ID);

    public static final Supplier<CreativeModeTab> TAB1 = CREATIVE_MODE_TAB.register("dan_dao_tab_dan_yao",
            ()-> CreativeModeTab.builder()
                    .icon(()->new ItemStack(ModItems.JIYUNDAN.get()))
                    .title(Component.translatable("creativetab.dandao.dan_dao_tab_dan_yao"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.JIEDUDAN.get());
                        output.accept(ModItems.BIGUDAN.get());
                        output.accept(ModItems.JIYUNDAN.get());
                        output.accept(ModItems.SHOULIDAN.get());
                        output.accept(ModItems.FUTIDAN.get());
                        output.accept(ModItems.HUICHUNDAN.get());
                        output.accept(ModItems.LONGLIDAN.get());
                        output.accept(ModItems.JULINGDAN.get());
                        output.accept(ModItems.HUIGUANGDAN.get());
                        output.accept(ModItems.JIUSIPOYUANDAN.get());
                        output.accept(ModItems.FANMINGDAN.get());
                        output.accept(ModItems.XUEHUNDAN.get());
                        output.accept(ModItems.JINHUANDAN.get());
                        output.accept(ModItems.TUHUANDAN.get());
                        output.accept(ModItems.HUOHUANDAN.get());
                        output.accept(ModItems.MUHUANDAN.get());
                        output.accept(ModItems.SHUIHUANDAN.get());
                        output.accept(ModItems.HUAIDAN.get());

                        output.accept(ModItems.XIAOYAOWAN.get());
                        output.accept(ModItems.BIXIESAN.get());
                        output.accept(ModItems.XINGJUNSAN.get());
                        output.accept(ModItems.SUIXINGDAN.get());
                        output.accept(ModItems.HUISHENGDAN.get());
                        output.accept(ModItems.HEIXIDAN.get());
                        output.accept(ModItems.CHIYANDAN.get());
                        output.accept(ModItems.NINGBIDAN.get());
                        output.accept(ModItems.WUSHISAN.get());
                        output.accept(ModItems.DUANHUNSAN.get());
                    }))
                    .build());

    public static final Supplier<CreativeModeTab> TAB2 = CREATIVE_MODE_TAB.register("dan_dao_tab_ingredient",
            ()-> CreativeModeTab.builder()
                    .icon(()->new ItemStack(ModItems.SPIRIT_STONE.get()))
                    .title(Component.translatable("creativetab.dandao.dan_dao_tab_ingredient"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.SPIRIT_STONE_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_SPIRIT_STONE_ORE.get());
                        output.accept(ModBlocks.CINNABAR_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_CINNABAR_ORE.get());
                        output.accept(ModBlocks.REALGAR_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_REALGAR_ORE.get());
                        output.accept(ModBlocks.MICA_ORE.get());
                        output.accept(ModBlocks.SULFUR_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_SULFUR_ORE.get());
                        output.accept(ModBlocks.NETHER_SULFUR_ORE.get());

                        // 原矿
                        output.accept(ModItems.SPIRIT_STONE.get());
                        output.accept(ModItems.ORPIMENT.get());
                        output.accept(ModItems.REALGAR.get());
                        output.accept(ModItems.CINNABAR.get());
                        output.accept(ModItems.MICA.get());
                        output.accept(ModItems.SULFUR.get());

                        // 矿粉
                        output.accept(ModItems.CINNABAR_POWDER.get());
                        output.accept(ModItems.ASADIN.get());
                        output.accept(ModItems.COAL_POWDER.get());
                        output.accept(ModItems.LIME.get());
                        output.accept(ModItems.ORPIMENT_POWDER.get());
                        output.accept(ModItems.REALGAR_POWDER.get());
                        output.accept(ModItems.SULFUR_POWDER.get());

                        // 作物
                        output.accept(ModItems.GINSENG_SEEDS.get());
                        output.accept(ModItems.BLOOD_BERRY_SEEDS.get());
                        output.accept(ModItems.STONE_FLOWER_SEED.get());
                        output.accept(ModItems.FLARE_FRUIT_SEEDS.get());
                        output.accept(ModItems.SUN_FLOWER_SEEDS.get());
                        output.accept(ModItems.SNOW_GINSENG_SEEDS.get());
                        output.accept(ModItems.GINSENG.get());
                        output.accept(ModItems.BLOOD_BERRY.get());
                        output.accept(ModItems.BLOOD_VINES_ITEM.get());
                        output.accept(ModItems.GROUND_MOSS.get());
                        output.accept(ModItems.STONE_FLOWER.get());
                        output.accept(ModItems.FLARE_FRUIT.get());
                        output.accept(ModItems.SUN_LEAF.get());
                        output.accept(ModItems.SNOW_LOTUS.get());
                        output.accept(ModItems.SNOW_GINSENG.get());

                        // 炮制的原料
                        output.accept(ModItems.PROCESSED_GINSENG.get());
                        output.accept(ModItems.PROCESSED_BLOOD_BERRY.get());
                        output.accept(ModItems.PROCESSED_BLOOD_VINES_ITEM.get());
                        output.accept(ModItems.PROCESSED_GROUND_MOSS.get());
                        output.accept(ModItems.PROCESSED_STONE_FLOWER.get());
                        output.accept(ModItems.PROCESSED_FLARE_FRUIT.get());
                        output.accept(ModItems.PROCESSED_SUN_LEAF.get());
                        output.accept(ModItems.PROCESSED_SNOW_LOTUS.get());
                        output.accept(ModItems.PROCESSED_SNOW_GINSENG.get());
                    }))
                    .build());

    public static final Supplier<CreativeModeTab> TAB3 = CREATIVE_MODE_TAB.register("dan_dao_tab_tools",
            ()-> CreativeModeTab.builder()
                    .icon(()->new ItemStack(ModBlocks.IRON_MILL.get()))
                    .title(Component.translatable("creativetab.dandao.dan_dao_tab_tools"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.MORTAR.get());
                        output.accept(ModBlocks.PUTUAN.get());
                        output.accept(ModBlocks.IRON_MILL.get());
                        output.accept(ModBlocks.KNEADING_BOARD.get());
                        output.accept(ModBlocks.REACTOR.get());
                        output.accept(ModItems.BAMBOO_SIEVE.get());
                        output.accept(ModItems.WHISK.get());
                        output.accept(ModBlocks.INCENSE_BURNER.get());
                        output.accept(ModItems.INCENSE_STICK.get());
                        output.accept(ModItems.TAOIST_ROBE_HELMET.get());
                        output.accept(ModItems.TAOIST_ROBE_CHESTPLATE.get());
                        output.accept(ModItems.TAOIST_ROBE_LEGGINGS.get());
                        output.accept(ModItems.TAOIST_ROBE_BOOTS.get());
                    }))
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
