package net.geradesolukas.weaponleveling.networking.message;


import net.geradesolukas.weaponleveling.networking.Networking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSendToast {

    public ItemStack stack;
    public int level;


    public PacketSendToast() {}

    public PacketSendToast(ItemStack stack, int level) {
        this.stack = stack;
        this.level = level;
    }

    public static void encode(PacketSendToast packet, FriendlyByteBuf buf) {
        buf.writeItemStack(packet.stack, true);
        buf.writeInt(packet.level);
    }

    public static PacketSendToast decode(FriendlyByteBuf buf) {
        return new PacketSendToast(buf.readItem(), buf.readInt());
    }

    public static void handle(PacketSendToast packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (packet == null) return;
        context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Networking.handleSendToast(packet, ctx)));
    }
}
