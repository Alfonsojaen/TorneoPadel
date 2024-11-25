package github.alfonsojaen.test;

import github.alfonsojaen.model.singleton.UserSession;

public class Testsingleton {
    public static void main(String[] args) {
        System.out.println("Antes de login()");

        // Simula el login del usuario
        UserSession.login("admin@gmail.com", "admin");

        System.out.println("Después de login()");

        // Verifica si el usuario está autenticado
        if (UserSession.isLogged()) {
            System.out.println("Usuario autenticado: " + UserSession.getUser().getUsername());
        } else {
            System.out.println("No hay usuario autenticado.");
        }
    }
}
