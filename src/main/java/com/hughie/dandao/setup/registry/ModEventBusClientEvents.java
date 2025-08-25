package com.hughie.dandao.setup.registry;

import com.github.tartaricacid.simplebedrockmodel.client.manager.BedrockEntityModelRegisterEvent;
import com.hughie.dandao.DanDao;
import com.hughie.dandao.client.model.bedrock.SimpleBedrockModel;
import com.hughie.dandao.client.renderer.*;
import com.hughie.dandao.common.entity.AlchemyFurnaceBlockEntity;
import com.hughie.dandao.common.entity.ModEntities;
import com.hughie.dandao.common.item.ModItems;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.io.InputStream;

@Mod.EventBusSubscriber(modid = DanDao.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    public static final ResourceLocation ALCHEMY_FURNACE_MODEL = ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "bedrock/block/alchemy_furnace");

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModEntities.MORTAR_ENT.get(), MortarBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModEntities.ALCHEMY_FURNACE.get(), AlchemyFurnaceRenderer::new);
        event.registerBlockEntityRenderer(ModEntities.IRON_MILL_ENT.get(), IronMillRenderer::new);
        event.registerBlockEntityRenderer(ModEntities.KNEADING_BOARD.get(), KneadingBoardRenderer::new);
    }

    @SubscribeEvent
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void onRegisterBedrockModelRenderer(BedrockEntityModelRegisterEvent event) {
        event.register(ALCHEMY_FURNACE_MODEL, stream -> new SimpleBedrockModel((InputStream) stream));
    }
}
