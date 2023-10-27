package fr.nayz.practice.managers;

import fr.nayz.practice.arenas.Arena;
import fr.nayz.practice.arenas.ArenaStatus;
import fr.nayz.practice.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfigManager {
    private static ConfigManager INSTANCE = new ConfigManager();

    private Location lobby;
    private Location kitEditing;
    private List<Arena> arenas;

    public ConfigManager() {
        INSTANCE = this;

        lobby = new Location(Bukkit.getWorld("lobby"), 4, 23, -75);
        kitEditing = new Location(Bukkit.getWorld("lobby"), 10, 34, -103);

        arenas = new ArrayList<>();

        List<Kit> defaultKits = List.of(Kit.BUILD_UHC, Kit.ARCHER, Kit.SOUP, Kit.NO_DEBUFF, Kit.CLASSIC);

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 508.5, 9, 49.5),
                new Location(Bukkit.getWorld("world"), 508.5, 9, 107.5, 180, 0),
                defaultKits
        ));

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 396.5, 9, 49.5),
                new Location(Bukkit.getWorld("world"), 396.5, 9, 107.5, 180, 0),
                defaultKits
        ));

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 284.5, 9, 49.5),
                new Location(Bukkit.getWorld("world"), 284.5, 9, 107.5, 180, 0),
                defaultKits
        ));

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 172.5, 9, 49.5),
                new Location(Bukkit.getWorld("world"), 172.5, 9, 107.5, 180, 0),
                defaultKits
        ));

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 60.5, 9, 49.5),
                new Location(Bukkit.getWorld("world"), 60.5, 9, 107.5, 180, 0),
                defaultKits
        ));

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 508.5, 9, -42.5),
                new Location(Bukkit.getWorld("world"), 508.5, 9, -100.5, 180, 0),
                defaultKits
        ));

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 396.5, 9, -42.5),
                new Location(Bukkit.getWorld("world"), 396.5, 9, -100.5, 180, 0),
                defaultKits
        ));

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 284.5, 9, -42.5),
                new Location(Bukkit.getWorld("world"), 284.5, 9, -100.5, 180, 0),
                defaultKits
        ));

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 172.5, 9, -42.5),
                new Location(Bukkit.getWorld("world"), 172.5, 9, -100.5, 180, 0),
                defaultKits
        ));

        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 60.5, 9, -42.5),
                new Location(Bukkit.getWorld("world"), 60.5, 9, -100.5, 180, 0),
                defaultKits
        ));

        // Sumo
        arenas.add(new Arena(
                new Location(Bukkit.getWorld("world"), 168.5, 28, 629.5, -90, 0),
                new Location(Bukkit.getWorld("world"), 176.5, 28, 629.5, 90, 0),
                List.of(Kit.SUMO)
        ));
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public Location getLobby() {
        return lobby;
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public Optional<Arena> getPlayerArena(Player player) {
        return arenas.stream().filter(a -> a.getPlayer1() == player|| a.getPlayer2() == player).findFirst();
    }

    public Optional<Arena> getAvailableArena(Kit kit) {
        return arenas.stream().filter(a -> a.getStatus() == ArenaStatus.AVAILABLE && a.getKits().contains(kit)).findFirst();
    }

    public Location getKitEditing() {
        return kitEditing;
    }
}
