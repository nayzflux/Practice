package fr.nayz.practice.commands;

import fr.nayz.commons.pratices.PracticeKit;
import fr.nayz.practice.Practice;
import fr.nayz.practice.arenas.Arena;
import fr.nayz.practice.gui.DuelGui;
import fr.nayz.practice.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DuelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (args.length == 1) {
            if (args[0].equals("accept")) {
                Player target = Practice.getInstance().getDuels().get(player);

                Practice.getInstance().getDuels().remove(player);
                Practice.getInstance().getDuels().remove(target);

                target.sendMessage("§f» §c" + player.getName() + " a accepté votre demande en duel.");
                player.sendMessage("§8» §aVous avez accepté la demande en duel.");

                PracticeKit kit = Practice.getInstance().getDuelKits().get(target);

                Optional<Arena> arena = ConfigManager.getInstance().getAvailableArena(kit);

                if (arena.isEmpty()) {
                    target.sendMessage("§f» §cAucune arène n'est disponible pour le moment, merci de réessayer plus tard");
                    player.sendMessage("§f» §cAucune arène n'est disponible pour le moment, merci de réessayer plus tard");
                    return false;
                }

                arena.get().setKit(kit);
                arena.get().setPlayer1(target);
                arena.get().setPlayer2(player);
                arena.get().setRanked(false);
                arena.get().start();

                return true;
            }

            if (args[0].equals("deny")) {
                Player target = Practice.getInstance().getDuels().get(player);

                Practice.getInstance().getDuels().remove(player);
                Practice.getInstance().getDuels().remove(target);

                target.sendMessage("§f» §c" + player.getName() + " a refusé votre demande en duel.");
                player.sendMessage("§8» §aVous avez refusé la demande en duel.");

                return true;
            }

            String name = args[0];
            Player target = Bukkit.getPlayer(name);

            if (target == null) {
                player.sendMessage("§f» §cLe joueur " + name + " n'est pas en ligne.");
                return false;
            }

            // Mapper l'inviteur à l'invité
            Practice.getInstance().getDuels().put(player, target);

            DuelGui.open(player);

            return true;
        }

        player.sendMessage("§8§m----------------------------");
        player.sendMessage("§8» §b/duel <pseudo> §8• §7Provoquer un joueur en duel");
        player.sendMessage("§8» §b/duel accept §8• §7Accepter la provocation en duel");
        player.sendMessage("§8» §b/duel deny §8• §7Refuser la provocation en duel");
        player.sendMessage("§8§m----------------------------");
        return false;
    }
}
