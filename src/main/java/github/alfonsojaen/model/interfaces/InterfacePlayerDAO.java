package github.alfonsojaen.model.interfaces;

import github.alfonsojaen.model.entity.Team;

import java.util.List;

public interface InterfacePlayerDAO <T> extends DAO<T> {
    void update(T entity);
    List<T> findByTeam(Team team);
    T findByNickname(String name);

}
