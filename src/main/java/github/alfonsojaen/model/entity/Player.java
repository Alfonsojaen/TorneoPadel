package github.alfonsojaen.model.entity;


import java.util.List;
import java.util.Objects;

public class Player {
    private int id;
    private String nickname;
    private String gender;
    private int age;
    private User user;
    private boolean selected;
    private List<Team> teams;

    public Player(int id, String nickname, String gender, int age, User user, List<Team> teams) {
        this.id = id;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.user = user;
        this.selected = false;
        this.teams = teams;
    }

    public Player() {

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Player player = (Player) object;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", user=" + user +
                ", teams=" + teams +
                '}';
    }
}

