package com.hughie.dandao.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class XPBoostEffect extends MobEffect {
    public XPBoostEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // 这个效果不需要每tick执行
        return false;
    }
}

