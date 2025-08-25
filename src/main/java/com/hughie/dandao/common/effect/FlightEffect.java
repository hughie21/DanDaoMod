package com.hughie.dandao.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;

public class FlightEffect extends MobEffect {
    public FlightEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && !player.level().isClientSide) {
            Abilities abilities = player.getAbilities();
            if (!player.isCreative() && !player.isSpectator()) {
                // 允许飞行
                if (!abilities.mayfly) {
                    abilities.mayfly = true;
                    player.onUpdateAbilities(); // 同步能力到客户端
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // 每tick都执行
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap map, int amplifier) {
        // 如果是玩家，恢复其飞行能力设置
        if (entity instanceof Player player && !player.level().isClientSide) {
            Abilities abilities = player.getAbilities();

            // 效果结束时恢复设置（仅在生存模式）
            if (!player.isCreative() && !player.isSpectator()) {
                // 如果玩家不在地面上，强制禁用飞行防止无限飞行
                if (!player.onGround()) {
                    abilities.mayfly = false;
                    abilities.flying = false;
                }
                player.onUpdateAbilities(); // 同步能力到客户端
            }
        }
        super.removeAttributeModifiers(entity, map, amplifier);
    }
}
