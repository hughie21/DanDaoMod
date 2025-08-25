package com.hughie.dandao.client.network;

import com.hughie.dandao.common.entity.KneadingBoardEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class AnimationFinishedPacket {
    private final BlockPos pos;
    private final UUID playerUUID;

    public AnimationFinishedPacket(BlockPos pos, UUID playerUUID) {
        this.pos = pos;
        this.playerUUID = playerUUID;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUUID(playerUUID);
    }

    public static AnimationFinishedPacket fromBytes(FriendlyByteBuf buf) {
        return new AnimationFinishedPacket(buf.readBlockPos(), buf.readUUID());
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = ctx.get().getSender().level();
            ServerPlayer player = ctx.get().getSender();
            // 获取方块实体并执行合成逻辑
            if (level.getBlockEntity(pos) instanceof KneadingBoardEntity entity) {
                entity.serverSideFinish(player); // 调用服务端专用方法
            }
        });
        ctx.get().setPacketHandled(true);
        return true;
    }
}
