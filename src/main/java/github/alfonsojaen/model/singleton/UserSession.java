package github.alfonsojaen.model.singleton;

import github.alfonsojaen.model.entity.User;

public class UserSession {
    private static User user=null;
    public static void login(String gmail, String username) {
        user = new User(username, "*******", gmail, "Desconocido");
    }
    public static void logout() {
        user = null;
    }
    public static boolean isLogged() {
        return user != null;
    }
    public static User getUser() {
        if (user == null) {
            throw new IllegalStateException("No user is logged in.");
        }
        return user;
    }
}

