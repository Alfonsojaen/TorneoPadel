package github.alfonsojaen.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:data/mydatabaseproyecto.db";
        return DriverManager.getConnection(url);
    }
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}