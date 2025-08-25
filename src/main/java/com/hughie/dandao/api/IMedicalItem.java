package com.hughie.dandao.api;

import com.hughie.dandao.common.util.MedicinalProperties;
import com.mojang.datafixers.util.Pair;

public interface IMedicalItem {
    Pair<MedicinalProperties, Integer> getMedicalProperty();
}
