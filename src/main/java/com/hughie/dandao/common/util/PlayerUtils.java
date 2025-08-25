package com.hughie.dandao.common.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerUtils {
    public static void givePlayerItems(Player player, ItemStack itemStack) {
        boolean flag = player.getInventory().add(itemStack);

        if(flag && itemStack.isEmpty()) {
            itemStack.setCount(1);
            ItemEntity itemStackEntity = player.drop(itemStack, false);
            if(itemStackEntity != null) {
                itemStackEntity.makeFakeItem();
            }

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.containerMenu.broadcastChanges();
        }else {
            ItemEntity itemStackEntity = player.drop(itemStack, false);
            if(itemStackEntity != null) {
                itemStackEntity.setNoPickUpDelay();
                itemStackEntity.setTarget(player.getUUID());
            }
        }
    }
}
