package com.hughie.dandao.common.item.custom;

import com.hughie.dandao.common.util.MedicinalProperties;
import com.hughie.dandao.common.util.MedicinalPropertiesNBT;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class DanEmbryoItem extends Item {
    public DanEmbryoItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        Pair<MedicinalProperties, Integer> medProperty = MedicinalPropertiesNBT.loadProperties(stack);
        tooltip.add(medProperty.getFirst().getTranslatableName(medProperty.getSecond()));
    }
}
