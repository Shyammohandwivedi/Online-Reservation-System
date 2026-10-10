package com.oasis.reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        String url = System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:mysql://localhost:3306/oibsip_reservation?serverTimezone=UTC"
        );

        String user = System.getenv().getOrDefault("DB_USER", "root");

        String password = System.getenv().getOrDefault(
            "DB_PASSWORD",
            "Shyam@123"
        );

        return DriverManager.getConnection(url, user, password);
    }
}