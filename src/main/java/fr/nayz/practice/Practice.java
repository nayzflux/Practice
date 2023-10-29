package fr.nayz.practice;

import fr.nayz.commons.pratices.PracticeKit;
import fr.nayz.practice.arenas.Arena;
import fr.nayz.practice.commands.DuelCommand;
import fr.nayz.practice.gui.*;
import fr.nayz.practice.listeners.ArenaListener;
import fr.nayz.practice.listeners.PlayerChatListener;
import fr.nayz.practice.listeners.PlayerListener;
import fr.nayz.practice.managers.ConfigManager;
import fr.nayz.practice.scoreboards.GameBoard;
import fr.nayz.practice.scoreboards.LobbyBoard;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Practice extends JavaPlugin {
    private static Practice INSTANCE;

    private Map<Player, Player> duels;
    private Map<Player, PracticeKit> duelKits;
    private Map<Player, PracticeKit> editingKits;

    @Override
    public void onEnable() {
        INSTANCE = this;

        duels = new HashMap<>();
        duelKits = new HashMap<>();
        editingKits  = new HashMap<>();

        saveDefaultConfig();

        FileConfiguration config = getConfig();

        ConfigurationSection databaseSection = config.getConfigurationSection("database");
        String db = databaseSection.getString("db");
        String host = databaseSection.getString("host");
        int port = databaseSection.getInt("port");
        String user = databaseSection.getString("user");
        String password = databaseSection.getString("password");

        registerEvents();
        registerCommands();

        PracticeKit.init();

        LobbyBoard.getInstance().runTaskTimer(this, 0, 20);
        GameBoard.getInstance().runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        for (Arena arena : ConfigManager.getInstance().getArenas()) {
            arena.reset();
        }
    }

    public static Practice getInstance() {
        return INSTANCE;
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new ArenaListener(), this);
        pm.registerEvents(new PlayerChatListener(), this);

        pm.registerEvents(new UnrankedGui(), this);
        pm.registerEvents(new RankedGui(), this);
        pm.registerEvents(new DuelGui(), this);
        pm.registerEvents(new ProfileGui(), this);
        pm.registerEvents(new EditingKitsGui(), this);
    }

    private void registerCommands() {
        getCommand("duel").setExecutor(new DuelCommand());
    }

    public Map<Player, Player> getDuels() {
        return duels;
    }

    public Map<Player, PracticeKit> getDuelKits() {
        return duelKits;
    }

    public Map<Player, PracticeKit> getEditingKits() {
        return editingKits;
    }

}
