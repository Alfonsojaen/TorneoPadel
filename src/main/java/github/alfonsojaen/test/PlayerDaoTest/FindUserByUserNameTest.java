package github.alfonsojaen.test.PlayerDaoTest;

import github.alfonsojaen.model.dao.UserDAO;
import github.alfonsojaen.model.entity.User;

import java.sql.SQLException;

public class FindUserByUserNameTest{
        public static void main(String[] args) {
    UserDAO userDAO = new UserDAO();

    try {
        // 1. Crear un nuevo usuario para probar la búsqueda
        System.out.println("Creando un nuevo usuario para probar...");
        User newUser = new User("testuser1", "password123", "test1@example.com", "Test User 1");
        userDAO.save(newUser);  // Guardamos el usuario

        // 2. Buscar al usuario por nombre de usuario
        System.out.println("Buscando usuario por nombre de usuario...");
        User foundUser = userDAO.findByUserName("testuser1");

        // 3. Verificar si el usuario fue encontrado
        if (foundUser != null) {
            System.out.println("Usuario encontrado: " + foundUser);
        } else {
            System.out.println("Usuario no encontrado.");
        }

        // 4. Intentar buscar un usuario que no existe
        System.out.println("Buscando un usuario que no existe...");
        User notFoundUser = userDAO.findByUserName("nonexistentuser");
        if (notFoundUser == null) {
            System.out.println("El usuario no existe.");
        }

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
