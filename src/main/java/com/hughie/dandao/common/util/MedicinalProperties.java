package com.hughie.dandao.common.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Map;

public enum MedicinalProperties {
    COLD(0, 9, "cold", 0x1E90FF),
    COOL(0, 9, "cool", 0x00FFFF),
    WARM(0, 9, "warm", 0xF4A460),
    HOT(0, 9, "hot", 0xFF4500),
    DEFAULT(0, 0, "default", 0xFFFFF);

    private int minLevel;
    private int maxLevel;
    private String name;
    private final int color;

    MedicinalProperties(int minLevel, int maxLevel, String name, int color) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.name = name;
        this.color = color;
    }

    public int getMinLevel() { return minLevel; }
    public int getMaxLevel() { return maxLevel; }
    public String getName() { return name; }
    public int getColor() { return color; }

    public static MedicinalProperties getFromName(String name) {
        for(MedicinalProperties property : MedicinalProperties.values()) {
            if(property.getName().equals(name)) {
                return property;
            }
        }
        return MedicinalProperties.DEFAULT;
    }


    public MutableComponent getTranslatableName(int level) {
        Component levelComponent = Component.translatable("level.dandao." + level);
        return Component.translatable("properties.dandao.medicinal_properties",
                Component.translatable("properties.dandao." + this.name + "_name")
                        .withStyle(style -> style.withColor(this.color)),
                levelComponent);
    }

    /*
    * 各药性的相辅相克表
     */
    public Map<MedicinalProperties, Integer> getRelationship() {
        switch (this) {
            case HOT:
                return Map.of(
                    HOT, 0,
                    WARM, 2,
                    COOL, -2,
                    COLD, -1
                );
            case WARM:
                return Map.of(
                    WARM, 0,
                    HOT, 1,
                    COOL, -1,
                    COLD, -1
                );
            case COLD:
                return Map.of(
                        COLD, 0,
                        COOL, 2,
                        HOT, -1,
                        WARM, -2
                );
            case COOL:
                return Map.of(
                        COLD, 1,
                        COOL, 0,
                        HOT, -1,
                        WARM, -1
                );
            default:
                return Map.of();
        }
    }
}
