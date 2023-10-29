package fr.nayz.practice.arenas;

import fr.nayz.api.GameAPI;
import fr.nayz.commons.pratices.PracticeKit;
import fr.nayz.practice.Practice;
import fr.nayz.commons.accounts.PracticeData;
import fr.nayz.practice.scoreboards.GameBoard;
import fr.nayz.practice.scoreboards.LobbyBoard;
import fr.nayz.practice.utils.GameUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Arena extends BukkitRunnable {
    private Location spawn1;
    private Location spawn2;
    private Player player1;
    private Player player2;
    private List<Block> placedBlocks;
    private ArenaStatus status;
    private int timer = 5;
    private PracticeKit kit;
    private List<PracticeKit> kits;
    private boolean ranked;
    private List<Entity> drops;

    public Arena(Location spawn1, Location spawn2, List<PracticeKit> kits) {
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.kits = kits;
        this.status = ArenaStatus.AVAILABLE;
        this.placedBlocks = new ArrayList<>();
        this.drops = new ArrayList<>();

        this.runTaskTimer(Practice.getInstance(), 0L, 20L);
    }

    public List<PracticeKit> getKits() {
        return kits;
    }

    public PracticeKit getKit() {
        return kit;
    }

    public void start() {
        Bukkit.getLogger().info("[Arena] Starting...");

        timer = 5;

        setStatus(ArenaStatus.STARTING);

        GameUtils.reset(List.of(player1, player2));

        kit.give(player1);
        kit.give(player2);

        player1.teleport(spawn1);
        player2.teleport(spawn2);

        player1.setGameMode(GameMode.SURVIVAL);
        player2.setGameMode(GameMode.SURVIVAL);

        GameBoard.getInstance().createNewScoreboard(player1);
        GameBoard.getInstance().createNewScoreboard(player2);
    }

    public void stop() {
        timer = 10;
        setStatus(ArenaStatus.END);
    }

    public void reset() {
        for (Block placedBlock : placedBlocks) {
            placedBlock.setType(Material.AIR);
        }

        placedBlocks.clear();

        for (Entity drop : drops) {
            Bukkit.getLogger().info(drop.getName());
            drop.remove();
        }

        drops.clear();
    }

    public boolean isRanked() {
        return ranked;
    }

    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setKit(PracticeKit kit) {
        this.kit = kit;
    }

    public void kill(Player victim, Player killer) {
        GameUtils.playSound(victim, Sound.ENTITY_WITHER_DEATH, 1F, 1F);
        GameUtils.playSound(killer, Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);

        GameUtils.showTitle(victim, "§cDéfaite !", "§7" + killer.getName() + " remporte la victoire");
        GameUtils.showTitle(killer, "§6Victoire !", "§7Vous remportez la victoire");

        victim.sendMessage("§f» §cVous avez perdu la partie face à " + killer.getName());
        killer.sendMessage("§7» §aVous avez gagné la partie face à " + victim.getName());

        GameUtils.spawnFirework(victim, 3);

        victim.setGameMode(GameMode.SPECTATOR);
        killer.setGameMode(GameMode.CREATIVE);

        for (ItemStack item : victim.getInventory().getContents()) {
            if (item != null) {
                Entity drop = victim.getLocation().getWorld().dropItem(victim.getLocation(), item);
                drops.add(drop);
            }
        }

        // Statistics
        PracticeData victimStats = GameAPI.getInstance().getPracticeDataManager().getData(victim);
        PracticeData killerStats = GameAPI.getInstance().getPracticeDataManager().getData(killer);

        victimStats.addDeath(1);
        victimStats.addLose(1);

        killerStats.addKill(1);
        killerStats.addWin(1);

        if (ranked) {
            victimStats.addRankedDeath(1);
            victimStats.addRankedLose(1);

            killerStats.addRankedKill(1);
            killerStats.addRankedWin(1);

            // Elo
            // Multiplicateur x64 + différence de vie entre les 2 joueurs
            int K = (int) (64 + Math.floor(killer.getHealth() - Math.ceil(victim.getHealth())));

            // Probalité de victoire
            float pWinner = (float) (1.0 / (1.0 + Math.pow(10, (victimStats.getElo(kit) - killerStats.getElo(kit)) / 200F)));
            Bukkit.getLogger().info("[Elo] Le gagnant avait " + pWinner * 100 + "% chance de gagner");

            float pLoser = (float) (1.0 / (1.0 + Math.pow(10, (killerStats.getElo(kit) - victimStats.getElo(kit)) / 200F)));
            Bukkit.getLogger().info("[Elo] Le perdant avait " + pLoser * 100 + "% chance de gagner");

            // Ajout d'élo
            int amountWinner = (int) (K * (1 - pWinner));

            // Définir +10 comment le gain minimal
            if (amountWinner < 10) {
                amountWinner = 10;
            }

            int amountLoser = (int) (K * pLoser);

            killerStats.addElo(kit, amountWinner);
            victimStats.removeElo(kit, amountLoser);

            killer.sendMessage("§8» §7Votre élo a été mis à jour §b" + killerStats.getElo(kit) + " §7(§e+" + amountWinner + "§7)");
            victim.sendMessage("§8» §7Votre élo a été mis à jour §b" + victimStats.getElo(kit) + " §7(§e-" + amountLoser + "§7)");
        }

        stop();
    }

    public ArenaStatus getStatus() {
        return status;
    }

    public void setStatus(ArenaStatus status) {
        this.status = status;
    }

    @Override
    public void run() {
        if (status == ArenaStatus.AVAILABLE) {
            return;
        }

        if (status == ArenaStatus.STARTING) {
            if (timer == 5) {
                GameUtils.playSound(List.of(player1, player2), Sound.BLOCK_LEVER_CLICK, 1F, 1F);
                GameUtils.showTitle(List.of(player1, player2), String.valueOf(timer), "");
            }

            if (timer == 3) {
                GameUtils.playSound(List.of(player1, player2), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
                GameUtils.showTitle(List.of(player1, player2), "§c" + timer, "");
            }

            if (timer == 2) {
                GameUtils.playSound(List.of(player1, player2), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 2F);
                GameUtils.showTitle(List.of(player1, player2), "§e" + timer, "");
            }

            if (timer == 1) {
                GameUtils.playSound(List.of(player1, player2), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 3F);
                GameUtils.showTitle(List.of(player1, player2), "§a" + timer, "");
            }

            if (timer == 0) {
                GameUtils.playSound(List.of(player1, player2), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1F);
                GameUtils.showTitle(List.of(player1, player2), "C'est parti !", "§7Que le meilleur gagne");

                setStatus(ArenaStatus.PLAYING);

                timer = 0;
                return;
            }

            timer--;
        }

        if (status == ArenaStatus.PLAYING) {
            timer++;

            // Sumo
            if (kit == PracticeKit.SUMO) {
                if (player1.getLocation().getBlockY() <= (spawn1.getBlockY() - 1)) {
                    kill(player1, player2);
                }

                if (player2.getLocation().getBlockY() <= (spawn1.getBlockY() - 1)) {
                    kill(player2, player1);
                }
            }
        }

        if (status == ArenaStatus.END) {
            if (timer == 0) {
                GameUtils.reset(List.of(player1, player2));
                GameUtils.giveLobbyItem(List.of(player1, player2));

                player1.setGameMode(GameMode.ADVENTURE);
                player2.setGameMode(GameMode.ADVENTURE);

                LobbyBoard.getInstance().createNewScoreboard(player1);
                LobbyBoard.getInstance().createNewScoreboard(player2);

                reset();

                GameUtils.teleportToLobby(List.of(player1, player2));

                player1 = null;
                player2 = null;

                setStatus(ArenaStatus.AVAILABLE);
            }

            timer--;
        }
    }

    public List<Entity> getDrops() {
        return drops;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public List<Block> getPlacedBlocks() {
        return placedBlocks;
    }
}
