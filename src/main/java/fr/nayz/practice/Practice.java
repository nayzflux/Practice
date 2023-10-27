package fr.nayz.practice;

import fr.nayz.commons.accounts.Account;
import fr.nayz.commons.accounts.PracticeData;
import fr.nayz.practice.arenas.Arena;
import fr.nayz.practice.commands.BugCommand;
import fr.nayz.practice.commands.DuelCommand;
import fr.nayz.practice.gui.*;
import fr.nayz.practice.kits.Kit;
import fr.nayz.practice.listeners.AccountListener;
import fr.nayz.practice.listeners.ArenaListener;
import fr.nayz.practice.listeners.PlayerChatListener;
import fr.nayz.practice.listeners.PlayerListener;
import fr.nayz.practice.managers.ConfigManager;
import fr.nayz.practice.mysql.MySQL;
import fr.nayz.practice.scoreboards.GameBoard;
import fr.nayz.practice.scoreboards.LobbyBoard;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Practice extends JavaPlugin {
    private static Practice INSTANCE;

    private List<PracticeData> statistics;

    private Map<Player, Player> duels;
    private Map<Player, Kit> duelKits;
    private MySQL mysql;
    private Map<Player, Kit> editingKits;
    private List<Account> accounts;

    @Override
    public void onEnable() {
        INSTANCE = this;

        statistics = new ArrayList<>();
        duels = new HashMap<>();
        duelKits = new HashMap<>();
        editingKits  = new HashMap<>();

        accounts = new ArrayList<>();

        saveDefaultConfig();

        FileConfiguration config = getConfig();

        ConfigurationSection databaseSection = config.getConfigurationSection("database");
        String db = databaseSection.getString("db");
        String host = databaseSection.getString("host");
        int port = databaseSection.getInt("port");
        String user = databaseSection.getString("user");
        String password = databaseSection.getString("password");

        mysql = new MySQL(host, port, db, user, password);
        mysql.connect();

        Bukkit.getWorld("world").setHardcore(true);
        Bukkit.getWorld("world").setDifficulty(Difficulty.HARD);
        Bukkit.getWorld("world").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bukkit.getWorld("world").setGameRule(GameRule.NATURAL_REGENERATION, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_FIRE_TICK, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_MOB_SPAWNING, false);

        registerEvents();
        registerCommands();

        Kit.init();

        LobbyBoard.getInstance().runTaskTimer(this, 0, 20);
        GameBoard.getInstance().runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        for (Arena arena : ConfigManager.getInstance().getArenas()) {
            arena.reset();
        }

        for (PracticeData data : statistics) {
            data.save();
        }

        mysql.disconnect();
    }

    public static Practice getInstance() {
        return INSTANCE;
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new ArenaListener(), this);
        pm.registerEvents(new PlayerChatListener(), this);

        pm.registerEvents(new AccountListener(), this);

        pm.registerEvents(new UnrankedGui(), this);
        pm.registerEvents(new RankedGui(), this);
        pm.registerEvents(new DuelGui(), this);
        pm.registerEvents(new ProfileGui(), this);
        pm.registerEvents(new EditingKitsGui(), this);
    }

    private void registerCommands() {
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("bugs").setExecutor(new BugCommand());
    }

    public List<PracticeData> getStatistics() {
        return statistics;
    }

    public PracticeData findPlayerStatistics(Player player) {
        return statistics.stream().filter(s -> s.getUuid() == player.getUniqueId()).findFirst().orElse(null);
    }

    public Map<Player, Player> getDuels() {
        return duels;
    }

    public Map<Player, Kit> getDuelKits() {
        return duelKits;
    }

    public MySQL getMySQL() {
        return mysql;
    }

    public Map<Player, Kit> getEditingKits() {
        return editingKits;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Optional<Account> findAccount(Player player) {
        return accounts.stream().filter(a -> a.getUuid() == player.getUniqueId()).findFirst();
    }
}
