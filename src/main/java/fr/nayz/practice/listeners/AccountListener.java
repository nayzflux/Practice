package fr.nayz.practice.listeners;

import fr.nayz.commons.accounts.Account;
import fr.nayz.practice.Practice;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class AccountListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Account account = new Account(player);
        account.load();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Optional<Account> account = Practice.getInstance().findAccount(player);

        if (account.isPresent()) {
            account.get().save();
        }
    }

}
