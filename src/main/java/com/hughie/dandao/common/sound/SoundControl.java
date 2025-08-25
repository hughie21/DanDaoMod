package com.hughie.dandao.common.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoundControl {
    private SoundInstance currentSound;

    public void playControllableSound(Player player, SoundEvent soundEvent) {
        if (currentSound != null) {
            stopCurrentSound();
        }

        currentSound = new SimpleSoundInstance(
                soundEvent,
                SoundSource.PLAYERS,
                1.0F, // 音量
                1.0F, // 音调
                player.getRandom(), // 随机数生成器
                player.blockPosition() // 声音位置
        );

        Minecraft.getInstance().getSoundManager().play(currentSound);
    }

    public void stopCurrentSound() {
        if (currentSound != null) {
            Minecraft.getInstance().getSoundManager().stop(currentSound);
            currentSound = null;
        }
    }
}
