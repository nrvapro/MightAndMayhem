package net.supernova.mightmayhem.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.supernova.mightmayhem.client.KeyBinding;
import net.supernova.mightmayhem.qi.Realm;
import net.supernova.mightmayhem.qi.RealmStage;

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
    private static final int COLOR_BAR_LABEL   = 0xFFE8A0A0; // next realm name at the end of the bar
    private static final int COLOR_NEXT_REALM  = 0xFFC48A8A; // "Next: ..."
    private static final int COLOR_BONUS       = 0xFFFF9E80; // bonus lines + first realm message
    private static final int COLOR_HINT        = 0xFF9A5C5C; // "Press B to close"
    private static final int COLOR_STAGE_TICK    = 0xFF8A4A4A; // stage marker not reached yet
    private static final int COLOR_STAGE_REACHED = 0xFFFFC857; // stage marker already reached

    private static final String FIRST_REALM_MESSAGE = "Cultivate to reach your first realm !";

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

    // Draws text whose RIGHT edge is at rightX
    private void drawTextRight(GuiGraphics g, String text, int rightX, float y, float scale, int color) {
        g.pose().pushPose();
        g.pose().translate((float) rightX, y, 0.0F);
        g.pose().scale(scale, scale, 1.0F);
        g.drawString(this.font, text, -this.font.width(text), 0, color);
        g.pose().popPose();
    }

    // Draws the Qi bar
    private void drawBar(GuiGraphics g, int barX, int barY, int barW, int barH) {
        g.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, COLOR_BAR_BORDER);
        g.fill(barX, barY, barX + barW, barY + barH, COLOR_BAR_BG);
        int filled = maxQi > 0 ? (int) ((long) barW * qi / maxQi) : 0;
        filled = Math.min(filled, barW);
        if (filled > 0) {
            g.fill(barX, barY, barX + filled, barY + barH, COLOR_BAR_FILL);
            g.fill(barX, barY, barX + filled, barY + 2, COLOR_BAR_SHINE);
        }
    }

    // Draws one little vertical bar + name for each stage of the realm, at its place on the Qi bar
    private void drawStageMarkers(GuiGraphics g, Realm realm, int barX, int barY, int barW, int barH, float k) {
        if (maxQi <= 0) return;
        for (RealmStage stage : realm.getStages()) {
            int x = barX + (int) ((long) barW * stage.qiRequired() / maxQi);
            x = Math.max(barX + 1, Math.min(x, barX + barW - 1));
            int color = qi >= stage.qiRequired() ? COLOR_STAGE_REACHED : COLOR_STAGE_TICK;
            g.fill(x - 1, barY - 3, x + 1, barY + barH + 3, color);
            drawText(g, stage.name(), x, barY + barH + 5 * k, 0.8F * k, color);
        }
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
        Realm next = realm.next();
        boolean noRealmYet = realm.getLevel() == 0; // still Mortal
        int stageIndex = realm.getStageIndex(qi); // 0 for realms without stages
        boolean showHearts = realm.getBonusHearts(stageIndex) > 0;

        // Shrinks everything on small windows / big GUI scales so it always fits
        float k = Math.min(1.0F, this.height / 300.0F);
        int cx = this.width / 2;
        int barW = Math.min(this.width - 100, 360);
        int barH = Math.max(8, Math.round(14 * k));
        int barX = cx - barW / 2;

        if (noRealmYet) {
            // Before the first realm: ONLY the message (plus the empty bar under it)
            float msgScale = Math.min(2.0F * k,
                    (this.width - 40) / (float) this.font.width(FIRST_REALM_MESSAGE));
            float msgHeight = 9 * msgScale;
            float gap = 24 * k;
            float y = (this.height - (msgHeight + gap + barH)) / 2.0F;

            drawText(g, FIRST_REALM_MESSAGE, cx, y, msgScale, COLOR_BONUS);
            drawBar(g, barX, Math.round(y + msgHeight + gap), barW, barH);
        } else {
            boolean hasStages = realm.hasStages();
            float stageRoom = hasStages ? 12.0F : 0.0F; // space for the stage names under the bar
            float total = ((showHearts ? 226.0F : 210.0F) + stageRoom) * k;
            float y = (this.height - total) / 2.0F;

            // Title + realm + Qi numbers
            drawText(g, "Cultivation", cx, y, 3.0F * k, COLOR_TITLE);
            y += 36 * k;
            String realmText = "Realm: " + realm.getDisplayName();
            if (hasStages) realmText += " - " + realm.getStage(qi).name();
            drawText(g, realmText, cx, y, 2.0F * k, COLOR_REALM);
            y += 28 * k;
            drawText(g, "Qi: " + qi + " / " + maxQi, cx, y, 1.5F * k, COLOR_QI_TEXT);
            y += 24 * k;

            // Qi bar + name of the next realm at the end of the bar
            int barY = Math.round(y);
            drawBar(g, barX, barY, barW, barH);
            if (hasStages) {
                drawStageMarkers(g, realm, barX, barY, barW, barH, k);
            }
            if (next != null) {
                drawTextRight(g, next.getDisplayName(), barX + barW, barY - 11 * k, 1.0F * k, COLOR_BAR_LABEL);
            }
            y += barH + (16 + stageRoom) * k;

            // Next realm requirement
            String nextText;
            if (hasStages && stageIndex < realm.getStages().size() - 1) {
                RealmStage nextStage = realm.getStages().get(stageIndex + 1);
                nextText = "Next stage: " + nextStage.name() + " at " + nextStage.qiRequired() + " Qi";
            } else if (next == null) {
                nextText = "Highest realm reached";
            } else {
                nextText = "Next: " + next.getDisplayName() + " at " + next.getQiRequired() + " Qi";
            }
            drawText(g, nextText, cx, y, 1.2F * k, COLOR_NEXT_REALM);
            y += 34 * k;

            // Bonuses
            float bonusScale = 1.3F * k;
            float step = 17 * k;
            if (showHearts) {
                drawText(g, "+" + realm.getBonusHearts(stageIndex) + " max hearts", cx, y, bonusScale, COLOR_BONUS);
                y += step;
            }
            drawText(g, "+" + String.format(Locale.ROOT, "%.1f", realm.getBonusDamage(stageIndex)) + " attack damage",
                    cx, y, bonusScale, COLOR_BONUS);
            y += step;
            drawText(g, "+" + Math.round(realm.getBonusSpeed(stageIndex) * 100) + "% movement speed",
                    cx, y, bonusScale, COLOR_BONUS);
            y += step;
            drawText(g, Math.round(realm.getDamageReduction(stageIndex) * 100) + "% damage reduction",
                    cx, y, bonusScale, COLOR_BONUS);
        }

        // Hint (kept above the health/hotbar area)
        drawText(g, "Press B to close", cx, this.height - 58, 1.0F, COLOR_HINT);

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