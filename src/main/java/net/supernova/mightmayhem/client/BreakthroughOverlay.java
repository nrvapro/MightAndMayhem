package net.supernova.mightmayhem.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Random;

/**
 * The big shaking "MAJOR BREAKTHROUGH!" text shown on screen when a player reaches a new realm.
 * Client only. It is drawn on top of the game (not a screen), so the player can keep playing.
 */
public class BreakthroughOverlay {

    // ---- Change these to restyle it ----
    private static final String TITLE = "MAJOR BREAKTHROUGH!";
    private static final int COLOR_TITLE    = 0xFFC857; // gold   (RGB, no alpha)
    private static final int COLOR_SUBTITLE = 0xFFE3E3; // near white
    private static final float TITLE_SCALE    = 5.0F;   // biggest size (it shrinks on small windows so it always fits)
    private static final float SUBTITLE_SCALE = 2.5F;
    private static final float SHAKE_STRENGTH = 5.0F;   // how far it jumps (pixels). 0 = no shake
    private static final long DURATION_MS = 4500;       // total time on screen
    private static final long FADE_IN_MS  = 250;
    private static final long FADE_OUT_MS = 1000;
    private static final float SCREEN_HEIGHT_POSITION = 0.25F; // 0 = top of the screen, 0.5 = middle

    private static final Random RANDOM = new Random();
    private static long startTime = -1;
    private static String subtitle = "";

    /** Starts the animation. Example subtitle: "Peak realm (Entry stage)". */
    public static void show(String realmName, String stageName) {
        subtitle = realmName + " realm" + (stageName.isEmpty() ? "" : " (" + stageName + " stage)");
        startTime = System.currentTimeMillis();
    }

    // Registered in ClientEvents (RegisterGuiOverlaysEvent)
    public static final IGuiOverlay OVERLAY =
            (ForgeGui gui, GuiGraphics g, float partialTick, int width, int height) -> render(g, width, height);

    private static void render(GuiGraphics g, int width, int height) {
        if (startTime < 0) return;

        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= DURATION_MS) {
            startTime = -1;
            return;
        }

        // Fade in -> stay -> fade out
        float fade = 1.0F;
        if (elapsed < FADE_IN_MS) {
            fade = elapsed / (float) FADE_IN_MS;
        } else if (elapsed > DURATION_MS - FADE_OUT_MS) {
            fade = (DURATION_MS - elapsed) / (float) FADE_OUT_MS;
        }
        int alpha = (int) (fade * 255);
        if (alpha <= 8) return; // very low alpha would be drawn fully opaque by the font renderer

        // Shake: jumps to a new random spot every frame, calmer as time passes
        float progress = elapsed / (float) DURATION_MS;
        float shake = SHAKE_STRENGTH * (1.0F - 0.6F * progress);
        float dx = (RANDOM.nextFloat() * 2.0F - 1.0F) * shake;
        float dy = (RANDOM.nextFloat() * 2.0F - 1.0F) * shake;

        // Small "pop": starts a bit bigger and settles
        long popTime = FADE_IN_MS * 2;
        float pop = elapsed < popTime ? 1.0F + 0.4F * (1.0F - elapsed / (float) popTime) : 1.0F;

        Font font = Minecraft.getInstance().font;
        float titleScale = Math.min(TITLE_SCALE, (width - 40) / (float) font.width(TITLE));
        float subtitleScale = Math.min(SUBTITLE_SCALE, (width - 40) / (float) font.width(subtitle));

        int cx = width / 2;
        float y = height * SCREEN_HEIGHT_POSITION;

        RenderSystem.enableBlend();
        drawCentered(g, font, TITLE, cx + dx, y + dy, titleScale * pop, (alpha << 24) | COLOR_TITLE);
        drawCentered(g, font, subtitle, cx + dx * 0.6F,
                y + dy * 0.6F + 9 * titleScale + 10, subtitleScale, (alpha << 24) | COLOR_SUBTITLE);
    }

    private static void drawCentered(GuiGraphics g, Font font, String text, float x, float y, float scale, int argb) {
        g.pose().pushPose();
        g.pose().translate(x, y, 0.0F);
        g.pose().scale(scale, scale, 1.0F);
        g.drawCenteredString(font, text, 0, 0, argb);
        g.pose().popPose();
    }
}
