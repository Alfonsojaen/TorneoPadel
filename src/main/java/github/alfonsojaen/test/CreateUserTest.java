package github.alfonsojaen.test;

import github.alfonsojaen.model.dao.UserDAO;
import github.alfonsojaen.model.entity.User;
import github.alfonsojaen.utils.Utils;

import java.sql.SQLException;

public class CreateUserTest {
    public static void main(String[] args) {
                UserDAO userDAO = new UserDAO();

                try {
                    System.out.println("Creando un nuevo usuario...");
                    String encryptedPassword = Utils.encryptSHA256("password123");
                    User newUser = new User("teje", encryptedPassword, "elmaki@gmail.com", "Test User 1");
                    userDAO.save(newUser);
                    System.out.println("Usuario creado: " + newUser);
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

