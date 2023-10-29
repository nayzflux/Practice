package fr.nayz.practice.managers;

import fr.nayz.practice.arenas.Arena;
import fr.nayz.commons.pratices.PracticeKit;
import fr.nayz.practice.utils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class QueueManager {
    private static QueueManager INSTANCE = new QueueManager();

    private Map<PracticeKit, List<Player>> rankedQueue;
    private Map<PracticeKit, List<Player>> unrankedQueue;

    public QueueManager() {
        INSTANCE = this;

        rankedQueue = new HashMap<>();
        unrankedQueue = new HashMap<>();

        for (PracticeKit kit : PracticeKit.values()) {
            if (kit.isRanked()) {
                rankedQueue.put(kit, new ArrayList<>());
            }

            unrankedQueue.put(kit, new ArrayList<>());
        }
    }

    public void matchmaking(PracticeKit kit, boolean ranked) {
        if (ranked) {
            Bukkit.getLogger().info("Matchamaking (ranked)");

            if (rankedQueue.get(kit).size() < 2) return;

            Player player1 = rankedQueue.get(kit).get(0);
            Player player2 = rankedQueue.get(kit).get(1);

            rankedQueue.get(kit).remove(player2);
            rankedQueue.get(kit).remove(player1);

            Optional<Arena> arena = ConfigManager.getInstance().getAvailableArena(kit);

            if (arena.isEmpty()) {
                player1.sendMessage("§f» §cAucune arène n'est disponible pour le moment, merci de patienter");
                player2.sendMessage("§f» §cAucune arène n'est disponible pour le moment, merci de patienter");
                return;
            }

            arena.get().setKit(kit);
            arena.get().setPlayer1(player1);
            arena.get().setPlayer2(player2);
            arena.get().setRanked(true);
            arena.get().start();

            if (rankedQueue.get(kit).size() >= 2) {
                matchmaking(kit, true);
            }

        } else {
            Bukkit.getLogger().info("Matchamaking");

            if (unrankedQueue.get(kit).size() < 2) return;

            Player player1 = unrankedQueue.get(kit).get(0);
            Player player2 = unrankedQueue.get(kit).get(1);

            unrankedQueue.get(kit).remove(player2);
            unrankedQueue.get(kit).remove(player1);

            Optional<Arena> arena = ConfigManager.getInstance().getAvailableArena(kit);

            if (arena.isEmpty()) {
                player1.sendMessage("§f» §cAucune arène n'est disponible pour le moment, merci de patienter");
                player2.sendMessage("§f» §cAucune arène n'est disponible pour le moment, merci de patienter");
                return;
            }

            arena.get().setKit(kit);
            arena.get().setPlayer1(player1);
            arena.get().setPlayer2(player2);
            arena.get().setRanked(false);
            arena.get().start();

            if (unrankedQueue.get(kit).size() >= 2) {
                matchmaking(kit, false);
            }
        }
    }

//    public Player getNearEloPlayer(Player player, Kit kit, int tolerance) {
//        PracticeStatistics stats = Practice.getInstance().findPlayerStatistics(player);
//        float elo = stats.getElo(kit);
//
//        for (Player opponent : rankedQueue.get(kit)) {
//            if (player != opponent) {
//                PracticeStatistics oStats = Practice.getInstance().findPlayerStatistics(opponent);
//                float oElo = oStats.getElo(kit);
//
//                if (oElo >= elo - tolerance &&  oElo <= elo + tolerance) {
//                    Bukkit.getLogger().info("[Matchmaking] (Ranked) Player found in range -+" + tolerance);
//                    return player;
//                }
//            }
//        }
//
//        Bukkit.getLogger().info("[Matchmaking] (Ranked) Not found in range -+" + tolerance + ", expanding range");
//
//        try {
//            wait(20000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        if (tolerance >= 2000) return null;
//
//        return getNearEloPlayer(player, kit, (tolerance + 300));
//    }

    public void join(Player player, PracticeKit kit, boolean ranked) {
        if (isInQueue(player)) {
            GameUtils.playSound(player, Sound.ENTITY_VILLAGER_NO, 1F, 1F);
            GameUtils.sendSectionMessage(player, "§7» §cVous êtes déjà dans une file d'attente");
            return;
        }

        GameUtils.playSound(player, Sound.BLOCK_LEVER_CLICK, 1F, 1F);
        GameUtils.sendSectionMessage(player, "§8» §aVous avez rejoint la file d'attente pour le kit " + kit.getName() + " (" + (ranked ? "Ranked" : "Unranked") + ")");

        GameUtils.reset(player);
        GameUtils.giveQueueItem(player, kit, ranked);

        if (ranked) {
            rankedQueue.get(kit).add(player);
        } else {
            unrankedQueue.get(kit).add(player);
        }

        matchmaking(kit, ranked);
    }

    public void leave(Player player) {
        GameUtils.playSound(player, Sound.ENTITY_VILLAGER_NO, 1F, 1F);
        GameUtils.sendSectionMessage(player, "§8» §aVous avez quitté la file d'attente");

        GameUtils.reset(player);
        GameUtils.giveLobbyItem(player);

        for (Map.Entry<PracticeKit, List<Player>> entry : unrankedQueue.entrySet()) {
            if (entry.getValue().contains(player)) {
                unrankedQueue.get(entry.getKey()).remove(player);
                return;
            }
        }

        for (Map.Entry<PracticeKit, List<Player>> entry : rankedQueue.entrySet()) {
            if (entry.getValue().contains(player)) {
                rankedQueue.get(entry.getKey()).remove(player);
                return;
            }
        }
    }

    public boolean isInQueue(Player player) {
        for (Map.Entry<PracticeKit, List<Player>> entry : unrankedQueue.entrySet()) {
            if (entry.getValue().contains(player)) {
                return true;
            }
        }

        for (Map.Entry<PracticeKit, List<Player>> entry : rankedQueue.entrySet()) {
            if (entry.getValue().contains(player)) {
                rankedQueue.get(entry.getKey()).remove(player);
                return true;
            }
        }

        return false;
    }

    public static QueueManager getInstance() {
        return INSTANCE;
    }

    public Map<PracticeKit, List<Player>> getUnrankedQueue() {
        return unrankedQueue;
    }

    public Map<PracticeKit, List<Player>> getRankedQueue() {
        return rankedQueue;
    }

    public int getQueueSize() {
        int i = 0;

        for (List<Player> players : unrankedQueue.values()) {
            i += players.size();
        }

        for (List<Player > players : rankedQueue.values()) {
            i += players.size();
        }

        return i;
    }
}
