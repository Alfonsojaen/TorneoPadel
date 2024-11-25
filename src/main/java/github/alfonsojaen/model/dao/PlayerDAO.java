package github.alfonsojaen.model.dao;

import github.alfonsojaen.model.connection.ConnectionMariaDB;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.interfaces.InterfacePlayerDAO;
import github.alfonsojaen.model.singleton.UserSession;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO implements InterfacePlayerDAO<Player> {

    private static final String FINDALL = "SELECT id, nickname, gender, age, user_username FROM Player WHERE user_username = ?";
    private static final String FINDBYID = "SELECT id, nickname, gender, age, user_username FROM Player WHERE id = ? AND user_username = ?";
    private static final String INSERT = "INSERT INTO Player (nickname, gender, age, user_username) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Player SET nickname = ?, gender = ?, age = ? WHERE id = ? AND user_username = ?";
    private static final String DELETE = "DELETE FROM Player WHERE nickname = ? AND user_username = ?";
    private final static String FINDBYNICKNAME = "SELECT a.id, a.nickname, a.gender, a.age, a.user_username FROM Player AS a WHERE a.nickname=?";
    private final static String FINDBYTEAM = "SELECT a.id,a.brotherhood,a.capacity FROM paso AS a, esta AS b WHERE b.pasoId=a.id AND b.cuadrillaId=?";


    private Connection conn;

    // Constructor
    public PlayerDAO() {
        conn = ConnectionMariaDB.getConnection(); // Singleton para manejar la conexión
    }

    @Override
    public Player save(Player player) throws SQLException {
        if (player != null && UserSession.isLogged()) {
            System.out.println("Usuario autenticado: " + UserSession.getUser());  // Verificar si la sesión está activa
            Player existing = findById(player.getId());
            if (existing.getId() == 0) {
                // INSERT
                try (PreparedStatement pst = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, player.getNickname());
                    pst.setString(2, player.getGender());
                    pst.setInt(3, player.getAge());
                    pst.setString(4, UserSession.getUser().getUsername()); // Usuario autenticado
                    pst.executeUpdate();
                    ResultSet rs = pst.getGeneratedKeys();
                    if (rs.next()) {
                        player.setId(rs.getInt(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    player = null;
                }
            }
        }
        return player;
    }

    @Override
    public Player delete(Player player) throws SQLException {
        if (player != null && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(DELETE)) {
                pst.setString(1, player.getNickname()); // Nickname del jugador
                pst.setString(2, UserSession.getUser().getUsername()); // Usuario autenticado
                int affectedRows = pst.executeUpdate();
                if (affectedRows == 0) {
                    player = null; // No se eliminó ningún jugador
                }
            } catch (SQLException e) {
                e.printStackTrace();
                player = null; // En caso de error
            }
        } else {
            player = null; // Si el jugador es null o no hay sesión
        }
        return player;
    }

    @Override
    public Player findById(int key) {
        Player player = new Player();
        if (key != 0 && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(FINDBYID)) {
                pst.setInt(1, key);
                pst.setString(2, UserSession.getUser().getUsername()); // Usuario autenticado
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    player.setId(rs.getInt("id"));
                    player.setNickname(rs.getString("nickname"));
                    player.setGender(rs.getString("gender"));
                    player.setAge(rs.getInt("age"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return player;
    }


    @Override
    public List<Player> findAll() {
        List<Player> players = new ArrayList<>();
        if (UserSession.isLogged()) {
            String username = UserSession.getUser().getUsername();
            System.out.println("Buscando jugadores para el usuario: " + username);

            try (PreparedStatement pst = conn.prepareStatement(FINDALL)) {
                pst.setString(1, username);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Player player = new Player();
                    player.setId(rs.getInt("id"));
                    player.setNickname(rs.getString("nickname"));
                    player.setGender(rs.getString("gender"));
                    player.setAge(rs.getInt("age"));
                    players.add(player);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return players;
    }

    @Override
    public void update(Player player) {
        if (player != null && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
                pst.setString(1, player.getNickname());
                pst.setString(2, player.getGender());
                pst.setInt(3, player.getAge());
                pst.setInt(4, player.getId());
                pst.setString(5, UserSession.getUser().getUsername()); // Usuario autenticado
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Player> findByTeam(Team team) {
        return null;
    }

    @Override
    public Player findByNickname(String name) {
        Player result = new Player();
        if (name != null) {
            try (PreparedStatement pst = conn.prepareStatement(FINDBYNICKNAME)) {
                pst.setString(1, name);  // Establece el valor del nickname en la consulta
                pst.setString(2, UserSession.getUser().getUsername()); // Usuario autenticado
                ResultSet res = pst.executeQuery();
                if (res.next()) {
                    result.setId(res.getInt("id"));
                    result.setNickname(res.getString("nickname"));
                    result.setGender(res.getString("gender"));
                    result.setAge(res.getInt("age"));
                }
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static PlayerDAO build() {
        return new PlayerDAO();
    }
}
