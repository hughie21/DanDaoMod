package com.hughie.dandao.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class PoisonImmune extends MobEffect {
    public PoisonImmune() {
        super(MobEffectCategory.BENEFICIAL, 0xD8BFD8);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}
