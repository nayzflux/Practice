package fr.nayz.practice.commands;

import fr.nayz.practice.Practice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 1) {
                try (PreparedStatement sts = Practice.getInstance().getMySQL().getConnection().prepareStatement("INSERT INTO `bugs` (`player_uuid`, `description`) VALUES (?, ?)")) {
                    sts.setString(1, player.getUniqueId().toString());
                    sts.setString(2, String.join(" ",args));

                    sts.executeUpdate();

                    player.sendMessage("§8» §aLe rapport de Bug a été soumis avec succès");
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    player.sendMessage("§f» §cLe rapport de Bug n'a pu être soumis");
                    return false;
                }


            }

            player.sendMessage("§8» §c/bug <description du bug>");
        }

        return false;
    }
}
