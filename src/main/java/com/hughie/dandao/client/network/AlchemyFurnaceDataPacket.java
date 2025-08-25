package com.hughie.dandao.client.network;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.entity.AlchemyFurnaceBlockEntity;
import com.hughie.dandao.common.screen.AlchemyFurnaceMenu;
import com.hughie.dandao.common.util.LockedSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AlchemyFurnaceDataPacket {
    private final BlockPos pos;

    public AlchemyFurnaceDataPacket(BlockPos pos) {
        this.pos = pos;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static AlchemyFurnaceDataPacket decode(FriendlyByteBuf buf) {
        return new AlchemyFurnaceDataPacket(buf.readBlockPos());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            AlchemyFurnaceBlockEntity be = (AlchemyFurnaceBlockEntity) ctx.get().getSender().level().getBlockEntity(pos);
            if (be != null) {
                be.toggleButtonState();

                boolean isOpen = be.isOpen();
                boolean isLocked = !isOpen;

                if (player.containerMenu instanceof AlchemyFurnaceMenu menu) {
                    int playerSlotsCount = 36;
                    for(int i = 4; i < 12; i++) {
                        Slot slot = menu.getSlot(i + playerSlotsCount);
                        if (slot instanceof LockedSlot lockedSlot) {
                            if (isOpen) {
                                lockedSlot.unlock();
                            } else {
                                lockedSlot.lock();
                            }
                        }
                    }
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    DanDao.NETWORK.sendTo(
                            new SyncAlchemyFurnaceLockPacket(pos, isLocked),
                            serverPlayer.connection.connection,
                            NetworkDirection.PLAY_TO_CLIENT
                    );
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
