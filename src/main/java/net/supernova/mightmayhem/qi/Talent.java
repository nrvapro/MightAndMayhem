package net.supernova.mightmayhem.qi;

import net.minecraft.util.RandomSource;

/**
 * Talent: a hidden value from 1 to 10 rolled once per player.
 * The higher the talent, the rarer it is, and the more Qi the player gains.
 */
public final class Talent {
    public static final int MIN = 1;
    public static final int MAX = 10;

    // ---- Qi multiplier, in tenths (5 = x0.5, 3 = +0.3) ----
    // Talent 1 gives BASE_TENTHS, and every talent level after that adds STEP_TENTHS.
    private static final int BASE_TENTHS = 5; // talent 1 = x0.5
    private static final int STEP_TENTHS = 3; // +0.3 per talent level -> talent 10 = x3.2

    // Chance out of 1000 for talent 1, 2, 3 ... 10. These MUST add up to 1000.
    // Talent 10 = 1 in 1000.
    private static final int[] WEIGHTS = {300, 250, 180, 120, 70, 40, 25, 10, 4, 1};
    private static final int TOTAL_WEIGHT = sum(WEIGHTS);

    private Talent() {
    }

    private static int sum(int[] values) {
        int total = 0;
        for (int v : values) total += v;
        return total;
    }

    /** Rolls a random talent using the rarity table above. */
    public static int roll(RandomSource random) {
        int pick = random.nextInt(TOTAL_WEIGHT);
        int cumulative = 0;
        for (int talent = MIN; talent <= MAX; talent++) {
            cumulative += WEIGHTS[talent - 1];
            if (pick < cumulative) return talent;
        }
        return MAX; // never reached
    }

    public static int clamp(int talent) {
        return Math.max(MIN, Math.min(talent, MAX));
    }

    /** Qi multiplier in tenths (integers avoid rounding errors). */
    public static int getMultiplierTenths(int talent) {
        return BASE_TENTHS + STEP_TENTHS * (clamp(talent) - 1);
    }

    public static double getMultiplier(int talent) {
        return getMultiplierTenths(talent) / 10.0;
    }
}
