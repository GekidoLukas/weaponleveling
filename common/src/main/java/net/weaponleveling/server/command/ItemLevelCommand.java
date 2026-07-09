package net.weaponleveling.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.api.LevelingAPI;
import net.weaponleveling.util.ModUtils;

public class ItemLevelCommand {

    private static final DynamicCommandExceptionType NOT_VALID_ITEM = new DynamicCommandExceptionType((value) -> {
        return  Component.translatable("weaponleveling.command.exception.novalidweapon", value);
    });
    private static final DynamicCommandExceptionType OUT_OF_BOUNDS = new DynamicCommandExceptionType((value) -> {
        return  Component.translatable("weaponleveling.command.exception.outofbounds",value);
    });


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,CommandBuildContext commandBuildContex, Commands.CommandSelection commandSelection) {
        dispatcher.register(Commands.literal("itemlevel").requires((permission) -> {
                    return permission.hasPermission(2);
                })
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.literal("level")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                                .executes((command) -> setLevelCommand(command.getSource(), EntityArgument.getPlayer(command, "player"),IntegerArgumentType.getInteger(command,"value"),command))
                                        )
                                )
                                .then(Commands.literal("points")
                                        .then(Commands.argument("value", LongArgumentType.longArg(0))
                                                .executes((command) -> setPointCommand(command.getSource(), EntityArgument.getPlayer(command, "player"), LongArgumentType.getLong(command,"value"),command))
                                        )
                                )

                        )
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.literal("level")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(0,1000))
                                                .executes((command) -> addLevelCommand(command.getSource(), EntityArgument.getPlayer(command, "player"),IntegerArgumentType.getInteger(command,"value"),command))
                                        )
                                )
                                .then(Commands.literal("points")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(0,10000000))
                                                .executes((command) -> addPointCommand(command.getSource(), EntityArgument.getPlayer(command, "player"), IntegerArgumentType.getInteger(command,"value"),command))
                                        )
                                )

                        )
                )
        );
    }



    private static int setLevelCommand(CommandSourceStack source, ServerPlayer player, int level, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ItemStack stack = player.getMainHandItem();
        if (ModUtils.isLevelableItem(stack)) {
            if(level <= ModUtils.getMaxLevel(stack)) {
                LevelingAPI.updateLevel(stack,level);
                source.sendSuccess(() -> {
                    return  Component.translatable("weaponleveling.command.setlevel",stack.getHoverName(),level);
                },true);
            }else {
                throw OUT_OF_BOUNDS.create(level);
            }

        }else {
            throw NOT_VALID_ITEM.create(stack);
        }

        return 1;
    }

    private static int setPointCommand(CommandSourceStack source, ServerPlayer player, long points, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ItemStack stack = player.getMainHandItem();
        if (ModUtils.isLevelableItem(stack)) {
        long maxProgress = LevelingAPI.getMaxProgress(stack);
            if(points <= maxProgress) {
                LevelingAPI.updateLevelProgress(stack,points);
                source.sendSuccess(() -> {
                    return Component.translatable("weaponleveling.command.setpoints",stack.getHoverName(),points);
                },true);
            }else {
                throw OUT_OF_BOUNDS.create(points);
            }

        }else {
            throw NOT_VALID_ITEM.create(stack.getHoverName());
        }
        return 1;
    }


    private static int addLevelCommand(CommandSourceStack source, ServerPlayer player, int level, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ItemStack stack = player.getMainHandItem();
        if (ModUtils.isLevelableItem(stack)) {

            int currentLevel = LevelingAPI.getLevel(stack);
            LevelingAPI.updateLevel(stack, Math.min(currentLevel +level,ModUtils.getMaxLevel(stack)));
            source.sendSuccess(() -> Component.translatable("weaponleveling.command.addlevel",stack.getHoverName(),level),true);

        }else {
            throw NOT_VALID_ITEM.create(stack);
        }

        return 1;
    }

    private static int addPointCommand(CommandSourceStack source, ServerPlayer player, int points, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ItemStack stack = player.getMainHandItem();
        if (ModUtils.isLevelableItem(stack)) {

            LevelingAPI.applyXPToItem(player,stack,points);
            source.sendSuccess(() -> Component.translatable("weaponleveling.command.addpoints",stack.getHoverName(),points),true);

        }else {
            throw NOT_VALID_ITEM.create(stack.getHoverName());
        }
        return 1;
    }
}
