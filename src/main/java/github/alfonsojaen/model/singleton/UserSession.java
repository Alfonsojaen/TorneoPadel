package github.alfonsojaen.model.singleton;

import github.alfonsojaen.model.entity.User;

public class UserSession {
    private static User user=null;
    public static void login(String gmail,String usuario) {
        user = new User(null,usuario,gmail,null);
    }
    public static void logout() {
        user = null;
    }
    public static boolean isLogged() {
        return user==null?false:true;
    }
    public static User getUser() {
        return user;
    }
}

