package net.supernova.mightmayhem.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.supernova.mightmayhem.client.KeyBinding;
import net.supernova.mightmayhem.qi.Realm;

import java.util.Locale;

public class QiScreen extends Screen {
    private static final int PANEL_W = 220;
    private static final int BASE_PANEL_H = 150;

    private final int qi;
    private final int maxQi;
    private final int realmLevel;

    public QiScreen(int qi, int maxQi, int realmLevel) {
        super(Component.literal("Cultivation"));
        this.qi = qi;
        this.maxQi = maxQi;
        this.realmLevel = realmLevel;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);

        Realm realm = Realm.byLevel(realmLevel);
        boolean showHearts = realm.getBonusHearts() > 0;
        int panelH = BASE_PANEL_H + (showHearts ? 12 : 0);

        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - panelH) / 2;
        int cx = this.width / 2;

        // Panel with border
        g.fill(x - 1, y - 1, x + PANEL_W + 1, y + panelH + 1, 0xFF8A6FD1);
        g.fill(x, y, x + PANEL_W, y + panelH, 0xEE14101F);

        g.drawCenteredString(this.font, "Cultivation", cx, y + 10, 0xFFD9A8FF);
        g.drawCenteredString(this.font, "Realm: " + realm.getDisplayName(), cx, y + 28, 0xFFFFD700);
        g.drawCenteredString(this.font, "Qi: " + qi + " / " + maxQi, cx, y + 44, 0xFFFFFFFF);

        // Qi bar
        int barX = x + 20;
        int barY = y + 60;
        int barW = PANEL_W - 40;
        int barH = 10;
        g.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, 0xFF000000);
        g.fill(barX, barY, barX + barW, barY + barH, 0xFF2A2A3A);
        int filled = maxQi > 0 ? (int) ((long) barW * qi / maxQi) : 0;
        g.fill(barX, barY, barX + Math.min(filled, barW), barY + barH, 0xFF55C8FF);

        // Next realm
        Realm next = realm.next();
        String nextText = next == null
                ? "Highest realm reached"
                : "Next: " + next.getDisplayName() + " at " + next.getQiRequired() + " Qi";
        g.drawCenteredString(this.font, nextText, cx, y + 78, 0xFFAAAAAA);

        // Bonuses
        int lineY = y + 94;
        if (showHearts) {
            g.drawCenteredString(this.font, "+" + realm.getBonusHearts() + " max hearts", cx, lineY, 0xFF7CFC7C);
            lineY += 12;
        }
        g.drawCenteredString(this.font,
                "+" + String.format(Locale.ROOT, "%.1f", realm.getBonusDamage()) + " attack damage",
                cx, lineY, 0xFF7CFC7C);
        lineY += 12;
        g.drawCenteredString(this.font,
                "+" + Math.round(realm.getBonusSpeed() * 100) + "% movement speed",
                cx, lineY, 0xFF7CFC7C);
        lineY += 12;
        g.drawCenteredString(this.font,
                Math.round(realm.getDamageReduction() * 100) + "% damage reduction",
                cx, lineY, 0xFF7CFC7C);

        g.drawCenteredString(this.font, "Press B to close", cx, y + panelH - 16, 0xFF777777);

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