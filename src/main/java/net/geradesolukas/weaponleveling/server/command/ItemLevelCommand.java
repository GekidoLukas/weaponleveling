package net.geradesolukas.weaponleveling.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class ItemLevelCommand {
    private static final DynamicCommandExceptionType NOT_VALID_ITEM = new DynamicCommandExceptionType((value) -> {
        return new TranslatableText("weaponleveling.command.exception.novalidweapon",value);
    });
    private static final DynamicCommandExceptionType OUT_OF_BOUNDS = new DynamicCommandExceptionType((value) -> {
        return new TranslatableText("weaponleveling.command.exception.outofbounds",value);
    });


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(CommandManager.literal("itemlevel").requires((permission) -> {
            return permission.hasPermissionLevel(2);
        })
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("player", EntityArgumentType.players())
                        .then(CommandManager.argument("value", IntegerArgumentType.integer(0))
                        .then(CommandManager.literal("level").executes((command) -> {
                            return setLevelCommand(command.getSource(), EntityArgumentType.getPlayer(command, "player"),IntegerArgumentType.getInteger(command,"value"),command);
                        }))

                        .then(CommandManager.literal("points").executes((command) -> {
                            return setPointCommand(command.getSource(), EntityArgumentType.getPlayer(command, "player"),IntegerArgumentType.getInteger(command,"value"),command);
                        }))))));
    }



    private static int setLevelCommand(ServerCommandSource source, ServerPlayerEntity player, int level, CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack stack = player.getMainHandStack();
        if (UpdateLevels.isAcceptedMeleeWeaponStack(stack) || UpdateLevels.isAcceptedArmor(stack)|| UpdateLevels.isAcceptedProjectileWeapon(stack)) {
            if(level <= WeaponlevelingConfig.Server.value_max_level.get()) {
                stack.getOrCreateNbt().putInt("level", level);
                source.sendFeedback(new TranslatableText("weaponleveling.command.setlevel",stack.getName(),level),true);
            }else {
                throw OUT_OF_BOUNDS.create(level);
            }

        }else {
            throw NOT_VALID_ITEM.create(stack);
        }

        return 1;
    }

    private static int setPointCommand(ServerCommandSource source, ServerPlayerEntity player, int points, CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack stack = player.getMainHandStack();
        int level = stack.getOrCreateNbt().getInt("levelprogress");
        int maxprogress = UpdateLevels.getMaxLevel(level);
        if (UpdateLevels.isAcceptedMeleeWeaponStack(stack) || UpdateLevels.isAcceptedArmor(stack)) {
            if(points <= maxprogress) {
                stack.getOrCreateNbt().putInt("levelprogress", points);
                source.sendFeedback(new TranslatableText("weaponleveling.command.setpoints",stack.getName(),points),true);
            }else {
                throw OUT_OF_BOUNDS.create(points);
            }

        }else {
            throw NOT_VALID_ITEM.create(stack.getName());
        }
        return 1;
    }

}
