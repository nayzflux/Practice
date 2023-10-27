package fr.nayz.commons.ranks;

import java.util.Arrays;

public enum Rank {
    ADMIN(0, 1000, "Admin", "§cAdmin", "§c[Administrateur] ", "§6", -1),
    MODERATOR(1, 200, "Modérateur", "§9Modérateur", "§9[Modérateur] ", "§6", -1),
    BUILDER(2, 100, "Buildeur", "§6Buildeur", "§6[Buildeur] ", "§6", -1),
    VIP_PLUS(3, 20, "VIP+", "§bVIP+", "§b[VIP+] ", "§f", -1),
    VIP(4, 10, "VIP", "§eVIP", "§e[VIP]", "§f", -1),
    PLAYER(5, 1, "Joueur", "Joueur", "§7", "§7", -1);

    private int id;
    private int power;
    private String name;
    private String displayName;
    private String chatPrefix;
    private String chatColor;
    private int duration;

    Rank(int id, int power, String name, String displayName, String chatPrefix, String chatColor, int duration) {
        this.id = id;
        this.power = power;
        this.name = name;
        this.displayName = displayName;
        this.chatPrefix = chatPrefix;
        this.chatColor = chatColor;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public int getPower() {
        return power;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public String getChatColor() {
        return chatColor;
    }

    public int getDuration() {
        return duration;
    }

    public static Rank find(String name) {
        return Arrays.stream(Rank.values()).filter(r -> r.getName().equalsIgnoreCase(name)).findFirst().orElse(PLAYER);
    }

    public static Rank find(int id) {
        return Arrays.stream(Rank.values()).filter(r -> r.getId() == id).findFirst().orElse(PLAYER);
    }
}
