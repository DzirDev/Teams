package me.goodwilled.legendstrifes;

public enum Team {
    KNIGHT("IDK, man. A knight."),
    MAGE("You're a wizard, Harry."),
    ARCHER("&6Archers &fare skilled bowsmen and", "are known to be tough to kill."),
    TAMER("You like to watch animals fuck?"),
    CIVILIAN("This person has no powers.");

    private final String[] description;

    Team(String... description) {
        this.description = description;
    }

    public String[] getDescription() {
        return this.description;
    }
}