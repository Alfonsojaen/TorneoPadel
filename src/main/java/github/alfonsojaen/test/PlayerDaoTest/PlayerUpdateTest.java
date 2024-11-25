package github.alfonsojaen.test.PlayerDaoTest;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.singleton.UserSession;

import java.sql.SQLException;

public class PlayerUpdateTest {

    public static void main(String[] args) {
        // Iniciar sesión con un usuario de prueba
        UserSession.login("elmaki@gmail.com", "teje");

        if (UserSession.isLogged()) {
            System.out.println("Usuario autenticado: " + UserSession.getUser());

            // Crear instancia de PlayerDAO
            PlayerDAO playerDAO = PlayerDAO.build();

            try {
                // Buscar un jugador por su ID o crear uno nuevo para actualizar
                Player player = playerDAO.findById(1); // Cambia el ID según tus datos
                if (player.getId() == 0) {
                    System.out.println("El jugador no existe, creando uno nuevo para pruebas.");
                    player.setNickname("nuevoJugador");
                    player.setGender("Male");
                    player.setAge(30);
                    player = playerDAO.save(player);
                }

                // Mostrar datos antes de actualizar
                System.out.println("Datos del jugador antes de actualizar: " + player);

                // Actualizar los datos del jugador
                player.setNickname("JugadorActualizado");
                player.setGender("Female");
                player.setAge(35);
                playerDAO.update(player);

                // Mostrar datos después de actualizar
                Player updatedPlayer = playerDAO.findById(player.getId());
                System.out.println("Datos del jugador después de actualizar: " + updatedPlayer);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se ha iniciado sesión correctamente.");
        }
    }
}
