package com.hughie.dandao.common.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;

public class ExplosionUtils {
    public static Explosion createTNTLikeExplosion(Level level, double x, double y, double z,
                                                   float power, boolean causesFire, boolean breaksBlocks,
                                                   Entity source) {
        if (level.isClientSide()) {
            return null;
        }

        Explosion explosion = new Explosion(level, source, x, y, z, power, causesFire,
                breaksBlocks ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP);

        ExplosionEvent.Start event = new ExplosionEvent.Start(level, explosion);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return null; // 如果事件被取消，则不执行爆炸
        }

        explosion.explode();
        explosion.finalizeExplosion(true);

        level.addParticle(
                ParticleTypes.EXPLOSION,
                x, y, z,
                0.5, 0.5, 0.5
        );
        level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1);

        return explosion;
    }
}
