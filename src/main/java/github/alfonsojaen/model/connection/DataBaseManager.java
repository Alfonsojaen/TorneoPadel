package github.alfonsojaen.model.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {
        private static DataBaseManager instance;
        private Connection connection;
        private String selectedDatabase;

        private DataBaseManager() {
            // Constructor privado para el patrón Singleton
        }

        public static DataBaseManager getInstance() {
            if (instance == null) {
                instance = new DataBaseManager();
            }
            return instance;
        }

        public void setDatabase(String database) throws SQLException {
            if (connection != null && !connection.isClosed()) {
                connection.close(); // Cerrar la conexión anterior si existe
            }

            if ("SQLite".equalsIgnoreCase(database)) {
                connection = new SQLiteConnection().getConnection();
                selectedDatabase = "SQLite";
                try (Statement statement = connection.createStatement()) {
                    statement.execute("PRAGMA foreign_keys = ON;");
                } catch (SQLException e) {
                    throw e;
                }


            } else if ("MariaDB".equalsIgnoreCase(database)) {
                connection = new ConnectionMariaDB().getConnection();
                selectedDatabase = "MariaDB";
            } else {
                throw new IllegalArgumentException("Tipo de base de datos no soportado: " + database);
            }
        }

        public Connection getConnection() {
            return connection;
        }

        public String getSelectedDatabase() {
            return selectedDatabase;
        }

        public void closeConnection() {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

