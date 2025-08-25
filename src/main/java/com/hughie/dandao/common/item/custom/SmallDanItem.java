package com.hughie.dandao.common.item.custom;

import com.hughie.dandao.common.sound.ModSounds;
import com.hughie.dandao.common.sound.SoundControl;
import com.hughie.dandao.common.util.PlayerUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmallDanItem extends Item {
    protected final int maxUses;
    protected final int nutrition = 1;
    protected final float saturation = 0.25F;
    protected final List<MobEffectInstance> effects;

    public SmallDanItem(Item.Properties pProperty, int maxUses, List<MobEffectInstance> effects) {
        super(pProperty.stacksTo(1).durability(maxUses - 1));
        this.maxUses = maxUses;
        this.effects = effects;
    }

    public SmallDanItem(List<MobEffectInstance> effects) {
        this(new Properties(), 5, effects);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 20;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return ModSounds.EMPTY_SOUND.get();
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        // 计算总使用时长
        int totalDuration = getUseDuration(stack);
        // 当使用刚开始的第一帧（剩余时间 = 总时长 - 1）时播放音效
        if (remainingUseDuration == totalDuration / 2) {
            if (entity instanceof Player player && !level.isClientSide) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        ModSounds.TAKING_MEDICATION.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() < getMaxDamage(stack)) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (entity instanceof Player player) {
                player.getFoodData().eat(nutrition, saturation);
                for (MobEffectInstance effect : effects) {
                    player.addEffect(new MobEffectInstance(effect));
                }

                // 检查是否是最后一次使用
                int newDamage = stack.getDamageValue() + 1;
                if (newDamage >= getMaxDamage(stack)) {
                    // 如果是最后一次使用，消耗当前物品并给予空瓶子
                    stack.shrink(1);
                    PlayerUtils.givePlayerItems(player, new ItemStack(Items.GLASS_BOTTLE));
                    return stack;
                } else {
                    // 不是最后一次使用，正常减少耐久
                    stack.setDamageValue(newDamage);
                }
            }
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(itemStack, level, tooltipComponents, isAdvanced);
        PotionUtils.addPotionTooltip(effects, tooltipComponents, 1.0F);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return stack.getDamageValue() > 0;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxUses - 1;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }
}
