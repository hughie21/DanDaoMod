package com.hughie.dandao.common.item.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class DanItem extends Item {
    public DanItem(Properties pProperty) {
        super(pProperty);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(itemStack, level, tooltipComponents, isAdvanced);

        FoodProperties food = itemStack.getItem().getFoodProperties();
        if (food != null) {
            List<MobEffectInstance> effects = food.getEffects().stream()
                    .map(Pair::getFirst) // 获取效果实例
                    .toList();
            PotionUtils.addPotionTooltip(effects, tooltipComponents, 1.0F);
        }
    }
}
