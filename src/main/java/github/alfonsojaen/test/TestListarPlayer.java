package github.alfonsojaen.test;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.singleton.UserSession;

import java.sql.SQLException;
import java.util.List;

public class TestListarPlayer {
    public static void main(String[] args) {
        // Iniciar sesión con un usuario de prueba (asegúrate de que este usuario esté registrado)
        UserSession.login("elmaki@gmail.com", "teje");

        // Verificar si el usuario ha iniciado sesión correctamente
        if (UserSession.isLogged()) {
            System.out.println("Usuario autenticado: " + UserSession.getUser());

            // Obtener todos los jugadores asociados al usuario autenticado
            PlayerDAO playerDAO = PlayerDAO.build();
            List<Player> players = playerDAO.findAll();

            // Verificar si la lista de jugadores no está vacía
            if (players.isEmpty()) {
                System.out.println("No hay jugadores registrados para este usuario.");
            } else {
                // Mostrar los jugadores encontrados
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
