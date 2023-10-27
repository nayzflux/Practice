package fr.nayz.commons.accounts;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import fr.nayz.practice.Practice;
import fr.nayz.practice.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PracticeData {
    private UUID uuid;
    private int kills;
    private int deaths;
    private int wins;
    private int loses;

    private Map<Kit, Integer> elo;
    private int rankedKills;
    private int rankedDeaths;
    private int rankedWins;
    private int rankedLoses;
    private Connection conn;
    private boolean isNew;
    private Map<Kit, Map<Integer, Integer>> inventories;

    public PracticeData(UUID uuid) {
        this.uuid = uuid;

        this.elo = new HashMap<>();

        for (Kit kit : Kit.values()) {
            elo.put(kit, 1500);
        }

        this.rankedKills = 0;
        this.rankedDeaths = 0;
        this.rankedLoses = 0;
        this.rankedWins = 0;

        this.wins = 0;
        this.loses = 0;
        this.kills = 0;
        this.deaths = 0;

        this.isNew = true;

        this.conn = Practice.getInstance().getMySQL().getConnection();

        this.inventories = new HashMap<>();

        for (Kit kit : Kit.values()) {
            inventories.put(kit, new HashMap<>());

            for (Map.Entry<Integer, ItemStack> entry : kit.getItems().entrySet()) {
                inventories.get(kit).put(entry.getKey(), entry.getKey());
            }
        }
    }

    public void load() {
        Practice.getInstance().getStatistics().add(this);

        try (PreparedStatement sts = conn.prepareStatement("SELECT * FROM `practice` WHERE `player_uuid` = ?")) {
            sts.setString(1, uuid.toString());

            ResultSet rs = sts.executeQuery();

            if (rs.next()) {
                kills = rs.getInt("kills");
                deaths = rs.getInt("deaths");
                wins = rs.getInt("wins");
                loses = rs.getInt("loses");
                rankedKills = rs.getInt("ranked_kills");
                rankedDeaths = rs.getInt("ranked_deaths");
                rankedWins = rs.getInt("ranked_wins");
                rankedLoses = rs.getInt("ranked_loses");

                // Elo
                String eloJson = rs.getString("elo");
                Gson gson = new Gson();

                Type mapType = new TypeToken<Map<Kit, Integer>>() {}.getType();

                try {
                    // Désérialisation de la chaîne JSON en une Map<Kit, Integer>
                    Map<Kit, Integer> kitMap = gson.fromJson(eloJson, mapType);

                    Bukkit.getLogger().info(kitMap.values().toString());

                    // Vous pouvez maintenant utiliser 'kitMap' comme une Map<Kit, Integer>
                    for (Map.Entry<Kit, Integer> entry : kitMap.entrySet()) {
                        Kit kit = entry.getKey();
                        Integer value = entry.getValue();

                        elo.put(kit, value);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

                isNew = false;
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        Practice.getInstance().getStatistics().remove(this);

        if (isNew) {
            try (PreparedStatement sts = conn.prepareStatement("INSERT INTO `practice` (`player_uuid`, `kills`, `deaths`, `wins`, `loses`, `ranked_kills`, `ranked_deaths`, `ranked_wins`, `ranked_loses`, `elo`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                // Créez une instance de Gson
                Gson gson = new Gson();
                // Sérialisez la Map en une chaîne JSON
                String elo = gson.toJson(this.elo);

                Bukkit.getLogger().info(elo);

                sts.setString(1, uuid.toString());
                sts.setInt(2, kills);
                sts.setInt(3, deaths);
                sts.setInt(4, wins);
                sts.setInt(5, loses);
                sts.setInt(6, rankedKills);
                sts.setInt(7, rankedDeaths);
                sts.setInt(8, rankedWins);
                sts.setInt(9, rankedLoses);
                sts.setString(10, elo);

                sts.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement sts = conn.prepareStatement("UPDATE `practice` SET `kills` = ?, `deaths` = ?, `wins` = ?, `loses` = ?, `ranked_kills` = ?, `ranked_deaths` = ?, `ranked_wins` = ?, `ranked_loses` = ?, `elo` = ? WHERE `player_uuid` = ?")) {

                // Créez une instance de Gson
                Gson gson = new Gson();
                // Sérialisez la Map en une chaîne JSON
                String elo = gson.toJson(this.elo);

                Bukkit.getLogger().info(elo);

                sts.setInt(1, kills);
                sts.setInt(2, deaths);
                sts.setInt(3, wins);
                sts.setInt(4, loses);
                sts.setInt(5, rankedKills);
                sts.setInt(6, rankedDeaths);
                sts.setInt(7, rankedWins);
                sts.setInt(8, rankedLoses);
                sts.setString(9, elo);
                sts.setString(10, uuid.toString());

                sts.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public UUID getUuid() {
        return uuid;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getRankedKills() {
        return rankedKills;
    }

    public int getRankedDeaths() {
        return rankedDeaths;
    }

    public int getRankedWins() {
        return rankedWins;
    }

    public int getRankedLoses() {
        return rankedLoses;
    }

    public void addRankedLose(int amount) {
        this.rankedLoses += amount;
    }

    public void addRankedWin(int amount) {
        this.rankedWins += amount;
    }

    public void addRankedKill(int amount) {
        this.rankedKills += amount;
    }

    public void addRankedDeath(int amount) {
        this.rankedDeaths += amount;
    }

    public void addLose(int amount) {
        this.loses += amount;
    }

    public void addWin(int amount) {
        this.wins += amount;
    }

    public void addKill(int amount) {
        this.kills += amount;
    }

    public void addDeath(int amount) {
        this.deaths += amount;
    }

    public int getKDRatio() {
        if (deaths == 0) return kills;

        return kills / deaths;
    }

    public int getWLRatio() {
        if (loses == 0) return kills;

        return wins / loses;
    }

    public int getRankedKDRatio() {
        if (rankedDeaths == 0) return kills;

        return rankedKills / rankedDeaths;
    }

    public int getRankedWLRatio() {
        if (rankedLoses == 0) return kills;

        return rankedWins / rankedLoses;
    }

    public int getElo(Kit kit) {
        return elo.get(kit);
    }

    public int addElo(Kit kit, int amount) {
        int elo = getElo(kit) + amount;
        return this.elo.put(kit, elo);
    }

    public int removeElo(Kit kit, int amount) {
        int elo = getElo(kit) - amount;
        return this.elo.put(kit, elo);
    }

    public Map<Kit, Map<Integer, Integer>> getInventories() {
        return inventories;
    }
}
