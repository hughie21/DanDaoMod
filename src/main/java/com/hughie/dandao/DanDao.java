package com.hughie.dandao;

import com.hughie.dandao.client.network.AlchemyFurnaceDataPacket;
import com.hughie.dandao.client.network.AnimationFinishedPacket;
import com.hughie.dandao.client.network.SyncAlchemyFurnaceLockPacket;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.command.ModCommands;
import com.hughie.dandao.common.entity.ModEntities;
import com.hughie.dandao.common.item.ModCreativeModeTabs;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.effect.ModEffects;
import com.hughie.dandao.common.loot.ModLootModifiers;
import com.hughie.dandao.common.recipe.ModRecipes;
import com.hughie.dandao.common.screen.*;
import com.hughie.dandao.common.sound.ModSounds;
import com.hughie.dandao.client.network.ClientsidePlayAnimationPacket;
import com.hughie.dandao.common.viliager.ModVillagers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.hughie.dandao.client.renderer.SeatEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(DanDao.MOD_ID)
public class DanDao
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "dandao";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public DanDao(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        NETWORK.registerMessage(0, ClientsidePlayAnimationPacket.class,
                ClientsidePlayAnimationPacket::encode,
                ClientsidePlayAnimationPacket::decode,
                ClientsidePlayAnimationPacket::handle);

        NETWORK.registerMessage(1, AnimationFinishedPacket.class,
                AnimationFinishedPacket::toBytes,
                AnimationFinishedPacket::fromBytes,
                AnimationFinishedPacket::handle);

        NETWORK.registerMessage(2, AlchemyFurnaceDataPacket.class,
                AlchemyFurnaceDataPacket::encode,
                AlchemyFurnaceDataPacket::decode,
                AlchemyFurnaceDataPacket::handle);

        NETWORK.registerMessage(3, SyncAlchemyFurnaceLockPacket.class,
                SyncAlchemyFurnaceLockPacket::encode,
                SyncAlchemyFurnaceLockPacket::decode,
                SyncAlchemyFurnaceLockPacket::handle);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModEffects.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModSounds.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModLootModifiers.register(modEventBus);

        GeckoLib.initialize();

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ComposterBlock.COMPOSTABLES.put(ModItems.HUAIDAN.get(), 0.5f);
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.SPIRIT_STONE_ORE);
            event.accept(ModBlocks.CINNABAR_ORE);
            event.accept(ModBlocks.REALGAR_ORE);
            event.accept(ModBlocks.MICA_ORE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        ModCommands.register(event.getServer().getCommands().getDispatcher());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.SEAT.get(), SeatEntityRenderer::new);
            MenuScreens.register(ModMenuTypes.MORTAR_MENU.get(), MortarScreen::new);
            MenuScreens.register(ModMenuTypes.IRON_MILL_MENU.get(), IronMillScreen::new);
            MenuScreens.register(ModMenuTypes.KNEADING_BOARD_MENU.get(), KneadingBoardScreen::new);
            MenuScreens.register(ModMenuTypes.ALCHEMY_FURNACE_MENU.get(), AlchemyFurnaceScreen::new);
            MenuScreens.register(ModMenuTypes.BAMBOO_SIEVE_MENU.get(), BambooSieveScreen::new);
        }
    }
}
