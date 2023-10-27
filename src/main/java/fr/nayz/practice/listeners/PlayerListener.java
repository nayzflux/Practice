package fr.nayz.practice.listeners;

import fr.nayz.commons.accounts.Account;
import fr.nayz.commons.ranks.Rank;
import fr.nayz.practice.Practice;
import fr.nayz.commons.accounts.PracticeData;
import fr.nayz.practice.arenas.Arena;
import fr.nayz.practice.arenas.ArenaStatus;
import fr.nayz.practice.gui.EditingKitsGui;
import fr.nayz.practice.gui.ProfileGui;
import fr.nayz.practice.gui.RankedGui;
import fr.nayz.practice.gui.UnrankedGui;
import fr.nayz.practice.kits.Kit;
import fr.nayz.practice.managers.ConfigManager;
import fr.nayz.practice.managers.QueueManager;
import fr.nayz.practice.scoreboards.LobbyBoard;
import fr.nayz.practice.utils.GameUtils;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.Optional;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GameUtils.reset(player);
        GameUtils.giveLobbyItem(player);
        GameUtils.teleportToLobby(player);

        player.setGameMode(GameMode.ADVENTURE);

        PracticeData stats = new PracticeData(player.getUniqueId());
        stats.load();

        LobbyBoard.getInstance().createNewScoreboard(player);

        Account account = Practice.getInstance().findAccount(player).get();
        Rank rank = account.getRank();
        String chatPrefix = rank.getChatPrefix();

        event.joinMessage(Component.text("§8» " + chatPrefix + player.getName() + " §7a rejoint le §c§lPractice"));

        if (rank.getPower() >= Rank.VIP.getPower()) {
            GameUtils.spawnFirework(player, 1);

//            for (Player online : Bukkit.getOnlinePlayers()) {
//                Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(online);
//
//                if (arena.isEmpty()) {
//                    GameUtils.showTitle(online, chatPrefix + player.getName(), "§bA rejoint le serveur !");
//                }
//            }
        }

        GameUtils.sendSectionMessage(player, "§8» §7Bienvenue sur le §c§lPractice", "§8» §7Le serveur est encore en phase de développement", "§8» §7Merci de signaler les bugs en utilisant la commande /bugs");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        if (arena.isPresent()) {
            if (arena.get().getStatus() == ArenaStatus.PLAYING) {
                Player oppenent = arena.get().getPlayer1() == player ? arena.get().getPlayer2() : arena.get().getPlayer1();
                arena.get().kill(player, oppenent);
            }

            if (arena.get().getStatus() == ArenaStatus.END) {
                arena.get().reset();
            }
        }

        QueueManager.getInstance().leave(player);

        Practice.getInstance().getEditingKits().remove(player);

        PracticeData stats = Practice.getInstance().findPlayerStatistics(player);
        stats.save();

        Account account = Practice.getInstance().findAccount(player).get();
        Rank rank = account.getRank();
        String chatPrefix = rank.getChatPrefix();

        event.quitMessage(Component.text("§8» " + chatPrefix + player.getName() + " §7a quitté le §c§lPractice"));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        event.setCancelled(true);

        if (item == null) return;

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        if (arena.isPresent()) {
            event.setCancelled(false);
            return;
        }

        Kit kit = Practice.getInstance().getEditingKits().get(player);

        if (kit != null) return;

        switch (item.getType()) {
            case STONE_SWORD:
                UnrankedGui.open(player);
                break;
            case DIAMOND_SWORD:
                RankedGui.open(player);
                break;
            case CHEST:
                EditingKitsGui.open(player);
                break;
            case PLAYER_HEAD:
                ProfileGui.open(player);
                break;
            case REDSTONE:
                QueueManager.getInstance().leave(player);
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onPlayerRightClickSign(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null) return;

        if (block.getType() == Material.OAK_WALL_SIGN) {
            event.setCancelled(true);

             Sign sign = (Sign) block.getState();

            Bukkit.getLogger().info(sign.getLine(2));

            if (sign.getLine(2).equals("spawn")) {
                Kit kit = Practice.getInstance().getEditingKits().get(player);

                // Save inv
                if (kit != null) {
//                    ItemStack[] items = player.getInventory().getContents();
//
//                    PracticeData data = Practice.getInstance().findPlayerStatistics(player);
//
//                    for (int i = 0; i < items.length; i++) {
//                        ItemStack item = items[i];
//                        Bukkit.getLogger().info(i + " " + item);
//                    }

                    Practice.getInstance().getEditingKits().remove(player);
                }

                GameUtils.reset(player);
                GameUtils.giveLobbyItem(player);
                GameUtils.teleportToLobby(player);
            }
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        if (arena.isPresent()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(event.getPlayer());

        if (arena.isPresent()) {
            arena.get().getDrops().add(event.getItemDrop());
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onArrowShot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

            if (arena.isPresent()) {
                arena.get().getDrops().add(event.getProjectile());
                return;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickItemEvent event) {
        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(event.getPlayer());

        if (arena.isPresent()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack  item = event.getCurrentItem();
        int slot = event.getSlot();

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getHolder() != player) return;

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        if (arena.isPresent()) return;

        event.setCancelled(true);

        if (item == null) return;

        Kit kit = Practice.getInstance().getEditingKits().get(player);

        if (kit == null) return;

        event.setCancelled(false);

        Bukkit.getLogger().info(event.getClick() + " " + item.getType() + " " + slot);
    }
}
