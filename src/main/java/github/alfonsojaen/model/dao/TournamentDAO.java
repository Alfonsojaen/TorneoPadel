package github.alfonsojaen.model.dao;

import github.alfonsojaen.model.connection.ConnectionMariaDB;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.Tournament;
import github.alfonsojaen.model.interfaces.InterfaceTournamentDAO;
import github.alfonsojaen.model.singleton.UserSession;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TournamentDAO implements InterfaceTournamentDAO<Tournament> {

    private final static String INSERT = "INSERT INTO Tournament (name, start_date, end_date, location, prize,user_username) VALUES (?, ?, ?, ?, ?, ?);";
    private final static String UPDATE = "UPDATE Tournament SET name = ?, start_date = ?, end_date = ?, location = ?, prize = ? WHERE id = ? AND user_username = ?";
    private final static String FINDALL = "SELECT id, name, start_date, end_date, location, prize, user_username FROM Tournament WHERE user_username = ?";
    private static final String FINDBYID = "SELECT id, name, start_date, end_date, location, prize, user_username FROM Tournament WHERE id = ? AND user_username = ?";
    private final static String DELETE = "DELETE FROM Tournament WHERE name = ? AND user_username = ?";
    private final static String FINDBYNAME = "SELECT id, name, start_date, end_date, location, prize, user_username FROM Tournament WHERE name = ? AND user_username = ?";
    private final static String INSERTTEAM = "INSERT INTO Pertenece (teamId, tournamentId) VALUES (?,?)";
    private final static String DELETETEAM = "DELETE FROM Pertenece WHERE tournamentId=?";
    private final static String GETTEAM = "SELECT teamId FROM Pertenece WHERE tournamentId = ?";

    private Connection conn;

    // Constructor
    public TournamentDAO() {
        conn = ConnectionMariaDB.getConnection(); // Singleton para manejar la conexión
    }

    @Override
    public Tournament save(Tournament tournament) throws SQLException {
        if (tournament != null && UserSession.isLogged()) {
            Tournament existing = findById(tournament.getId());
            if (existing.getId() == 0) {
                // INSERT
                try (PreparedStatement pst = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, tournament.getName());
                    pst.setDate(2, tournament.getStartDate());
                    pst.setDate(3, tournament.getEndDate());
                    pst.setString(4, tournament.getLocation());
                    pst.setString(5, tournament.getPrize());
                    pst.setString(8, UserSession.getUser().getUsername());
                    pst.executeUpdate();
                    ResultSet rs = pst.getGeneratedKeys();
                    if (rs.next()) {
                        tournament.setId(rs.getInt(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    tournament = null;
                }
            }
        }
        return tournament;
    }


    @Override
    public Tournament delete(Tournament tournament) throws SQLException {
        if (tournament != null && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(DELETE)) {
                pst.setString(1, tournament.getName());
                pst.setString(2, UserSession.getUser().getUsername());
                int affectedRows = pst.executeUpdate();
                if (affectedRows == 0) {
                    tournament = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                tournament = null;
            }
        } else {
            tournament = null;
        }
        return tournament;
    }

    @Override
    public Tournament findById(int key) {
        Tournament tournament = new Tournament();
        if (key != 0 && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(FINDBYID)) {
                pst.setInt(1, key);
                pst.setString(2, UserSession.getUser().getUsername());
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    tournament.setId(rs.getInt("id"));
                    tournament.setName(rs.getString("name"));
                    tournament.setStartDate(rs.getDate("start_date"));
                    tournament.setEndDate(rs.getDate("end_date"));
                    tournament.setLocation(rs.getString("location"));
                    tournament.setPrize(rs.getString("prize"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tournament;
    }

    @Override
    public List<Tournament> findAll() {
        List<Tournament> tournaments = new ArrayList<>();
        if (UserSession.isLogged()) {
            String username = UserSession.getUser().getUsername();
            try (PreparedStatement pst = conn.prepareStatement(FINDALL)) {
                pst.setString(1, username);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Tournament tournament = new Tournament();
                    tournament.setId(rs.getInt("id"));
                    tournament.setName(rs.getString("name"));
                    tournament.setStartDate(rs.getDate("start_date"));
                    tournament.setEndDate(rs.getDate("end_date"));
                    tournament.setLocation(rs.getString("location"));
                    tournament.setPrize(rs.getString("prize"));
                    tournaments.add(tournament);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tournaments;
    }

    @Override
    public void setTeam(Tournament tournament) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(DELETETEAM)) {
            pst.setInt(1, tournament.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
        }
        for(Team team : tournament.getTeams()){
            try (PreparedStatement pst = conn.prepareStatement(INSERTTEAM)) {
                pst.setInt(1, team.getId());
                pst.setInt(2, tournament.getId());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Tournament tournament) {
        if (tournament != null && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
                pst.setString(1, tournament.getName());
                pst.setDate(2, tournament.getStartDate());
                pst.setDate(3, tournament.getEndDate());
                pst.setString(4, tournament.getLocation());
                pst.setString(5, tournament.getPrize());
                pst.setInt(6, tournament.getId());
                pst.setString(7, UserSession.getUser().getUsername());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Tournament findByName(String name) {
        Tournament result = new Tournament();
        if (name != null && UserSession.isLogged()) {
            try (PreparedStatement pst = conn.prepareStatement(FINDBYNAME)) {
                pst.setString(1, name);
                pst.setString(2, UserSession.getUser().getUsername());
                ResultSet res = pst.executeQuery();
                if (res.next()) {
                    result.setId(res.getInt("id"));
                    result.setName(res.getString("name"));
                    result.setStartDate(res.getDate("start_date"));
                    result.setEndDate(res.getDate("end_date"));
                    result.setLocation(res.getString("location"));
                    result.setPrize(res.getString("prize"));
                }
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public List<Team> getTeamsByTournament(int tournamentId) {
        List<Team> teams = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(GETTEAM)) {
            pst.setInt(1, tournamentId);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                int teamId = res.getInt("teamId");
                Team team = TeamDAO.build().findById(teamId);

                if (team != null) {
                    teams.add(team);
                }
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    public static TournamentDAO build(){
        return new TournamentDAO();
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
}

