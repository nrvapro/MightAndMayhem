package net.supernova.mightmayhem.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.supernova.mightmayhem.qi.PlayerQiProvider;

import java.util.function.Supplier;

// Client -> Server: "I pressed B, send me my Qi data"
public class RequestQiScreenC2SPacket {

    public RequestQiScreenC2SPacket() {
    }

    public RequestQiScreenC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            player.getCapability(PlayerQiProvider.PLAYER_QI).ifPresent(qi ->
                    ModMessages.sendToPlayer(
                            new OpenQiScreenS2CPacket(qi.getQi(), qi.getMaxQi(), qi.getRealm()), player));
        });
        context.setPacketHandled(true);
    }
}
