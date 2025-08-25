package com.hughie.dandao.common.entity;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.ModBlocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DanDao.MOD_ID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENETITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DanDao.MOD_ID);

    public static final RegistryObject<EntityType<SeatEntity>> SEAT = ENTITIES.register("seat",
            () -> EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                    .sized(0.01F, 0.01F) // 极小的碰撞箱，几乎不可见
                    .clientTrackingRange(10)
                    .build("seat"));

    public static final RegistryObject<BlockEntityType<MortarBlockEntity>> MORTAR_ENT = BLOCK_ENETITIES.register("mortar_ent",
            () -> BlockEntityType.Builder.of(MortarBlockEntity::new, ModBlocks.MORTAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<IronMillBlockEntity>> IRON_MILL_ENT = BLOCK_ENETITIES.register("iron_mill_ent",
            () -> BlockEntityType.Builder.of(IronMillBlockEntity::new, ModBlocks.IRON_MILL.get()).build(null));

    public static final RegistryObject<BlockEntityType<AlchemyFurnaceBlockEntity>> ALCHEMY_FURNACE = BLOCK_ENETITIES.register("alchemy_furnace",
            () -> BlockEntityType.Builder.of(AlchemyFurnaceBlockEntity::new, ModBlocks.ALCHEMY_FURNACE.get()).build(null));

    public static final RegistryObject<BlockEntityType<KneadingBoardEntity>> KNEADING_BOARD = BLOCK_ENETITIES.register("kneading_board",
            () -> BlockEntityType.Builder.of(KneadingBoardEntity::new, ModBlocks.KNEADING_BOARD.get()).build(null));

    public static final RegistryObject<BlockEntityType<IncenseBurnerBlockEntity>> INCENSE_BURNER = BLOCK_ENETITIES.register("incense_burner",
            () -> BlockEntityType.Builder.of(IncenseBurnerBlockEntity::new, ModBlocks.INCENSE_BURNER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BambooSieveVirtualBlockEntity>> BAMBOO_SIEVE_VIRTUAL_BLOCK_ENTITY = BLOCK_ENETITIES.register("bamboo_virtual_block_entity",
            () -> BlockEntityType.Builder.of((blockPos, state) -> new BambooSieveVirtualBlockEntity(), ModBlocks.BAMBOO_SIEVE_VIRTUAL_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
        BLOCK_ENETITIES.register(eventBus);
    }
}
