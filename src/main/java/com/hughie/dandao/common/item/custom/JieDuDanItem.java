package com.hughie.dandao.common.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JieDuDanItem extends DanItem {
    public JieDuDanItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack usedStack = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide) {
            // 清除实体所有的负面效果
            clearAllNegativeEffects(entity);
        }

        return usedStack;
    }

    private void clearAllNegativeEffects(LivingEntity entity) {
        // 遍历实体所有的状态效果
        List<MobEffect> effectsToRemove = new ArrayList<>();
        for (MobEffectInstance effectInstance : entity.getActiveEffects()) {
            MobEffect effect = effectInstance.getEffect();
            // 判断是否为负面效果
            if (!effect.isBeneficial()) {
                // 移除该负面效果
                effectsToRemove.add(effect);
            }
        }

        for (MobEffect effect : effectsToRemove) {
            entity.removeEffect(effect);
        }
    }
}
