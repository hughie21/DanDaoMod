package com.hughie.dandao.common.screen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.block.ModBlocks;
import com.hughie.dandao.common.entity.BambooSieveVirtualBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, DanDao.MOD_ID);

    public static final RegistryObject<MenuType<MortarMenu>> MORTAR_MENU =
            registerMenuType("mortar_menu", MortarMenu::new);

    public static final RegistryObject<MenuType<IronMillMenu>> IRON_MILL_MENU =
            registerMenuType("iron_mill_menu", IronMillMenu::new);

    public static final RegistryObject<MenuType<KneadingBoardMenu>> KNEADING_BOARD_MENU =
            registerMenuType("kneading_board_menu", KneadingBoardMenu::new);

    public static final RegistryObject<MenuType<AlchemyFurnaceMenu>> ALCHEMY_FURNACE_MENU =
            registerMenuType("alchemy_furnace_menu", AlchemyFurnaceMenu::new);

    public static final RegistryObject<MenuType<BambooSieveMenu>> BAMBOO_SIEVE_MENU =
            registerMenuType("bamboo_sieve_menu", (windowId, inventory, data) -> {
                Player player = inventory.player;
                BambooSieveVirtualBlockEntity blockEntity = new BambooSieveVirtualBlockEntity();

                ItemStack stack = player.getMainHandItem();
                blockEntity.setLinkedItem(stack);

                blockEntity.loadFromItemNBT();

                return new BambooSieveMenu(windowId, inventory, blockEntity);
            });

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
