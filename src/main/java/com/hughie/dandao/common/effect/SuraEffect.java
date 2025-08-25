package com.hughie.dandao.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SuraEffect extends MobEffect {
    public static final Map<UUID, Integer> killCounts = new HashMap<>();
    private static final float DAMAGE_PER_KILL = 0.5f;
    private static final float MAX_DAMAGE_BOOST = 5.0f;

    protected SuraEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x880808);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }

    public static float getDamageBoost(Player player) {
        int kills = killCounts.getOrDefault(player.getUUID(), 0);
        if(kills == 0) {
            return 1;
        }
        // 计算加成，不超过最大值
        return Math.min(kills * DAMAGE_PER_KILL, MAX_DAMAGE_BOOST);
    }

    public static void resetKillCount(Player player) {
        killCounts.remove(player.getUUID());
    }
}
