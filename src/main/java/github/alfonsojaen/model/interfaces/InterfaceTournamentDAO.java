package github.alfonsojaen.model.interfaces;

import github.alfonsojaen.model.entity.Team;

import java.sql.SQLException;
import java.util.List;

public interface InterfaceTournamentDAO<T> extends DAO<T> {
    void setTeam(T entity) throws SQLException;
    void update(T entity);
    T findByName(String name);
    List<Team> getTeamsByTournament(int tournamentId);
    

}

