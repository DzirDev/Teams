package me.goodwilled.legendstrifes;

public enum Legend {
    UNDEAD("Undead", 1.0f),
    VIKING("Viking", 2.0f),
    WIZARD("Wizard", 1.0f),
    NINJA("Ninja", 1.0f);

    private final float damageMultiplier;
    private final String name;

    Legend(String name, float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
        this.name = name;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    @Override
    public String toString() {
        return this.name;
    }
}