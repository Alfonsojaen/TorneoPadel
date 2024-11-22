package github.alfonsojaen.model.interfaces;


import github.alfonsojaen.model.entity.User;

import java.sql.SQLException;

public interface InterfaceUserDAO<T> extends DAO<T>{
    User findByUserName(String username) throws SQLException;
    String checkLogin(String email, String password) throws SQLException;

}
