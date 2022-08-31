package net.geradesolukas.weaponleveling.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemLevelCommand {
    private static final DynamicCommandExceptionType NOT_VALID_ITEM = new DynamicCommandExceptionType((value) -> {
        return new TranslatableComponent("weaponleveling.command.exception.novalidweapon",value);
    });
    private static final DynamicCommandExceptionType OUT_OF_BOUNDS = new DynamicCommandExceptionType((value) -> {
        return new TranslatableComponent("weaponleveling.command.exception.outofbounds",value);
    });


    public ItemLevelCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("itemlevel").requires((permission) -> {
            return permission.hasPermission(2);
        })
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                        .then(Commands.literal("level").executes((command) -> {
                            return setLevelCommand(command.getSource(), EntityArgument.getPlayer(command, "player"),IntegerArgumentType.getInteger(command,"value"),command);
                        }))

                        .then(Commands.literal("points").executes((command) -> {
                            return setPointCommand(command.getSource(), EntityArgument.getPlayer(command, "player"),IntegerArgumentType.getInteger(command,"value"),command);
                        }))))));
    }



    private int setLevelCommand(CommandSourceStack source, ServerPlayer player, int level, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ItemStack stack = player.getMainHandItem();
        if (UpdateLevels.isAcceptedWeapon(stack) || UpdateLevels.isAcceptedArmor(stack)) {
            if(level <= WeaponLevelingConfig.Server.value_max_level.get()) {
                stack.getOrCreateTag().putInt("level", level);
                source.sendSuccess(new TranslatableComponent("weaponleveling.command.setlevel",stack.getHoverName(),level),true);
            }else {
                throw OUT_OF_BOUNDS.create(level);
            }

        }else {
            throw NOT_VALID_ITEM.create(stack);
        }

        return 1;
    }

    private int setPointCommand(CommandSourceStack source, ServerPlayer player, int points, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ItemStack stack = player.getMainHandItem();
        int level = stack.getOrCreateTag().getInt("levelprogress");
        int maxprogress = UpdateLevels.getMaxLevel(level);
        if (UpdateLevels.isAcceptedWeapon(stack) || UpdateLevels.isAcceptedArmor(stack)) {
            if(points <= maxprogress) {
                stack.getOrCreateTag().putInt("levelprogress", points);
                source.sendSuccess(new TranslatableComponent("weaponleveling.command.setpoints",stack.getHoverName(),points),true);
            }else {
                throw OUT_OF_BOUNDS.create(points);
            }

        }else {
            throw NOT_VALID_ITEM.create(stack.getHoverName());
        }
        return 1;
    }
}
