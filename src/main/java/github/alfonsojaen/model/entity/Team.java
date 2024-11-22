package github.alfonsojaen.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {
    private int id;
    private String name;
    private String coach;
    private String description;
    private User user;
    private List<Player> players;
    private List<Tournament> tournaments;

    public Team(int id, String name, String coach, String description, List<Player> players, List<Tournament> tournaments) {
        this.id = id;
        this.name = name;
        this.coach = coach;
        this.description = description;
        this.user = user;
        this.players = players;
        this.tournaments = tournaments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }
    /**
     * Adds a player to the list if it's not already present.
     * @param player The paso to be added.
     */
    public void addPlayer(Player player){
        if(players==null){
            players = new ArrayList<>();
        }
        if(!players.contains(player)){
            players.add(player);
        }
    }
    /**
     * Removes a player from the list if it exists.
     * @param player The paso to be removed.
     */
    public void removeTeam(Player player){
        if(players!=null){
            players.remove(player);
        }
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Team team = (Team) object;
        return id == team.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coach='" + coach + '\'' +
                ", description='" + description + '\'' +
                ", players=" + players +
                ", tournaments=" + tournaments +
                '}';
    }
}
