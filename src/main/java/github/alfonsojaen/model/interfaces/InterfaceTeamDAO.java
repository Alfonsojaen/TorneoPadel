package github.alfonsojaen.model.interfaces;

import github.alfonsojaen.model.entity.Tournament;

import java.sql.SQLException;
import java.util.List;

public interface InterfaceTeamDAO <T> extends DAO<T> {
    void setPlayer(T entity) throws SQLException;
    void update(T entity);
    T findByName(String name);
    List<T> findByTournament(Tournament tournament);

}

