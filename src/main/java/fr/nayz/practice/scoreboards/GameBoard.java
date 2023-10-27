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

public class GameBoard extends BukkitRunnable {
    private final static GameBoard INSTANCE = new GameBoard();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

            if (arena.isPresent()) {
                updateScoreboard(player);
            }
        }
    }

    public void createNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective health = scoreboard.registerNewObjective("health", Criteria.HEALTH, Component.text("§c❤"));
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);

        Objective objective = scoreboard.registerNewObjective("game-scoreboard", Criteria.DUMMY, Component.text("§c§lPractice"));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("§1").setScore(7);
        objective.getScore("§8• §b" + player.getName()).setScore(6);
        objective.getScore("§2").setScore(5);

        Team opponent = scoreboard.registerNewTeam("opponent");
        opponent.addEntry("§8» §7Adversaire: ");
        objective.getScore("§8» §7Adversaire: ").setScore(4);

        Team kit = scoreboard.registerNewTeam("kit");
        kit.addEntry("§8» §7Kit: ");
        objective.getScore("§8» §7Kit: ").setScore(3);

        Team mode = scoreboard.registerNewTeam("mode");
        mode.addEntry("§8» §7Mode: ");
        objective.getScore("§8» §7Mode: ").setScore(3);

        objective.getScore("§3").setScore(1);
        objective.getScore("§emc.nayz.fr").setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        Arena arena = ConfigManager.getInstance().getPlayerArena(player).get();

        Team opponent = scoreboard.getTeam("opponent");
        opponent.suffix(Component.text("§d" + (arena.getPlayer1() != player ? arena.getPlayer1().getName() : arena.getPlayer2().getName())));

        Team kit = scoreboard.getTeam("kit");
        kit.suffix(Component.text("§d" + arena.getKit().getName()));

        Team mode = scoreboard.getTeam("mode");
        mode.suffix(Component.text(arena.isRanked() ? "§cRanked" : "§aUnranked"));
    }

    public static GameBoard getInstance() {
        return INSTANCE;
    }
}
