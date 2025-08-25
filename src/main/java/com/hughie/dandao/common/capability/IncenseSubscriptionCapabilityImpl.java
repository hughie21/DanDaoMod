package com.hughie.dandao.common.capability;

import com.hughie.dandao.api.IncenseSubscriptionCapability;
import com.hughie.dandao.setup.registry.CommonRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class IncenseSubscriptionCapabilityImpl implements IncenseSubscriptionCapability {
    private BlockPos subscribedPos = null;

    @Override
    public BlockPos getSubscribedPos() {
        return subscribedPos;
    }

    @Override
    public void setSubscribedPos(BlockPos pos) {
        this.subscribedPos = pos;
    }

    @Override
    public void clearSubscription() {
        this.subscribedPos = null;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (subscribedPos != null) {
            tag.putInt("subX", subscribedPos.getX());
            tag.putInt("subY", subscribedPos.getY());
            tag.putInt("subZ", subscribedPos.getZ());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("subX")) {
            this.subscribedPos = new BlockPos(
                    nbt.getInt("subX"),
                    nbt.getInt("subY"),
                    nbt.getInt("subZ")
            );
        } else {
            this.subscribedPos = null;
        }
    }

    public static class Provider implements ICapabilityProvider {
        private final IncenseSubscriptionCapabilityImpl instance = new IncenseSubscriptionCapabilityImpl();
        private final LazyOptional<IncenseSubscriptionCapability> optional = LazyOptional.of(() -> instance);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
            return CommonRegistry.INCENSE_SUBSCRIPTION.orEmpty(cap, optional.cast());
        }
    }
}
