package github.alfonsojaen.test.PlayerDaoTest;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.singleton.UserSession;

import java.io.IOException;
import java.sql.SQLException;

public class CrearyBuscarPlayerTest {
    public static void main(String[] args) {
        UserSession.login("elmaki@gmail.com", "teje");

        if (UserSession.isLogged()) {
            System.out.println("Usuario autenticado: " + UserSession.getUser());
        } else {
            System.out.println("No se ha podido autenticar el usuario.");
            return;
        }

        PlayerDAO playerDAO = PlayerDAO.build();
        Player newPlayer = new Player();
        newPlayer.setNickname("adios");
        newPlayer.setGender("hombre");
        newPlayer.setAge(10);

        try {
            Player savedPlayer = playerDAO.save(newPlayer);
            if (savedPlayer != null) {
                System.out.println("Jugador guardado: " + savedPlayer.getNickname());
            }

            Player foundPlayer = playerDAO.findById(savedPlayer.getId());
            System.out.println("Jugador encontrado por ID: " + (foundPlayer != null ? foundPlayer.getNickname() : "No encontrado"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                playerDAO.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
