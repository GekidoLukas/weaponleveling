package net.weaponleveling.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PrintTagListsCommand {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,CommandBuildContext commandBuildContex, Commands.CommandSelection commandSelection) {
        dispatcher.register(Commands.literal("print_tag_list").requires((permission) -> {
                    return permission.hasPermission(2);
                })
                .then(Commands.literal("item")
                        .then(Commands.literal("selected")
                                .executes((command) -> printItemListSelected(command.getSource()))

                        )
                        .then(Commands.literal("of")
                                .then(Commands.argument("value", ItemArgument.item(commandBuildContex))
                                        .executes((command) -> printItemList(command.getSource(),ItemArgument.getItem(command,"value").createItemStack(1,false)))
                                )
                        )
                )
                .then(Commands.literal("entity")
                        .then(Commands.literal("looking")
                                .executes((command) -> printEntityListLooking(command.getSource()))

                        )
                        .then(Commands.literal("of")
                                .then(Commands.argument("value", ResourceArgument.resource(commandBuildContex, Registries.ENTITY_TYPE))
                                        .executes((command) -> printEntityList(command.getSource(),ResourceArgument.getEntityType(command,"value").value()))
                                )
                        )
                )
        );
    }


    private static int printItemListSelected(CommandSourceStack source) throws CommandSyntaxException {
        if(source.isPlayer()) {
            ServerPlayer serverPlayer = source.getPlayer();
            printItemList(source,serverPlayer.getMainHandItem());
        }

        return 1;
    }
    private static int printItemList(CommandSourceStack source, ItemStack stack) throws CommandSyntaxException {

        Set<TagKey<Item>> set = stack.getTags().collect(Collectors.toSet());


        source.sendSuccess(() ->
                Component.translatable("weaponleveling.command.print_tag_list.success",
                        stack.getDisplayName().copy().withStyle(ChatFormatting.YELLOW),
                        ComponentUtils.formatList(set.stream().map(itemTagKey -> "#" + itemTagKey.location().toString()).toList())
                ), false);

        return 1;
    }


    private static int printEntityListLooking(CommandSourceStack source) throws CommandSyntaxException {
        if(source.isPlayer()) {
            ServerPlayer serverPlayer = source.getPlayer();
            Optional<Entity> looking = getLookingAtEntity(serverPlayer,5.0d);
            if(looking.isPresent()) {
                printEntityList(source,looking.get().getType());
            } else {
                source.sendFailure(Component.translatable("weaponleveling.command.print_tag_list.failure.no_entity_in_view"));
            }
        }

        return 1;
    }
    private static int printEntityList(CommandSourceStack source, EntityType<?> type) throws CommandSyntaxException {

        Set<TagKey<EntityType<?>>> set = BuiltInRegistries.ENTITY_TYPE
                .getHolderOrThrow(BuiltInRegistries.ENTITY_TYPE.getResourceKey(type).orElseThrow())
                .tags()
                .collect(Collectors.toSet());

        source.sendSuccess(() ->
                Component.translatable("weaponleveling.command.print_tag_list.success",
                        type.getDescription().copy().withStyle(ChatFormatting.YELLOW),
                        ComponentUtils.formatList(set.stream().map(itemTagKey -> "#" + itemTagKey.location().toString()).toList())
                ), false);

        return 1;
    }


    private static Optional<Entity> getLookingAtEntity(ServerPlayer player, double range) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getViewVector(1.0F);
        Vec3 reachVec = eyePos.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

        AABB box = player.getBoundingBox()
                .expandTowards(lookVec.scale(range))
                .inflate(1.0D, 1.0D, 1.0D);

        EntityHitResult result = ProjectileUtil.getEntityHitResult(player.level(),player,eyePos,reachVec,box,entity -> !entity.isSpectator());

        if (result != null) {
            return Optional.of(result.getEntity());
        }

        return Optional.empty();
    }
}
