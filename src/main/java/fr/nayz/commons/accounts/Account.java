package fr.nayz.commons.accounts;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import fr.nayz.commons.ranks.Rank;
import fr.nayz.practice.Practice;
import fr.nayz.practice.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Account {
    private UUID uuid;
    private Rank rank;
    private List<UUID> friends;

    private boolean isNew;

    private Connection conn = Practice.getInstance().getMySQL().getConnection();

    public Account(Player player) {
        this.uuid = player.getUniqueId();
        this.rank = Rank.PLAYER;
        this.friends = new ArrayList<>();

        this.isNew = true;
    }

    public void load() {
        Practice.getInstance().getAccounts().add(this);

        try (PreparedStatement sts = conn.prepareStatement("SELECT * FROM `accounts` WHERE `player_uuid` = ?")) {
            sts.setString(1, uuid.toString());

            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                rank = Rank.find(rs.getString("rank_name"));

                // Friends
                Gson gson = new Gson();
                friends = gson.fromJson(rs.getString("friends"), new TypeToken<List<UUID>>(){}.getType());

                isNew = false;
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        Practice.getInstance().getAccounts().remove(this);

        Gson gson = new Gson();

        if (isNew) {
            try (PreparedStatement sts = conn.prepareStatement("INSERT INTO `accounts` (`player_uuid`, `player_name`, `rank_name`, `friends`) VALUES (?, ?, ?, ?)")) {

                sts.setString(1, uuid.toString());
                sts.setString(2, Bukkit.getPlayer(uuid).getName());
                sts.setString(3, rank.getName());
                sts.setString(3, rank.getName());
                sts.setString(4, gson.toJson(friends));

                sts.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement sts = conn.prepareStatement("UPDATE `accounts` SET `player_name` = ?, `rank_name` = ?, `friends` = ? WHERE `player_uuid` = ?")) {

                sts.setString(1, Bukkit.getPlayer(uuid).getName());
                sts.setString(2, rank.getName());
                sts.setString(3, gson.toJson(friends));
                sts.setString(4, uuid.toString());

                sts.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isNew() {
        return isNew;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public boolean isFriend(UUID uuid) {
        return friends.contains(uuid);
    }

    public void addFriend(UUID uuid) {
        friends.add(uuid);
    }
}
