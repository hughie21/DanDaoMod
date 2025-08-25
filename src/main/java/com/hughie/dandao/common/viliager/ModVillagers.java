package com.hughie.dandao.common.viliager;

import com.google.common.collect.ImmutableSet;
import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.ModBlocks;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPE =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, DanDao.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, DanDao.MOD_ID);

    public static final RegistryObject<PoiType> HERB_POI = POI_TYPE.register("herb_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.IRON_MILL.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> HERBALIST =
            VILLAGER_PROFESSIONS.register("herbalist", () -> new VillagerProfession("herbalist",
                    poiTypeHolder -> poiTypeHolder.get() == HERB_POI.get(), holder -> holder.get() == HERB_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));


    public static void register(IEventBus eventBus) {
        POI_TYPE.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
