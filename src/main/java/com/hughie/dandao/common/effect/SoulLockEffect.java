package com.hughie.dandao.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SoulLockEffect extends MobEffect {
    public SoulLockEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x008080);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // 这个效果不需要每tick执行
        return false;
    }
}
