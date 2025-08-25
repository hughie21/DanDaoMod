package com.hughie.dandao.setup.registry;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.effect.ModEffects;
import com.hughie.dandao.common.effect.SuraEffect;
import com.hughie.dandao.common.effect.ToDieHardEffect;
import com.hughie.dandao.common.entity.IncenseBurnerBlockEntity;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.hughie.dandao.common.util.MedicinalPropertiesNBT;
import com.hughie.dandao.common.util.PlayerUtils;
import com.hughie.dandao.common.viliager.ModVillagers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.*;

import static com.hughie.dandao.common.effect.SuraEffect.killCounts;

@Mod.EventBusSubscriber(modid = DanDao.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onPlayerXpGain(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        if (player.hasEffect(ModEffects.XP_BOOST.get())) {
            int originalXp = event.getAmount();
            event.setAmount(2 * originalXp);
        }
    }

    @SubscribeEvent
    public static void onPlayerFirstJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().getGameProfile().getId() != null &&
        !event.getEntity().getPersistentData().getBoolean("HasJoinedBefore")) {
            event.getEntity().getPersistentData().putBoolean("HasJoinedBefore", true);
            ItemStack book = PatchouliAPI.get().getBookStack(ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "dandao_guide_book"));
            PlayerUtils.givePlayerItems(event.getEntity(), book);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof  Player player) {
            CommonRegistry.getSubscriptionCapability(player).ifPresent(subscription -> {
                BlockPos pos = subscription.getSubscribedPos();
                if (pos != null) {
                    Level level = player.level();
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof IncenseBurnerBlockEntity incenseEntity) {
                        incenseEntity.removeListenerByPlayer(player);
                    }
                    subscription.clearSubscription();
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        checkTDHEffect(event);
        checkDamageEffect(event);
    }

    public static void checkDamageEffect(LivingHurtEvent event) {
        Entity attacker = event.getSource().getEntity();
        float MAX_BOOST_MULTIPLIER = 3f;
        float MAX_MUTI_BOOST_MULTIPLIER = 8f;
        if (attacker instanceof Player player) {
            if (player.hasEffect(ModEffects.NO_TURNING_BACK.get()) && player.hasEffect(ModEffects.SURA.get())) {
                float healthPercentage = player.getHealth() / player.getMaxHealth();
                float ntbDamageMultiplier = 1.0f + (MAX_BOOST_MULTIPLIER - 1.0f) * (1.0f - healthPercentage);
                float suraDamageMultiplier = SuraEffect.getDamageBoost(player);
                float originalDamage = event.getAmount();
                float dilutionMultiplier = Math.min(ntbDamageMultiplier + suraDamageMultiplier, MAX_MUTI_BOOST_MULTIPLIER);
                float boostedDamage = originalDamage * dilutionMultiplier;
                event.setAmount(boostedDamage);
                return;
            }
            if (player.hasEffect(ModEffects.NO_TURNING_BACK.get())) {
                float healthPercentage = player.getHealth() / player.getMaxHealth();
                float damageMultiplier = 1.0f + (MAX_BOOST_MULTIPLIER - 1.0f) * (1.0f - healthPercentage);
                float originalDamage = event.getAmount();
                float boostedDamage = originalDamage * damageMultiplier;
                event.setAmount(boostedDamage);
            }else if (player.hasEffect(ModEffects.SURA.get())) {
                float damageMultiplier = SuraEffect.getDamageBoost(player);
                float originalDamage = event.getAmount();
                float boostedDamage = originalDamage * damageMultiplier;
                event.setAmount(boostedDamage);
            }
        }
    }

    public static void checkTDHEffect(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            // 检查玩家是否有该效果
            if (!player.hasEffect(ModEffects.TO_DIE_HARD_INACTIVE.get()) && !player.hasEffect(ModEffects.TO_DIE_HARD_ACTIVE.get())) {
                return;
            }

            float currentHealth = player.getHealth();
            float damage = event.getAmount();

            // 如果伤害会导致死亡，调整为保留1点血
            if (currentHealth - damage <= 0) {
                event.setAmount(0);
                if(player.hasEffect(ModEffects.TO_DIE_HARD_INACTIVE.get())) {
                    player.removeEffect(ModEffects.TO_DIE_HARD_INACTIVE.get());
                    player.addEffect(new MobEffectInstance(ModEffects.TO_DIE_HARD_ACTIVE.get(), 15*20));
                }
                player.setHealth(1);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpire(MobEffectEvent.Expired event) {
        if (event.getEntity() instanceof Player player) {
            if(event.getEffectInstance().getEffect() == ModEffects.TO_DIE_HARD_ACTIVE.get()) {
                if(player.getHealth() != player.getMaxHealth()) {
                    player.setHealth(0);
                }
            } else if (event.getEffectInstance().getEffect() == ModEffects.SURA.get()) {
                SuraEffect.resetKillCount(player);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if(event.getEffectInstance() == null) {
            return;
        }
        if (event.getEntity() instanceof Player player) {
            if(event.getEffectInstance().getEffect() == ModEffects.TO_DIE_HARD_ACTIVE.get()) {
                event.setCanceled(true);
            }else if (event.getEffectInstance().getEffect() == ModEffects.SURA.get()) {
                SuraEffect.resetKillCount(player);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        checkSURAEffect(event);
        checkSLEvent(event);
    }

    public static void checkSLEvent(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (!player.hasEffect(ModEffects.SOUL_LOCK.get())) {
                return;
            }
            event.setCanceled(true);
            player.setHealth(5f);
            player.removeEffect(ModEffects.SOUL_LOCK.get());
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 15*20, 5));
        }
    }

    public static void checkSURAEffect(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();
        LivingEntity killer = target.getKillCredit();

        if(killer instanceof Player player) {
            if(!player.hasEffect(ModEffects.SURA.get())) {
                return;
            }
            UUID playerId = player.getUUID();
            int currentKills = killCounts.getOrDefault(playerId, 0);
            killCounts.put(playerId, currentKills + 1);
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity instanceof Player player) {
            if(player.hasEffect(ModEffects.HEARTBROKEN.get())) {
                float healing = event.getAmount();
                float beforeHealth = player.getHealth();
                float maxHealth = player.getMaxHealth() / 2;

                if(beforeHealth + healing > maxHealth) {
                    event.setAmount(maxHealth - beforeHealth);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEffectApplying(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player && player.hasEffect(ModEffects.POISON_IMMUNE.get())) {
            MobEffect thisEffect = event.getEffectInstance().getEffect();
            if (!thisEffect.isBeneficial() && !(thisEffect instanceof ToDieHardEffect.ToDieHardEffectActive)) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == ModEffects.HEARTBROKEN.get() &&
                event.getEntity() instanceof Player player) {
            float maxHealth = player.getMaxHealth();
            float halfHealth = maxHealth / 2;
            float currentHealth = player.getHealth();

            if (currentHealth > halfHealth) {
                player.setHealth(halfHealth);
            }
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // level 1
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.BLOOD_BERRY_SEEDS.get(), 5),
                    12, 8, 0.02f
            ));

            // level 2
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.STONE_FLOWER_SEED.get(), 2),
                    12, 8, 0.02f
            ));

            // level 3
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),
                    new ItemStack(ModItems.SNOW_GINSENG_SEEDS.get(), 1),
                    12, 8, 0.02f
            ));
        }

        if(event.getType() == ModVillagers.HERBALIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // Level1
            trades.get(1).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, random.nextInt(1, 4)),
                    new ItemStack(ModItems.GINSENG.get(), random.nextInt(1,5)),
                    6, 8, 0.02f
            )));

            trades.get(1).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, random.nextInt(1, 4)),
                    new ItemStack(ModItems.SULFUR.get(), random.nextInt(1,5)),
                    6, 8, 0.02f
            )));

            trades.get(1).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, random.nextInt(1, 4)),
                    new ItemStack(ModItems.REALGAR.get(), random.nextInt(1,5)),
                    12, 8, 0.02f
            )));

            trades.get(1).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, random.nextInt(1, 4)),
                    new ItemStack(ModItems.SULFUR.get(), random.nextInt(1,5)),
                    12, 8, 0.02f
            )));

            trades.get(1).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(ModItems.CINNABAR.get(), random.nextInt(1,5)),
                    new ItemStack(Items.EMERALD, random.nextInt(1, 4)),
                    5, 8, 0.02f
            )));

            // Level2
            trades.get(2).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, random.nextInt(2, 8)),
                    new ItemStack(ModItems.GROUND_MOSS.get(), random.nextInt(2,4)),
                    8, 8, 0.02f
            )));

            trades.get(2).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, random.nextInt(2, 8)),
                    new ItemStack(ModItems.CINNABAR.get(), random.nextInt(2,6)),
                    8, 8, 0.02f
            )));

            trades.get(2).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(ModItems.SULFUR.get(), random.nextInt(5, 10)),
                    new ItemStack(Items.EMERALD, random.nextInt(1, 5)),
                    8, 8, 0.02f
            )));

            // Level3
            trades.get(3).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, random.nextInt(2, 6)),
                    new ItemStack(ModItems.SNOW_LOTUS.get(), random.nextInt(1, 3)),
                    6, 8, 0.02f
            )));

            trades.get(3).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, random.nextInt(2, 6)),
                    new ItemStack(ModItems.ORPIMENT.get(), random.nextInt(2, 3)),
                    6, 8, 0.02f
            )));

            trades.get(3).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(ModItems.REALGAR.get(), random.nextInt(1, 5)),
                    new ItemStack(ModItems.MICA.get(), random.nextInt(4, 8)),
                    new ItemStack(ModItems.ORPIMENT.get(), random.nextInt(2, 3)),
                    6, 8, 0.02f
            )));

            // Level4
            trades.get(4).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(ModItems.SPIRIT_STONE.get(), random.nextInt(1,4)),
                    getRandomMedPropItem(random),
                    6, 8, 0.03f
            )));

            // Level5
            trades.get(5).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, random.nextInt(10, 20)),
                    new ItemStack(ModItems.SPIRIT_STONE.get(), random.nextInt(1, 4)),
                    5, 16, 0.075f
            )));

            trades.get(5).add(((trader, random) -> new MerchantOffer(
                    new ItemStack(ModItems.SUN_LEAF.get(), random.nextInt(1, 2)),
                    new ItemStack(ModItems.FLARE_FRUIT.get(), random.nextInt(2, 5)),
                    new ItemStack(ModItems.SPIRIT_STONE.get(), random.nextInt(5, 8)),
                    5, 16, 0.075f
            )));
         }
    }

    private static ItemStack getRandomMedPropItem(RandomSource randomSource) {
        ItemStack medPowder = new ItemStack(ModItems.MEDICINAL_POWDER.get());
        Map<Integer, MedicinalProperties> propList = Map.of(
                0, MedicinalProperties.HOT,
                1, MedicinalProperties.WARM,
                2, MedicinalProperties.COOL,
                3, MedicinalProperties.COLD
        );
        MedicinalPropertiesNBT.setPropertyLevel(medPowder, propList.get(randomSource.nextInt(0,3)), randomSource.nextInt(0, 9));

        return medPowder;
    }

    @SubscribeEvent
    public static void addCustomWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrade = event.getRareTrades();

        rareTrade.add(((entity, randomSource) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, randomSource.nextInt(5,10)),
                new ItemStack(ModItems.FLARE_FRUIT_SEEDS.get(), randomSource.nextInt(1, 2)),
                2, 2, 0.2f
        )));

        rareTrade.add(((entity, randomSource) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 6),
                new ItemStack(ModItems.SPIRIT_STONE.get()),
                5, 16, 0.5f
        )));

    }
}
