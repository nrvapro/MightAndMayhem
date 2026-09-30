package net.supernova.mightmayhem.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.supernova.mightmayhem.client.ClientHooks;

import java.util.function.Supplier;

// Server -> Client: "you just broke through to a new realm, show the big shaking text"
public class RealmBreakthroughS2CPacket {
    private final String realmName;
    private final String stageName; // empty if the realm has no stages

    public RealmBreakthroughS2CPacket(String realmName, String stageName) {
        this.realmName = realmName;
        this.stageName = stageName;
    }

    public RealmBreakthroughS2CPacket(FriendlyByteBuf buf) {
        this.realmName = buf.readUtf();
        this.stageName = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(realmName);
        buf.writeUtf(stageName);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        ClientHooks.showBreakthrough(realmName, stageName)));
        context.setPacketHandled(true);
    }
}