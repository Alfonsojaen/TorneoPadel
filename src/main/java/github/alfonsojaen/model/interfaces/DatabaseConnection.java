package github.alfonsojaen.model.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnection {
    Connection getConnection() throws SQLException;
    void closeConnection() throws SQLException;
}
