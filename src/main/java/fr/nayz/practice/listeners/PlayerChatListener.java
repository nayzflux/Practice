package fr.nayz.practice.listeners;

import fr.nayz.api.GameAPI;
import fr.nayz.commons.accounts.Account;
import fr.nayz.commons.ranks.Rank;
import fr.nayz.practice.Practice;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class PlayerChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Account account = GameAPI.getInstance().getAccountManager().getAccount(player);

        Rank rank = account.getRank();

        String chatPrefix = rank.getChatPrefix();
        String chatColor = rank.getChatColor();

        event.setFormat(chatPrefix + "%2s §8» " + chatColor + "%1s");
    }
}
