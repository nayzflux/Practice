package fr.nayz.practice.scoreboards;

import fr.nayz.practice.arenas.Arena;
import fr.nayz.practice.managers.ConfigManager;
import fr.nayz.practice.managers.QueueManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.Optional;

public class LobbyBoard extends BukkitRunnable {
    private final static LobbyBoard INSTANCE = new LobbyBoard();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

            if (arena.isEmpty()) {
                updateScoreboard(player);
            }
        }
    }

    public void createNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("lobby-scoreboard", Criteria.DUMMY, Component.text("§c§lPractice"));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("§1").setScore(7);
        objective.getScore("§8• §b" + player.getName()).setScore(6);
        objective.getScore("§2").setScore(5);
        objective.getScore("§8» §aLobby").setScore(4);

        Team playerCount = scoreboard.registerNewTeam("player-count");
        playerCount.addEntry("§8» §7Joueurs: ");
        playerCount.suffix(Component.text("§d" + Bukkit.getOnlinePlayers().size()));
        objective.getScore("§8» §7Joueurs: ").setScore(3);

        Team queueCount = scoreboard.registerNewTeam("queue-count");
        queueCount.addEntry("§8» §7File d'attente: ");
        queueCount.suffix(Component.text("§d" + QueueManager.getInstance().getQueueSize()));
        objective.getScore("§8» §7File d'attente: ").setScore(3);

        objective.getScore("§3").setScore(1);
        objective.getScore("§emc.nayz.fr").setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team playerCount = scoreboard.getTeam("player-count");
        playerCount.suffix(Component.text("§d" + Bukkit.getOnlinePlayers().size()));
        Team queueCount = scoreboard.getTeam("queue-count");
        queueCount.suffix(Component.text("§d" + QueueManager.getInstance().getQueueSize()));
    }

    public static LobbyBoard getInstance() {
        return INSTANCE;
    }
}
