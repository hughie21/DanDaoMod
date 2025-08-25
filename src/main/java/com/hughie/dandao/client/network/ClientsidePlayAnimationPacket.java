package com.hughie.dandao.client.network;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.api.IAniBlock;
import com.hughie.dandao.common.entity.IronMillBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientsidePlayAnimationPacket {
    private final BlockPos pos;

    public ClientsidePlayAnimationPacket(BlockPos pos) {
        this.pos = pos;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }

    public static ClientsidePlayAnimationPacket decode(FriendlyByteBuf buffer) {
        return new ClientsidePlayAnimationPacket(buffer.readBlockPos());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            if (level.getBlockEntity(pos) instanceof IAniBlock block) {
//                    ironMillBlockEntity.setShouldPlayAnimate(true);
                    DanDao.LOGGER.info("[Packet Animation] Received for pos={}, found: {}", pos, true);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
