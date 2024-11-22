package github.alfonsojaen.test;

import github.alfonsojaen.model.dao.UserDAO;
import github.alfonsojaen.utils.Utils;

import java.sql.SQLException;

public class LoginTest {
        public static void main(String[] args) {
            UserDAO userDAO = new UserDAO();

            try {
                System.out.println("Verificando login...");
                String encryptedPassword = Utils.encryptSHA256("password123");
                String username = userDAO.checkLogin("test1@example.com", encryptedPassword);
                if (username != null) {
                    System.out.println("Login exitoso para el usuario: " + username);
                } else {
                    System.out.println("Credenciales incorrectas.");
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
