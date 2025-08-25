package com.hughie.dandao.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class DanWisdom extends MobEffect {
    public DanWisdom() {
        super(MobEffectCategory.BENEFICIAL, 0xFF4452);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}
