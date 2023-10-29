package fr.nayz.practice.utils;

import fr.nayz.api.items.ItemBuilder;
import fr.nayz.commons.pratices.PracticeKit;
import fr.nayz.practice.managers.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;

public class GameUtils {
    public static void showTitle(Player player, String title, String subtitle) {
        player.showTitle(Title.title(Component.text(title), Component.text(subtitle)));
    }

    public static void showTitle(List<Player> players, String title, String subtitle) {
        for (Player player : players) {
            player.showTitle(Title.title(Component.text(title), Component.text(subtitle)));
        }
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(Component.text(message));
    }

    public static void sendMessage(List<Player> players, String message) {
        for (Player player : players) {
            player.sendMessage(Component.text(message));
        }
    }

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player, sound, volume, pitch);
    }

    public static void playSound(List<Player> players, Sound sound, float volume, float pitch) {
        for (Player player : players) {
            player.playSound(player, sound, volume, pitch);
        }
    }

    public static void reset(Player player) {
        player.getEquipment().clear();
        player.getInventory().clear();
        player.clearActivePotionEffects();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
    }

    public static void reset(List<Player> players) {
        for (Player player : players) {
            reset(player);
        }
    }

    public static void teleportToLobby(Player player) {
        player.teleport(ConfigManager.getInstance().getLobby());
    }

    public static void teleportToLobby(List<Player> players) {
        for (Player player : players) {
            player.teleport(ConfigManager.getInstance().getLobby());
        }
    }
    public static void giveLobbyItem(Player player) {
        ItemStack unranked = new ItemBuilder(Material.STONE_SWORD).setName("§8» §a§lUnranked §7(Clique-Droit)").toItem();
        ItemStack ranked = new ItemBuilder(Material.DIAMOND_SWORD).setName("§8» §c§lRanked §7(Clique-Droit)").toItem();
        ItemStack editKits = new ItemBuilder(Material.CHEST).setName("§8» §d§lModifier les kits §7(Clique-Droit)").toItem();

        ItemStack profile = new ItemBuilder(Material.PLAYER_HEAD).setName("§8» §6§lProfil §7(Clique-Droit)").setSkullOwner(player.getUniqueId()).toItem();

        Inventory inv = player.getInventory();

        inv.setItem(0, profile);
        inv.setItem(4, unranked);
        inv.setItem(5, ranked);
        inv.setItem(8, editKits);
    }


    public static void giveLobbyItem(List<Player> players) {
        for (Player player : players) {
            giveLobbyItem(player);
        }
    }

    public static void giveQueueItem(Player player, PracticeKit kit, boolean ranked) {
        ItemStack cancel = new ItemBuilder(Material.REDSTONE).setName("§8» §c§lAnnuler §7(Clique-Droit)").toItem();
        ItemStack queue = new ItemBuilder(Material.PAPER).setName("§8» §b§l" + kit.getName() + " §7(" + (ranked ? "Ranked" : "Unranked") + ")").toItem();

        Inventory inv = player.getInventory();

        inv.setItem(4, queue);
        inv.setItem(8, cancel);
    }

    public static void spawnFirework(Player player, int amount) {
        Location loc = player.getLocation();
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i < amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public static void sendSectionMessage(Player player, String ...msg) {
        player.sendMessage("§7§m--------------------------------------------");
        for (String s : msg) {
            player.sendMessage(s);
        }
        player.sendMessage("§7§m--------------------------------------------");
    }
}
