package fr.nayz.practice.gui;

import fr.nayz.practice.Practice;
import fr.nayz.practice.kits.Kit;
import fr.nayz.practice.managers.QueueManager;
import fr.nayz.practice.utils.GameUtils;
import fr.nayz.practice.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Target;
import java.util.Optional;

public class DuelGui implements Listener {
    private static final String NAME = "§8» §e§lDuel";
    private static final int SIZE = 3 * 9;

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, SIZE, Component.text(NAME));

        for (int i = 0; i < SIZE; i++) {
            inv.setItem(i, new ItemStack(Material.GLASS));
        }

        for (Kit kit : Kit.values()) {
            ItemStack item = new ItemBuilder(kit.getMaterial()).setName("§8» §e§l" + kit.getName()).toItem();
            inv.setItem(kit.getSlot(), item);
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (!view.getOriginalTitle().equals(NAME)) return;

        event.setCancelled(true);

        if (item == null) return;

        Optional<Kit> kit = Kit.find(item.getType());

        if (kit.isEmpty()) return;

        Practice.getInstance().getDuelKits().put(player, kit.get());

        Player target = Practice.getInstance().getDuels().get(player);

        // Mapper l'invité à l'inviteur
        Practice.getInstance().getDuels().put(target, player);

        // Envoyer la demande
        GameUtils.sendSectionMessage(target, "§8» §b" + player.getName() + ", vous a proposé en duel", "§8» §e" + kit.get().getName(), "§8» §7/duel accept §8• §apour accepter la demande", "§8» §7/duel deny §8• §cpour refuser la demande");

        // Fermer l'inv
        view.close();
    }
}
