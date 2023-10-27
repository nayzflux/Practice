package fr.nayz.practice.mysql;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private String url;

    private Connection conn;

    public MySQL(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(url, user, password);
            Bukkit.getLogger().info("Connected to MySQL db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }
}
