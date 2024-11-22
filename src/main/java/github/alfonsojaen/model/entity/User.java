package github.alfonsojaen.model.entity;

import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private String name;
    private List<Tournament> tournaments;

    public User(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;

    }

    public User() {
        this("", "", "", "");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public String toString() {
        String hiddenPassword = password.replaceAll(".", "*");
        return "Usuario -> " + name + '\'' +
                "Nombre del usuario : " + username + '\'' +
                "Contraseña : " + hiddenPassword + '\'' +
                "Email del usuario : " + email;
    }
}