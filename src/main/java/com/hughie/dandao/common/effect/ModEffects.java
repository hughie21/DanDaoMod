package com.hughie.dandao.common.effect;

import com.hughie.dandao.DanDao;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, DanDao.MOD_ID);

    public static final RegistryObject<MobEffect> FLIGHT = EFFECTS.register("flight_effect", FlightEffect::new);
    public static final RegistryObject<MobEffect> XP_BOOST = EFFECTS.register("xp_boost", XPBoostEffect::new);
    public static final RegistryObject<MobEffect> TO_DIE_HARD_INACTIVE = EFFECTS.register("to_die_hard_inactive", ToDieHardEffect.ToDieHardEffectInactive::new);
    public static final RegistryObject<MobEffect> TO_DIE_HARD_ACTIVE = EFFECTS.register("to_die_hard_active", ToDieHardEffect.ToDieHardEffectActive::new);
    public static final RegistryObject<MobEffect> NO_TURNING_BACK = EFFECTS.register("no_turning_back", NoTurningBackEffect::new);
    public static final RegistryObject<MobEffect> SURA = EFFECTS.register("sura", SuraEffect::new);
    public static final RegistryObject<MobEffect> SOUL_LOCK = EFFECTS.register("soul_lock", SoulLockEffect::new);
    public static final RegistryObject<MobEffect> HEARTBROKEN = EFFECTS.register("heartbroken", HeartbrokenEffect::new);
    public static final RegistryObject<MobEffect> DAN_WISDOM = EFFECTS.register("dan_wisdom", DanWisdom::new);
    public static final RegistryObject<MobEffect> POISON_IMMUNE = EFFECTS.register("poison_immune", PoisonImmune::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
