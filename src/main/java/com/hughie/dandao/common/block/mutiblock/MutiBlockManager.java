package com.hughie.dandao.common.block.mutiblock;

import com.hughie.dandao.api.IMultiBlock;

import java.util.HashMap;
import java.util.Map;

public class MutiBlockManager {
    private static Map<String, IMultiBlock> MUTI_BLOCK_LIST;

    private MutiBlockManager() {
        MUTI_BLOCK_LIST = new HashMap<>();
    }

    public static void init() {
        MutiBlockManager manager = new MutiBlockManager();
        manager.add("alchemy furnace", new MutiBlockAlchemyFurnace());
    }

    public static Map<String, IMultiBlock> getMutiBlockList() {
        return Map.copyOf(MUTI_BLOCK_LIST);
    }

    public void add(String key, IMultiBlock multiBlock) {
        MUTI_BLOCK_LIST.put(key, multiBlock);
    }
}
