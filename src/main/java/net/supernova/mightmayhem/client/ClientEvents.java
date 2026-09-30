package net.supernova.mightmayhem.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.supernova.mightmayhem.MightMayhem;
import net.supernova.mightmayhem.network.ModMessages;
import net.supernova.mightmayhem.network.RequestQiScreenC2SPacket;

public class ClientEvents {

    // Forge bus: reacts to key presses
    @Mod.EventBusSubscriber(modid = MightMayhem.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinding.OPEN_QI_KEY.consumeClick()) {
                ModMessages.sendToServer(new RequestQiScreenC2SPacket());
            }
        }
    }

    // Mod bus: registers the keybind so it shows in Options > Controls
    @Mod.EventBusSubscriber(modid = MightMayhem.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.OPEN_QI_KEY);
        }

        // Registers the big "MAJOR BREAKTHROUGH!" text that is drawn on top of the game
        @SubscribeEvent
        public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("breakthrough", BreakthroughOverlay.OVERLAY);
        }
    }
}