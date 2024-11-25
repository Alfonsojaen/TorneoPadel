package github.alfonsojaen.test.PlayerDaoTest;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.singleton.UserSession;

import java.sql.SQLException;
import java.util.List;

public class TestListarPlayer {
    public static void main(String[] args) {
        UserSession.login("elmaki@gmail.com", "teje");

        if (UserSession.isLogged()) {
            System.out.println("Usuario autenticado: " + UserSession.getUser());

            PlayerDAO playerDAO = PlayerDAO.build();
            List<Player> players = playerDAO.findAll();

            if (players.isEmpty()) {
                System.out.println("No hay jugadores registrados para este usuario.");
            } else {
                System.out.println("Jugadores del usuario autenticado:");
                for (Player player : players) {
                    System.out.println(player);
                }
            }
        } else {
            System.out.println("No se ha iniciado sesión correctamente.");
        }
    }
}
