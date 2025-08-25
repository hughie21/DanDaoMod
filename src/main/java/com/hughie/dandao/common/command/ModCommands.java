package com.hughie.dandao.common.command;

import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.util.MedicinalProperties;
import com.hughie.dandao.common.util.MedicinalPropertiesNBT;
import com.hughie.dandao.common.util.PlayerUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ModCommands {
    private static final Collection<String> ALLOWED_TYPE = Arrays.asList("cold", "cool", "warm", "hot", "default");
    private static final Collection<String> ALLOWED_ITEM = Arrays.asList("medicinal_powder", "dan_embryo");

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_TYPES = (context, builder) -> SharedSuggestionProvider.suggest(ALLOWED_TYPE, builder);

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_ITEMS = (context, builder) -> SharedSuggestionProvider.suggest(ALLOWED_ITEM, builder);

    private static final LiteralArgumentBuilder<CommandSourceStack> giveItemCommand = Commands.literal("dandao:give")
            .then(Commands.argument("item", StringArgumentType.string())
                    .suggests(SUGGEST_ITEMS)
                    .then(Commands.argument("type", StringArgumentType.string())
                            .suggests(SUGGEST_TYPES)
                            .then(Commands.argument("level", IntegerArgumentType.integer(0, 9))
                                    .executes(commandContext -> addItem(
                                            commandContext,
                                            StringArgumentType.getString(commandContext, "item"),
                                            StringArgumentType.getString(commandContext, "type"),
                                            IntegerArgumentType.getInteger(commandContext, "level")
                                    ))
                            )
                    )
            );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(giveItemCommand);
    }

    private static int addItem(CommandContext<CommandSourceStack> context, String item, String type, int level) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Map<String, Item> itemMap = Map.of(
                "medicinal_powder", ModItems.MEDICINAL_POWDER.get(),
                "dan_embryo", ModItems.DAN_EMBRYO.get()
        );

        if(!ALLOWED_ITEM.contains(item) || !ALLOWED_TYPE.contains(type)) {
            context.getSource().sendFailure(Component.translatable("commands.dandao.error.invaild_value"));
            return 0;
        }

        ItemStack itemStack = new ItemStack(itemMap.get(item));

        MedicinalPropertiesNBT.setPropertyLevel(itemStack, MedicinalProperties.getFromName(type), level);
        PlayerUtils.givePlayerItems(player, itemStack);
        return 1;
    }
}
