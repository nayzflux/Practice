package fr.nayz.practice.listeners;

import fr.nayz.practice.arenas.Arena;
import fr.nayz.practice.arenas.ArenaStatus;
import fr.nayz.commons.pratices.PracticeKit;
import fr.nayz.practice.managers.ConfigManager;
import fr.nayz.practice.utils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class ArenaListener implements Listener {
    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            Bukkit.getLogger().info("entity dmg");

            Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

            // Si il n'est pas dans une arène
            if (arena.isEmpty()) {
                event.setCancelled(true);
                return;
            }

            // Si l'arène n'est pas en phase de jeu
            if (arena.get().getStatus() != ArenaStatus.PLAYING) {
                event.setCancelled(true);
                return;
            }

            // Si c'est Sumo alors il n'y a aucun dégat
            if (arena.get().getKit() == PracticeKit.SUMO) {
                event.setDamage(0);
            }

            Player damager = null;

            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();

                if (!(arrow.getShooter() instanceof Player)) {
                    event.setCancelled(true);
                    return;
                }

                damager = (Player) arrow.getShooter();
                GameUtils.playSound(damager, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1F);
                GameUtils.sendMessage(damager, "§7" + player.getName() + " est à §c" + Math.round(player.getHealth()) + " ❤️");
            }

            if (event.getDamager() instanceof Player) {
                damager = (Player) event.getDamager();
            }

            if (damager == null) {
                event.setCancelled(true);
                return;
            }

            // Si le joueur est mort
            if (event.getFinalDamage() >= player.getHealth()) {
                event.setCancelled(true);
                arena.get().kill(player, damager);
                return;
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        // Si il n'est pas dans une arène
        if (arena.isEmpty()) {
            return;
        }

        // Si l'arène est Sumo et en demarrage
        if (arena.get().getStatus() == ArenaStatus.STARTING && arena.get().getKit() == PracticeKit.SUMO) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // TP Void
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                player.teleport(ConfigManager.getInstance().getLobby());
                event.setCancelled(true);
                return;
            }

            Bukkit.getLogger().info("dmg");

            Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

            // Si il n'est pas dans une arène
            if (arena.isEmpty()) {
                event.setCancelled(true);
                return;
            }

            // Si l'arène n'est pas en phase de jeu
            if (arena.get().getStatus() != ArenaStatus.PLAYING) {
                event.setCancelled(true);
                return;
            }

            // Si le joueur est mort
            if (event.getFinalDamage() >= player.getHealth()) {
                event.setCancelled(true);

                Player oppenent = arena.get().getPlayer1() == player ? arena.get().getPlayer2() : arena.get().getPlayer1();
                arena.get().kill(player, oppenent);
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() != Material.AIR) {
            event.setCancelled(true);
            return;
        }

        Bukkit.getLogger().info(block.toString());

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        // Si il n'est pas dans une arène
        if (arena.isEmpty()) {
            event.setCancelled(true);
            return;
        }

        // Si la partie n'a pas commencé
        if (arena.get().getStatus() != ArenaStatus.PLAYING) {
            event.setCancelled(true);
            return;
        }

        arena.get().getPlacedBlocks().add(block);
    }

    @EventHandler
    public void onMerge(BlockFormEvent event) {
        // Bricolage
        Player player = event.getBlock().getLocation().getNearbyPlayers(300).stream().findFirst().orElse(null);
        Block block = event.getBlock();

        Bukkit.getLogger().info(block.toString());

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        // Si il n'est pas dans une arène
        if (arena.isEmpty()) {
            event.setCancelled(true);
            return;
        }

        // Si la partie n'a pas commencé
        if (arena.get().getStatus() != ArenaStatus.PLAYING) {
            event.setCancelled(true);
            return;
        }

        arena.get().getPlacedBlocks().add(block);
    }


    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        Bukkit.getLogger().info(block.toString());

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        // Si il n'est pas dans une arène
        if (arena.isEmpty()) {
            event.setCancelled(true);
            return;
        }

        // Si la partie n'a pas commencé
        if (arena.get().getStatus() != ArenaStatus.PLAYING) {
            event.setCancelled(true);
            return;
        }

        arena.get().getPlacedBlocks().add(block);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        Bukkit.getLogger().info(block.toString());

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        // Si il n'est pas dans une arène
        if (arena.isEmpty()) {
            event.setCancelled(true);
            return;
        }

        // Si la partie n'a pas commencé
        if (arena.get().getStatus() != ArenaStatus.PLAYING) {
            event.setCancelled(true);
            return;
        }

        // Si ce n'est pas un bloc posé par un joueur
        if (!arena.get().getPlacedBlocks().contains(block)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

        // Si il n'est pas dans une arène
        if (arena.isEmpty()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            Optional<Arena> arena = ConfigManager.getInstance().getPlayerArena(player);

            // Si il n'est pas dans une arène
            if (arena.isEmpty()) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
