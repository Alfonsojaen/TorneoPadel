package github.alfonsojaen.test.PlayerDaoTest;

import github.alfonsojaen.model.dao.PlayerDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.singleton.UserSession;

import java.sql.SQLException;
import java.util.Scanner;

public class DeletePlayerTest {
    public static void main(String[] args) {
        Scanner
                scanner = new Scanner(System.in);

        // Paso 1: Iniciar sesión con un usuario de prueba
        System.out.println("Paso 1: Iniciando sesión...");
        UserSession.login("elmaki@gmail.com", "teje");

        if (UserSession.isLogged()) {
            System.out.println("Usuario autenticado: " + UserSession.getUser());
            pause(scanner);

            // Instancia del DAO
            PlayerDAO playerDAO = PlayerDAO.build();
            String playerNickname = "JugadorPrueba";

            try {
                // Paso 2: Buscar jugador por nickname
                System.out.println("Paso 2: Buscar jugador por nickname...");
                Player playerToDelete = playerDAO.findByNickname(playerNickname);
                pause(scanner);

                // Paso 3: Crear jugador si no existe
                if (playerToDelete.getId() == 0) {
                    System.out.println("Jugador no encontrado, procediendo a crearlo...");
                    playerToDelete.setNickname(playerNickname);
                    playerToDelete.setGender("Male");
                    playerToDelete.setAge(25);

                    playerToDelete = playerDAO.save(playerToDelete);
                    System.out.println("Jugador creado: " + playerToDelete);
                } else {
                    System.out.println("Jugador encontrado: " + playerToDelete);
                }
                pause(scanner);

                // Paso 4: Confirmar que el jugador existe en la base de datos
                System.out.println("Paso 4: Verificar jugador en la base de datos...");
                Player existingPlayer = playerDAO.findByNickname(playerNickname);
                if (existingPlayer.getId() != 0) {
                    System.out.println("Jugador encontrado en la base de datos: " + existingPlayer);
                } else {
                    System.out.println("Error: No se encontró al jugador recién creado.");
                }
                pause(scanner);

                // Paso 5: Eliminar jugador por nickname
                System.out.println("Paso 5: Eliminar jugador por nickname...");
                Player deletedPlayer = playerDAO.delete(existingPlayer);

                if (deletedPlayer == null) {
                    System.out.println("Jugador eliminado correctamente: " + playerNickname);
                } else {
                    System.out.println("Error: No se pudo eliminar al jugador.");
                }
                pause(scanner);

                // Paso 6: Confirmar eliminación
                System.out.println("Paso 6: Confirmar eliminación...");
                Player verifyDeletion = playerDAO.findByNickname(playerNickname);
                if (verifyDeletion.getId() == 0) {
                    System.out.println("Confirmación: El jugador ya no existe en la base de datos.");
                } else {
                    System.out.println("Error: El jugador aún existe: " + verifyDeletion);
                }
                pause(scanner);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se ha iniciado sesión correctamente.");
        }

        // Cerrar el scanner
        scanner.close();
    }

    /**
     * Método para pausar el programa hasta que el usuario presione *Enter*
     */
    private static void pause(Scanner scanner) {
        System.out.println("\nPresiona Enter para continuar...");
        scanner.nextLine();
    }
}