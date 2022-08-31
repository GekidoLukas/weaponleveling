package net.geradesolukas.weaponleveling.networking;

import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.geradesolukas.weaponleveling.networking.message.PacketSendToast;
import net.geradesolukas.weaponleveling.util.CustomToast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.awt.*;
import java.util.function.Supplier;

public class Networking {

    private static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() { return ID++;}

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(WeaponLeveling.MODID, "networking"),
                () -> "1.0",
                s -> true,
                s -> true);

        INSTANCE.messageBuilder(PacketSendToast.class, nextID())
                .encoder(PacketSendToast::encode)
                .decoder(PacketSendToast::decode)
                .consumer(PacketSendToast::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void sendToAllPlayers(Object packet) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
    }




    public static void handleSyncLeveling(PacketSendToast packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        Player player = Minecraft.getInstance().player;
        ItemStack stack = packet.stack;
        int level = packet.level;
        //net.minecraft.network.chat.Component component2 = new net.minecraft.network.chat.TextComponent("Ayo");
        //net.minecraft.network.chat.Component component3 = new net.minecraft.network.chat.TextComponent("This is cool");
        //new SystemToast(SystemToast.SystemToastIds.WORLD_BACKUP, component2, component3)
        Minecraft.getInstance().getToasts().addToast(new CustomToast(stack, level));

    }
}
