package com.hughie.dandao.client.network;

import com.hughie.dandao.common.screen.AlchemyFurnaceMenu;
import com.hughie.dandao.common.util.LockedSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncAlchemyFurnaceLockPacket {
    private final BlockPos pos;
    private final boolean isLocked;

    public SyncAlchemyFurnaceLockPacket(BlockPos pos, boolean isLocked) {
        this.pos = pos;
        this.isLocked = isLocked;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isLocked);
    }

    public static SyncAlchemyFurnaceLockPacket decode(FriendlyByteBuf buf) {
        return new SyncAlchemyFurnaceLockPacket(buf.readBlockPos(), buf.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                AbstractContainerMenu containerMenu = player.containerMenu;
                if (containerMenu instanceof AlchemyFurnaceMenu alchemyMenu) {
                    if (alchemyMenu.alchemyFurnaceBlockEntity.getBlockPos().equals(pos)) {
                        int playerSlotsCount = 36;
                        for (int i = 4; i < 12; i++) {
                            Slot slot = alchemyMenu.getSlot(i + playerSlotsCount);
                            if (slot instanceof LockedSlot lockedSlot) {
                                if (isLocked) {
                                    lockedSlot.lock();
                                } else {
                                    lockedSlot.unlock();
                                }
                            }
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
