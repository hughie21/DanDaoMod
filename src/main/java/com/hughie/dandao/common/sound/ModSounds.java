package com.hughie.dandao.common.sound;

import com.hughie.dandao.DanDao;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DanDao.MOD_ID);

    public static final RegistryObject<SoundEvent> GRIND_MEDICINE = registerSoundEvents("grind_medicine");
    public static final RegistryObject<SoundEvent> CLOSE_FURNACE = registerSoundEvents("close_furnace");
    public static final RegistryObject<SoundEvent> OPEN_FURNACE = registerSoundEvents("open_furnace");
    public static final RegistryObject<SoundEvent> TAKING_MEDICATION = registerSoundEvents("taking_medication");
    public static final RegistryObject<SoundEvent> EMPTY_SOUND = registerSoundEvents("empty_sound");
    public static final RegistryObject<SoundEvent> BAMBOO_SIEVE_SHAKE = registerSoundEvents("bamboo_sieve_shake");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
