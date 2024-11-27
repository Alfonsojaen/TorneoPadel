package github.alfonsojaen.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tournament {
    private int id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String location;
    private String prize;
    private User user;

    public Tournament(int id, String name, Date startDate, Date endDate, String location, String prize, User user) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.prize = prize;
        this.user = user;
    }

    public Tournament() {
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

    public java.sql.Date getStartDate() {
        return (java.sql.Date) startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return (java.sql.Date) endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private List<Team> teams = new ArrayList<>();
    public List<Team> getTeams() {
        return teams;
    }
    public void setTeams(List<Team> teams) {
        if (teams == null) {
            this.teams = new ArrayList<>();
        } else {
            this.teams = teams;
        }
    }

    public void addTeam(Team team){
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void removeTeam(Team team){
            teams.remove(team);
    }

    @Override
    public String toString() {
        return "Torneo: " + name + ", Fecha inicio: " + startDate + ", Fecha fin: " + endDate + ", Ubicación: " + location;
    }
}