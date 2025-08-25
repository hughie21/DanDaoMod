package com.hughie.dandao.common.item.custom;

import com.hughie.dandao.api.IMedicalItem;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MedicalItem extends Item implements IMedicalItem {
    private final MedicinalProperties property;
    private final int level;
    public MedicalItem(Properties properties, MedicinalProperties property, int level) {
        super(properties);
        this.property = property;
        this.level = level;
    }

    public Pair<MedicinalProperties, Integer> getMedicalProperty() {
        return Pair.of(property, level);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> components, TooltipFlag isAdvanced) {
        components.add(property.getTranslatableName(level));
        super.appendHoverText(itemStack, world, components, isAdvanced);
    }
}
