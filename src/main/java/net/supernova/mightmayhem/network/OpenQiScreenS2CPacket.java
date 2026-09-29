package net.supernova.mightmayhem.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.supernova.mightmayhem.client.ClientHooks;

import java.util.function.Supplier;

// Server -> Client: "here is your Qi data, open the screen"
public class OpenQiScreenS2CPacket {
    private final int qi;
    private final int maxQi;
    private final int realm;

    public OpenQiScreenS2CPacket(int qi, int maxQi, int realm) {
        this.qi = qi;
        this.maxQi = maxQi;
        this.realm = realm;
    }

    public OpenQiScreenS2CPacket(FriendlyByteBuf buf) {
        this.qi = buf.readInt();
        this.maxQi = buf.readInt();
        this.realm = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(qi);
        buf.writeInt(maxQi);
        buf.writeInt(realm);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        ClientHooks.openQiScreen(qi, maxQi, realm)));
        context.setPacketHandled(true);
    }
}
