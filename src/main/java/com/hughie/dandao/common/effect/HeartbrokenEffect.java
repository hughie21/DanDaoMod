package com.hughie.dandao.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class HeartbrokenEffect extends MobEffect {

    protected HeartbrokenEffect() {
        super(MobEffectCategory.HARMFUL, 0x2ff02);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}
