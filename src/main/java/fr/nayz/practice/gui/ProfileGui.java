package fr.nayz.practice.gui;

import fr.nayz.api.GameAPI;
import fr.nayz.api.items.ItemBuilder;
import fr.nayz.commons.accounts.PracticeData;
import fr.nayz.commons.pratices.PracticeKit;
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

public class ProfileGui implements Listener {
    private static final String NAME = "§8» §6§lProfil";
    private static final int SIZE = 6 * 9;

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, SIZE, Component.text(NAME));
        PracticeData data = GameAPI.getInstance().getPracticeDataManager().getData(player);

        for (int i = 0; i < SIZE; i++) {
            inv.setItem(i, new ItemStack(Material.GLASS_PANE));
        }

        for (PracticeKit kit : PracticeKit.values()) {
            if (kit.isRanked()) {
                ItemStack item = new ItemBuilder(kit.getMaterial()).setName("§8» §e§l" + kit.getName() + " §7(Ranked)").setLore("§f• §7Elo: §b" + data.getElo(kit)).toItem();
                inv.setItem(kit.getSlot() + 10, item);
            }
        }

        ItemStack global = new ItemBuilder(Material.PLAYER_HEAD)
                .setName("§8» §a§lGlobal")
                .setLore("§f• §7Parties: §e" + (data.getWins() + data.getLoses()),
                        "§f• §7Victoires: §a" + data.getWins(),
                        "§f• §7Défaites: §c" + data.getLoses(),
                        "§f• §7Ratio V/D: §e" + data.getWLRatio(),
                        "§f• §7Eliminations: §a" + data.getKills(),
                        "§f• §7Morts: §c" + data.getDeaths(),
                        "§f• §7Ratio E/M: §e" + data.getKDRatio())
                .setSkullOwner(player.getUniqueId())
                .toItem();

        ItemStack ranked = new ItemBuilder(Material.DIAMOND_SWORD)
                .setName("§8» §c§lRanked")
                .setLore("§f• §7Parties: §e" + (data.getRankedWins() + data.getRankedLoses()),
                        "§f• §7Victoires: §a" + data.getRankedWins(),
                        "§f• §7Défaites: §c" + data.getRankedLoses(),
                        "§f• §7Ratio V/D: §e" + data.getRankedWLRatio(),
                        "§f• §7Eliminations: §a" + data.getRankedKills(),
                        "§f• §7Morts: §c" + data.getRankedDeaths(),
                        "§f• §7Ratio E/M: §e" + data.getRankedKDRatio())
                .toItem();

        inv.setItem(39, global);
        inv.setItem(41, ranked);

        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (!view.getOriginalTitle().equals(NAME)) return;

        event.setCancelled(true);
    }
}
