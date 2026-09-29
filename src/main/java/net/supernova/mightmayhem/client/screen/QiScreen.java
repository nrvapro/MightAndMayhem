package net.supernova.mightmayhem.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.supernova.mightmayhem.client.KeyBinding;
import net.supernova.mightmayhem.qi.Realm;

import java.util.Locale;

public class QiScreen extends Screen {

    // ---- Colors (0xAARRGGBB) - change these to restyle the whole screen ----
    // Background opacity: the first two digits (99 = about 60%). Lower = more see-through.
    private static final int COLOR_BACKGROUND  = 0x99200609; // full-screen dark red tint
    private static final int COLOR_BORDER      = 0xFF9E1B1B; // frame around the screen
    private static final int COLOR_TITLE       = 0xFFFF5C5C; // "Cultivation"
    private static final int COLOR_REALM       = 0xFFFFC857; // realm name (gold)
    private static final int COLOR_QI_TEXT     = 0xFFFFE3E3; // "Qi: x / y"
    private static final int COLOR_BAR_BORDER  = 0xFF000000;
    private static final int COLOR_BAR_BG      = 0xFF3A1216; // empty part of the bar
    private static final int COLOR_BAR_FILL    = 0xFFD62828; // filled part of the bar
    private static final int COLOR_BAR_SHINE   = 0xFFFF6B6B; // thin highlight on top of the fill
    private static final int COLOR_NEXT_REALM  = 0xFFC48A8A; // "Next: ..."
    private static final int COLOR_BONUS       = 0xFFFF9E80; // bonus lines
    private static final int COLOR_HINT        = 0xFF9A5C5C; // "Press B to close"

    private final int qi;
    private final int maxQi;
    private final int realmLevel;

    public QiScreen(int qi, int maxQi, int realmLevel) {
        super(Component.literal("Cultivation"));
        this.qi = qi;
        this.maxQi = maxQi;
        this.realmLevel = realmLevel;
    }

    // Draws centered text at a custom size
    private void drawText(GuiGraphics g, String text, int cx, float y, float scale, int color) {
        g.pose().pushPose();
        g.pose().translate((float) cx, y, 0.0F);
        g.pose().scale(scale, scale, 1.0F);
        g.drawCenteredString(this.font, text, 0, 0, color);
        g.pose().popPose();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // Full-screen see-through tint (we skip renderBackground so the world stays visible)
        g.fill(0, 0, this.width, this.height, COLOR_BACKGROUND);

        // Frame around the screen
        int m = 10;
        int t = 2;
        g.fill(m, m, this.width - m, m + t, COLOR_BORDER);
        g.fill(m, this.height - m - t, this.width - m, this.height - m, COLOR_BORDER);
        g.fill(m, m, m + t, this.height - m, COLOR_BORDER);
        g.fill(this.width - m - t, m, this.width - m, this.height - m, COLOR_BORDER);

        Realm realm = Realm.byLevel(realmLevel);
        boolean showHearts = realm.getBonusHearts() > 0;

        // Shrinks everything on small windows / big GUI scales so it always fits
        float k = Math.min(1.0F, this.height / 300.0F);
        float total = (showHearts ? 226.0F : 210.0F) * k;
        float y = (this.height - total) / 2.0F;
        int cx = this.width / 2;

        // Title + realm + Qi numbers
        drawText(g, "Cultivation", cx, y, 3.0F * k, COLOR_TITLE);
        y += 36 * k;
        drawText(g, "Realm: " + realm.getDisplayName(), cx, y, 2.0F * k, COLOR_REALM);
        y += 28 * k;
        drawText(g, "Qi: " + qi + " / " + maxQi, cx, y, 1.5F * k, COLOR_QI_TEXT);
        y += 24 * k;

        // Qi bar (wide)
        int barW = Math.min(this.width - 100, 360);
        int barH = Math.max(8, Math.round(14 * k));
        int barX = cx - barW / 2;
        int barY = Math.round(y);
        g.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, COLOR_BAR_BORDER);
        g.fill(barX, barY, barX + barW, barY + barH, COLOR_BAR_BG);
        int filled = maxQi > 0 ? (int) ((long) barW * qi / maxQi) : 0;
        filled = Math.min(filled, barW);
        if (filled > 0) {
            g.fill(barX, barY, barX + filled, barY + barH, COLOR_BAR_FILL);
            g.fill(barX, barY, barX + filled, barY + 2, COLOR_BAR_SHINE);
        }
        y += barH + 16 * k;

        // Next realm
        Realm next = realm.next();
        String nextText = next == null
                ? "Highest realm reached"
                : "Next: " + next.getDisplayName() + " at " + next.getQiRequired() + " Qi";
        drawText(g, nextText, cx, y, 1.2F * k, COLOR_NEXT_REALM);
        y += 34 * k;

        // Bonuses
        float bonusScale = 1.3F * k;
        float step = 17 * k;
        if (showHearts) {
            drawText(g, "+" + realm.getBonusHearts() + " max hearts", cx, y, bonusScale, COLOR_BONUS);
            y += step;
        }
        drawText(g, "+" + String.format(Locale.ROOT, "%.1f", realm.getBonusDamage()) + " attack damage",
                cx, y, bonusScale, COLOR_BONUS);
        y += step;
        drawText(g, "+" + Math.round(realm.getBonusSpeed() * 100) + "% movement speed",
                cx, y, bonusScale, COLOR_BONUS);
        y += step;
        drawText(g, Math.round(realm.getDamageReduction() * 100) + "% damage reduction",
                cx, y, bonusScale, COLOR_BONUS);

        // Hint at the bottom
        drawText(g, "Press B to close", cx, this.height - 28, 1.0F, COLOR_HINT);

        super.render(g, mouseX, mouseY, partialTick);
    }

    // Pressing B again closes the screen
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (KeyBinding.OPEN_QI_KEY.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}