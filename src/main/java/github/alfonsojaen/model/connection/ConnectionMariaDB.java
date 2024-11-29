package github.alfonsojaen.model.connection;

import github.alfonsojaen.model.interfaces.DatabaseConnection;
import github.alfonsojaen.utils.XMLManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMariaDB implements DatabaseConnection {
    private Connection conn;

    public ConnectionMariaDB() {
        ConnectionProperties properties = (ConnectionProperties) XMLManager.readXML(new ConnectionProperties(), "connection.xml");
        try {
            conn = DriverManager.getConnection(properties.getURL(), properties.getUser(), properties.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            conn = null;
        }
    }
    @Override
    public Connection getConnection() throws SQLException {
        return conn;
    }

    @Override
    public void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
