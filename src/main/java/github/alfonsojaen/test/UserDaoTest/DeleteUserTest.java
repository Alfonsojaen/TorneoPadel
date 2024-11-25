package github.alfonsojaen.test.UserDaoTest;

import github.alfonsojaen.model.dao.UserDAO;
import github.alfonsojaen.model.entity.User;

import java.sql.SQLException;

public class DeleteUserTest {
public static void main(String[] args) {
    UserDAO userDAO = new UserDAO();

    try {
        System.out.println("Eliminando usuario...");
        User userToDelete = new User("testuser1", null, null, null);
        userDAO.delete(userToDelete);
        System.out.println("Usuario eliminado.");
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            userDAO.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
}
