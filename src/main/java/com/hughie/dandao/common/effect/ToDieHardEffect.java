package com.hughie.dandao.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ToDieHardEffect {

    public static class ToDieHardEffectInactive extends MobEffect {
        protected ToDieHardEffectInactive() {
            super(MobEffectCategory.BENEFICIAL, 0x66CDAA);
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            // 这个效果不需要每tick执行
            return false;
        }
    }

    public static class ToDieHardEffectActive extends MobEffect {
        protected ToDieHardEffectActive() {
            super(MobEffectCategory.HARMFUL, 0x66CDAA);
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            // 这个效果不需要每tick执行
            return false;
        }
    }
}
