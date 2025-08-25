package com.hughie.dandao.common.item.custom;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.client.renderer.BambooSieveRenderer;
import com.hughie.dandao.common.entity.BambooSieveVirtualBlockEntity;
import com.hughie.dandao.common.sound.ModSounds;
import com.hughie.dandao.common.sound.SoundControl;
import com.hughie.dandao.common.util.BambooSieveDataNBT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class BambooSieveItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation ACTIVE_ANIM = RawAnimation.begin().thenLoop("animation.bamboo_sieve.shake");
    private static final String CONTROLLER_NAME = "sieve_controller";

    private long activeAnimId = -1;
    private SoundControl soundController = new SoundControl();

    public BambooSieveItem(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 200;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        stopAnimation(player);
        soundController.stopCurrentSound();
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BambooSieveVirtualBlockEntity blockEntity = new BambooSieveVirtualBlockEntity();
        blockEntity.setLinkedItem(stack);
        if (!level.isClientSide() && player.isShiftKeyDown()) {
            NetworkHooks.openScreen((ServerPlayer) player, blockEntity, buf -> {});
            return InteractionResultHolder.consume(stack);
        } else if (!player.isShiftKeyDown() && hand.equals(InteractionHand.MAIN_HAND)) {
            if (blockEntity.hasValidRecipe(level)) {
                player.startUsingItem(hand);
                if (!level.isClientSide()) {
                    long animId = GeoItem.getOrAssignId(stack, (ServerLevel) level);
                    triggerAnim(player, animId, CONTROLLER_NAME, "shake");
                    activeAnimId = animId;
                    soundController.playControllableSound(player, ModSounds.BAMBOO_SIEVE_SHAKE.get());
                }
                return InteractionResultHolder.consume(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide() && activeAnimId != -1) {
            triggerAnim(entity, activeAnimId, CONTROLLER_NAME, "shake");
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (level.isClientSide() || !(entity instanceof Player player)) {
            soundController.stopCurrentSound();
            return stack;
        }

        BambooSieveVirtualBlockEntity blockEntity = new BambooSieveVirtualBlockEntity();
        blockEntity.setLinkedItem(stack);

        blockEntity.craftItem(player);

        long animId = GeoItem.getId(stack);
        if (animId != -1) {
            stopAnimation(entity);
        }
        return stack;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        long animId = GeoItem.getId(stack);
        if (animId != -1) {
            stopAnimation(entity);
        }

        soundController.stopCurrentSound();
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        // 玩家松开右键或被迫停止使用时，立即停止动画
        if (!level.isClientSide() && entity instanceof Player) {
            long animId = GeoItem.getId(stack);
            if (animId != -1) {
                stopAnimation(entity);
            }
        }
        soundController.stopCurrentSound();
    }

    private void stopAnimation(LivingEntity entity) {
        if (activeAnimId != -1) {
            triggerAnim(entity, activeAnimId, CONTROLLER_NAME, "stop");
            activeAnimId = -1;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, CONTROLLER_NAME, state -> PlayState.STOP)
                .triggerableAnim("shake", ACTIVE_ANIM)
                .triggerableAnim("stop", RawAnimation.begin())
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BambooSieveRenderer renderer = null;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new BambooSieveRenderer();
                return renderer;
            }
        });
    }
}
