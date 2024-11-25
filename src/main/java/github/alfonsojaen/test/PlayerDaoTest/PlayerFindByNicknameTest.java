package github.alfonsojaen.test.PlayerDaoTest;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.singleton.UserSession;

import java.sql.SQLException;

public class PlayerFindByNicknameTest {
    public static void main(String[] args) {
        // Iniciar sesión con un usuario de prueba
        UserSession.login("elmaki@gmail.com", "teje");

        if (UserSession.isLogged()) {
            System.out.println("Usuario autenticado: " + UserSession.getUser());

            // Crear instancia de PlayerDAO
            PlayerDAO playerDAO = PlayerDAO.build();

            // Nombre del jugador a buscar
            String nicknameToFind = "JugadorActualizado";

            // Buscar jugador por nickname
            Player foundPlayer = playerDAO.findByNickname(nicknameToFind);

            // Verificar resultado
            if (foundPlayer.getId() != 0) {
                System.out.println("Jugador encontrado: " + foundPlayer);
            } else {
                System.out.println("No se encontró ningún jugador con el nickname: " + nicknameToFind);
            }

        } else {
            System.out.println("No se ha iniciado sesión correctamente.");
        }
    }
}
