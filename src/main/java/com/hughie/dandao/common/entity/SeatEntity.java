package com.hughie.dandao.common.entity;

import com.hughie.dandao.common.block.ModBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;


public class SeatEntity extends Entity {
    private boolean exiting = false;

    public SeatEntity(EntityType<? extends SeatEntity> type, Level level) {
        super(type,level);
        this.noPhysics = true;
    }



    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();

        // 如果世界是客户端，不执行后续逻辑
        if (this.level().isClientSide) {
            return;
        }

        // 检查是否有乘客
        if (this.getPassengers().isEmpty()) {
            // 没有乘客时移除实体
            this.discard();
            return;
        }

        // 检查方块是否被破坏
        if (this.level().getBlockState(this.blockPosition()).getBlock() != ModBlocks.PUTUAN.get()) {
            // 方块已被破坏，让玩家离开
            this.ejectPassengers();
            this.discard();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false; // 不可碰撞
    }

    @Override
    public boolean startRiding(Entity entity, boolean force) {
        if (super.startRiding(entity, force)) {
            // 调整玩家视角
            if (entity instanceof Player player) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
            }
            return true;
        }
        return false;
    }

    @Override
    public void stopRiding() {
        super.stopRiding();
        this.exiting = true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRender(double x, double y, double z) {
        return false; // 客户端不渲染此实体
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
