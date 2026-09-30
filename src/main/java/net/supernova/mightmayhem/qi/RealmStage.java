package net.supernova.mightmayhem.qi;

/**
 * One stage inside a realm (for example "Entry", "Proficient", "Peak").
 *
 * qiRequired = the total Qi the player needs to be in this stage.
 * The other four numbers are the TOTAL bonuses the player has while in this stage
 * (same meaning as the bonuses on a realm line in Realm.java).
 */
public record RealmStage(String name, int qiRequired,
                         int hearts, float damage, double speed, float reduction) {
}
