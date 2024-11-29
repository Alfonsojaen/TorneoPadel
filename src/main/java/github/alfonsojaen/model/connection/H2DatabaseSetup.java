package github.alfonsojaen.model.connection;

import java.io.File;
import java.sql.*;

public class H2DatabaseSetup {
    private static final String JDBC_URL = "jdbc:sqlite:data/mydatabaseproyecto.db";

    public static void main(String[] args) {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            System.out.println("Conexión establecida con la base de datos SQLite.");

            try (Statement statement = connection.createStatement()) {

                statement.execute("CREATE TABLE IF NOT EXISTS User ("
                        + "username TEXT PRIMARY KEY, "
                        + "password TEXT, "
                        + "name TEXT, "
                        + "email TEXT"
                        + ")");
                System.out.println("Tabla 'User' creada.");

                statement.execute("CREATE TABLE IF NOT EXISTS Tournament ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT, "
                        + "start_date DATE, "
                        + "end_date DATE, "
                        + "location TEXT, "
                        + "prize TEXT, "
                        + "user_username TEXT, "
                        + "FOREIGN KEY(user_username) REFERENCES User(username) "
                        + ")");
                System.out.println("Tabla 'Tournament' creada.");

                statement.execute("CREATE TABLE IF NOT EXISTS Team ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT, "
                        + "coach TEXT, "
                        + "description TEXT, "
                        + "user_username TEXT, "
                        + "FOREIGN KEY(user_username) REFERENCES User(username) "
                        + ")");
                System.out.println("Tabla 'Team' creada.");

                statement.execute("CREATE TABLE IF NOT EXISTS Player ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "nickname TEXT, "
                        + "gender TEXT, "
                        + "age INTEGER, "
                        + "user_username TEXT, "
                        + "FOREIGN KEY(user_username) REFERENCES User(username) "
                        + ")");
                System.out.println("Tabla 'Player' creada.");

                statement.execute("CREATE TABLE IF NOT EXISTS Pertenece ("
                        + "tournamentId INTEGER, "
                        + "teamId INTEGER, "
                        + "PRIMARY KEY (tournamentId, teamId), "
                        + "FOREIGN KEY (tournamentId) REFERENCES Tournament(id) ON DELETE CASCADE, "
                        + "FOREIGN KEY (teamId) REFERENCES Team(id) ON DELETE CASCADE"
                        + ")");
                System.out.println("Tabla 'Pertenece' creada.");

                statement.execute("CREATE TABLE IF NOT EXISTS Esta ("
                        + "teamId INTEGER, "
                        + "playerId INTEGER, "
                        + "PRIMARY KEY (teamId, playerId), "
                        + "FOREIGN KEY (teamId) REFERENCES Team(id) ON DELETE CASCADE, "
                        + "FOREIGN KEY (playerId) REFERENCES Player(id) ON DELETE CASCADE"
                        + ")");
                System.out.println("Tabla 'Esta' creada.");

                statement.executeUpdate("INSERT INTO User (username, password, name, email) "
                        + "VALUES ('usuario1', 'password123', 'Juan Pérez', 'juan@correo.com')");
                System.out.println("Usuario insertado correctamente.");

                statement.executeUpdate("INSERT INTO Tournament (name, start_date, end_date, location, prize, user_username) "
                        + "VALUES ('Torneo 1', '2024-12-01', '2024-12-10', 'Madrid', '1000 Euros', 'usuario1')");
                System.out.println("Torneo insertado correctamente.");

                statement.executeUpdate("INSERT INTO Team (name, coach, description, user_username) "
                        + "VALUES ('Equipo 1', 'Carlos García', 'Equipo de fútbol', 'usuario1')");
                System.out.println("Equipo insertado correctamente.");

                System.out.println("\nConsultando datos de la tabla User:");
                try (ResultSet rs = statement.executeQuery("SELECT * FROM User")) {
                    while (rs.next()) {
                        System.out.println("Username: " + rs.getString("username"));
                        System.out.println("Name: " + rs.getString("name"));
                        System.out.println("Email: " + rs.getString("email"));
                    }
                }

                System.out.println("\nConsultando datos de la tabla Tournament:");
                try (ResultSet rs = statement.executeQuery("SELECT * FROM Tournament")) {
                    while (rs.next()) {
                        System.out.println("Tournament Name: " + rs.getString("name"));
                        System.out.println("Start Date: " + rs.getString("start_date"));
                        System.out.println("End Date: " + rs.getString("end_date"));
                        System.out.println("Location: " + rs.getString("location"));
                    }
                }

                System.out.println("\nConsultando datos de la tabla Team:");
                try (ResultSet rs = statement.executeQuery("SELECT * FROM Team")) {
                    while (rs.next()) {
                        System.out.println("Team Name: " + rs.getString("name"));
                        System.out.println("Coach: " + rs.getString("coach"));
                        System.out.println("Description: " + rs.getString("description"));
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error al crear tablas: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}