package com.hughie.dandao.setup.registry;

import com.hughie.dandao.api.IncenseSubscriptionCapability;
import com.hughie.dandao.common.capability.IncenseSubscriptionCapabilityImpl;
import com.hughie.dandao.common.block.mutiblock.MutiBlockManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonRegistry {
    public static final Capability<IncenseSubscriptionCapability> INCENSE_SUBSCRIPTION =
            CapabilityManager.get(new CapabilityToken<>() {});

    public static LazyOptional<IncenseSubscriptionCapability> getSubscriptionCapability(Player player) {
        return player.getCapability(INCENSE_SUBSCRIPTION);
    }

    @SubscribeEvent
    public static void onSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(MutiBlockManager::init);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IncenseSubscriptionCapabilityImpl.class);
    }
}
