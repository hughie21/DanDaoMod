package com.hughie.dandao.common.item.custom;

import com.hughie.dandao.common.util.PlayerUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BiXieSanItem extends SmallDanItem{
    public BiXieSanItem(Properties pProperty) {
        super(pProperty, 5, new ArrayList<>());
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (entity instanceof Player player) {
                player.getFoodData().eat(nutrition, saturation);
                clearAllNegativeEffects(player);
                // 检查是否是最后一次使用
                int newDamage = stack.getDamageValue() + 1;
                if (newDamage >= getMaxDamage(stack)) {
                    // 如果是最后一次使用，消耗当前物品并给予空瓶子
                    stack.shrink(1);
                    PlayerUtils.givePlayerItems(player, new ItemStack(Items.GLASS_BOTTLE));
                    return stack;
                } else {
                    // 不是最后一次使用，正常减少耐久
                    stack.setDamageValue(newDamage);
                }
            }
        }
        return stack;
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

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("effect.dandao.jiedudan").withStyle(style -> style.withColor(0xFF55FF)));
        super.appendHoverText(itemStack, level, tooltipComponents, isAdvanced);
    }
}
