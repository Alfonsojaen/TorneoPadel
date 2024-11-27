package github.alfonsojaen.view;


import github.alfonsojaen.model.dao.TeamDAO;
import github.alfonsojaen.model.dao.TournamentDAO;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.entity.Tournament;
import github.alfonsojaen.utils.Utils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ControllerAssignTeam {
    private Tournament tournament;
    private List<Team> selected = new ArrayList<>();
    private ObservableList<Team> teams;
    private List<Team> Selected = new ArrayList<>(selected);

    @FXML
        private TableView<Team> tableview;

    @FXML
    private TableColumn<Team, String> idColumn;

    @FXML
    private TableColumn<Team, String> nameColumn;

    @FXML
    private TableColumn<Team, String> coachColumn;

    @FXML
    private  TableColumn<Team, String> descriptionColumn;

    @FXML
    private TableColumn<Team, Boolean> selectionColumn;


    @FXML
    private void switchToMenuTournament() throws IOException {
        Scenes.setRoot("pantallaMenuTournament",null,null);
    }


    public void start () {
        idColumn.setCellValueFactory(team -> new SimpleStringProperty(String.valueOf(team.getValue().getId())));

        nameColumn.setCellValueFactory(team -> new SimpleStringProperty(String.valueOf(team.getValue().getName())));

        coachColumn.setCellValueFactory(team -> new SimpleStringProperty(String.valueOf(team.getValue().getCoach())));

        descriptionColumn.setCellValueFactory(team -> new SimpleStringProperty(String.valueOf(team.getValue().getDescription())));

        selectionColumn.setCellFactory(CheckBoxTableCell.forTableColumn(
                (Integer i) ->{
                    SimpleBooleanProperty selectedProperty= new SimpleBooleanProperty(teams.get(i).isSelected());
                    selectedProperty.addListener((obs, oldValue, newValue) -> {
                        Team team = teams.get(i);
                        team.setSelected(newValue);
                        if(newValue){
                            this.tournament.addTeam(team);
                        }else{
                            this.tournament.removeTeam(team);
                        }
                    });
                    return selectedProperty;
                } ));


        selectionColumn.setEditable(true);
        this.teams = FXCollections.observableArrayList(TeamDAO.build().findAll());

        TeamDAO tdao = new TeamDAO();
        this.selected=tdao.findByTournament(tournament);
        for(Team team : this.teams){
            if(this.selected.contains(team)){
                team.setSelected(true);
            }else{
                team.setSelected(false);
            }
        }

        tableview.setItems(this.teams);
    }


    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
        start();
    }

    public void assing() {
        try {
            List<Team> teamsSelected = new ArrayList<>();
            for (Team team : teams) {
                if (team.isSelected()) {
                    teamsSelected.add(team);
                }
            }

            for (Team team : Selected) {
                if (!teamsSelected.contains(team)) {
                    tournament.removeTeam(team);
                }
            }

            for (Team team : teamsSelected) {
                if (!Selected.contains(team)) {
                    tournament.addTeam(team);
                }
            }

            TournamentDAO.build().setTeam(this.tournament);
            Utils.ShowAlert("Se ha asignado con éxito");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
