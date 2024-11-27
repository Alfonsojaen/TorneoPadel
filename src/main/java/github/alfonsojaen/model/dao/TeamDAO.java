package github.alfonsojaen.model.dao;

import github.alfonsojaen.model.connection.ConnectionMariaDB;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.Tournament;
import github.alfonsojaen.model.interfaces.InterfaceTeamDAO;
import github.alfonsojaen.model.singleton.UserSession;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO implements InterfaceTeamDAO<Team> {

    private final static String INSERT = "INSERT INTO Team (name, coach, description, user_username) VALUES (?,?,?,?)";
    private final static String UPDATE = "UPDATE Team SET name=?, coach=?, description=? WHERE id=? AND user_username = ?";
    private final static String FINDALL = "SELECT id, name, coach, description, user_username FROM Team  WHERE user_username = ?";
    private static final String FINDBYID = "SELECT id, name, coach, description, user_username FROM Team WHERE id = ? AND user_username = ?";
    private final static String DELETE= "DELETE FROM Team  WHERE name=? AND user_username = ?";
    private final static String FINDBYNAME = "SELECT id, name, coach, description, user_username FROM Team WHERE name=? AND user_username = ?";
    private final static String FINDBYTOURNAMENT = "SELECT t.id, t.name, t.coach, t.description FROM Team t JOIN Pertenece p ON p.teamId = t.id WHERE p.tournamentId = ?";
    private final static String INSERTPLAYER = "INSERT INTO Esta (playerId, teamId) VALUES (?,?)";
    private final static String DELETEPLAYER = "DELETE FROM Esta WHERE teamId=?";
    private final static String GETPLAYER = "SELECT playerId FROM Esta WHERE teamId = ?";
    private Connection conn;

    // Constructor
    public TeamDAO() {
        conn = ConnectionMariaDB.getConnection(); // Singleton para manejar la conexión
    }

    @Override
    public Team save(Team team) throws SQLException {
        if (team != null && UserSession.isLogged()) {
            Team existing = findById(team.getId());
            if (existing.getId() == 0) {
                // INSERT
                try (PreparedStatement pst = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, team.getName());
                    pst.setString(2, team.getCoach());
                    pst.setString(3, team.getDescription());
                    pst.setString(4, UserSession.getUser().getUsername());
                    pst.executeUpdate();
                    ResultSet rs = pst.getGeneratedKeys();
                    if (rs.next()) {
                        team.setId(rs.getInt(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    team = null;
                }
            }
        }
        return team;
    }

    @Override
    public Team delete(Team team) throws SQLException {
        if (team != null && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(DELETE)) {
                pst.setString(1, team.getName());
                pst.setString(2, UserSession.getUser().getUsername());
                int affectedRows = pst.executeUpdate();
                if (affectedRows == 0) {
                    team = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                team = null;
            }
        } else {
            team = null;
        }
        return team;
    }


    @Override
    public Team findById(int key) {
        Team team = new Team();
        if (key != 0 && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(FINDBYID)) {
                pst.setInt(1, key);
                pst.setString(2, UserSession.getUser().getUsername());
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    team.setId(rs.getInt("id"));
                    team.setName(rs.getString("name"));
                    team.setCoach(rs.getString("coach"));
                    team.setDescription(rs.getString("description"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return team;
    }


    @Override
    public List<Team> findAll() {
        List<Team> teams = new ArrayList<>();
        if (UserSession.isLogged()) {
            String username = UserSession.getUser().getUsername();
            try (PreparedStatement pst = conn.prepareStatement(FINDALL)) {
                pst.setString(1, username);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setName(rs.getString("name"));
                    team.setCoach(rs.getString("coach"));
                    team.setDescription(rs.getString("description"));
                    teams.add(team);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return teams;
    }


    @Override
    public void setPlayer(Team team) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(DELETEPLAYER)) {
            pst.setInt(1, team.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
        }
        for(Player player : team.getPlayers()){
            try (PreparedStatement pst = conn.prepareStatement(INSERTPLAYER)) {
                pst.setInt(1, player.getId());
                pst.setInt(2, team.getId());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Team team) {
        if (team != null && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
                pst.setString(1, team.getName());
                pst.setString(2, team.getCoach());
                pst.setString(3, team.getDescription());
                pst.setInt(4, team.getId());
                pst.setString(5, UserSession.getUser().getUsername());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Team findByName(String name) {
        Team result = new Team();
        if (name != null && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(FINDBYNAME)) {
                pst.setString(1, name);
                pst.setString(2, UserSession.getUser().getUsername());
                ResultSet res = pst.executeQuery();
                if (res.next()) {
                    result.setId(res.getInt("id"));
                    result.setName(res.getString("name"));
                    result.setCoach(res.getString("coach"));
                    result.setDescription(res.getString("description"));
                }
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    @Override
    public List<Player> getPlayersByTeam(int teamId) {
        List<Player> players = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(GETPLAYER)) {
            pst.setInt(1, teamId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                int playerId = res.getInt("playerId");
                Player player = PlayerDAO.build().findById(playerId);

                if (player != null) {
                    players.add(player);
                }
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }


    @Override
    public List<Team> findByTournament(Tournament tu) {
        List<Team> result = new ArrayList<>();
        try(PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FINDBYTOURNAMENT)) {
            pst.setInt(1, tu.getId());
            ResultSet res = pst.executeQuery();
            while (res.next()){
                Team c = new Team();
                c.setId(res.getInt(1));
                c.setName(res.getString("name"));
                c.setCoach(res.getString("coach"));
                c.setDescription(res.getString("description"));
                result.add(c);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public static TeamDAO build(){
        return new TeamDAO();
    }
    class TeamLazy extends Team {
        @Override
        public List<Player> getPlayer() {
            if (super.getPlayer() == null) {
                setPlayer(PlayerDAO.build().findByTeam(this));
            }
            return super.getPlayer();
        }

    }
}
