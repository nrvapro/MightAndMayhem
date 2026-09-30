package net.supernova.mightmayhem.client;

import net.minecraft.client.Minecraft;
import net.supernova.mightmayhem.client.screen.QiScreen;

// Client-only helper (kept separate so the server never loads client classes)
public class ClientHooks {
    public static void openQiScreen(int qi, int maxQi, int realm) {
        Minecraft.getInstance().setScreen(new QiScreen(qi, maxQi, realm));
    }

    public static void showBreakthrough(String realmName, String stageName) {
        BreakthroughOverlay.show(realmName, stageName);
    }
}