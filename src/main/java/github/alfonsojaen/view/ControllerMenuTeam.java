package github.alfonsojaen.view;

import github.alfonsojaen.model.dao.TeamDAO;
import github.alfonsojaen.model.entity.Player;
import github.alfonsojaen.model.entity.Team;
import github.alfonsojaen.model.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ControllerMenuTeam implements Initializable {
    @FXML
    private TableView<Team> tableview;

    @FXML
    private TableColumn<Team, String> id;
    @FXML
    private TableColumn<Team, String> name;

    @FXML
    private TableColumn<Team, String> coach;

    @FXML
    private TableColumn<Team, String> description;

    private ObservableList<Team> teams;


    @FXML
    private void switchToMenu() throws IOException {
        Scenes.setRoot("pantallaMenu",null,null);
    }


    @FXML
    private void switchToInsertTeam() throws IOException {
        Scenes.setRoot("pantallaInsertTeam",null,null);
    }


    @FXML
    private void switchToDeleteTeam() throws IOException {
        Scenes.setRoot("pantallaDeleteTeam",null,null);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            if (UserSession.isLogged()) {
                List<Team> teams = TeamDAO.build().findAll();
                this.teams = FXCollections.observableArrayList(teams);

                tableview.setItems(this.teams);
                tableview.setEditable(true);

                tableview.setRowFactory(tv -> {
                    TableRow<Team> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 3 && (!row.isEmpty())) {
                            Team rowTeam = row.getItem();
                            try {
                                Scenes.setRoot("pantallaAssignPlayer", rowTeam,null);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    return row;
                });

                TableColumn<Team, Void> actionColumn = new TableColumn<>("Integrantes");

                actionColumn.setCellFactory(col -> {
                    TableCell<Team, Void> cell = new TableCell<Team, Void>() {
                        private final Button btn = new Button("JUGADORES");
                        {
                            btn.setOnAction((ActionEvent event) -> {
                                Team rowTeam = getTableRow().getItem();
                                if (rowTeam != null) {
                                    showPlayers(rowTeam);
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!empty) {
                                setGraphic(btn);
                            } else {
                                setGraphic(null);
                            }
                        }
                    };
                    return cell;
                });

                tableview.getColumns().add(actionColumn);

                id.setCellValueFactory(team -> new SimpleStringProperty(String.valueOf(team.getValue().getId())));
                name.setCellValueFactory(team -> new SimpleStringProperty(team.getValue().getName()));
                coach.setCellValueFactory(team -> new SimpleStringProperty(team.getValue().getCoach()));
                description.setCellValueFactory(team -> new SimpleStringProperty(team.getValue().getDescription()));

                name.setCellFactory(TextFieldTableCell.forTableColumn());
                name.setOnEditCommit(event -> {
                    String newName = event.getNewValue();
                    String oldName = event.getOldValue();
                    if (newName.equals(oldName)) {
                        return;
                    }
                    if (newName.length() <= 30) {
                        boolean exists = TeamDAO.build().findAll().stream()
                                .anyMatch(team -> team.getName().equals(newName));
                        if (exists) {
                            Utils.ShowAlert("El Nombre ya está en uso. Elige otro.");
                        } else {
                            Team team = event.getRowValue();
                            team.setName(newName);
                            TeamDAO.build().update(team);
                        }
                    } else {
                        Utils.ShowAlert("Te has pasado del límite de caracteres!");
                    }
                });

                // Hacer editable el coach del equipo
                coach.setCellFactory(TextFieldTableCell.forTableColumn());
                coach.setOnEditCommit(event -> {
                    if (event.getNewValue().equals(event.getOldValue())) {
                        return;
                    }
                    if (event.getNewValue().length() <= 20) {
                        Team team = event.getRowValue();
                        team.setCoach(event.getNewValue());
                        TeamDAO.build().update(team);
                    } else {
                        Utils.ShowAlert("Te has pasado del límite de caracteres!");
                    }
                });

                description.setCellFactory(TextFieldTableCell.forTableColumn());
                description.setOnEditCommit(event -> {
                    if (event.getNewValue().equals(event.getOldValue())) {
                        return;
                    }
                    if (event.getNewValue().length() <= 60) {
                        Team team = event.getRowValue();
                        team.setDescription(event.getNewValue());
                        TeamDAO.build().update(team);
                    } else {
                        Utils.ShowAlert("Te has pasado del límite de caracteres!");
                    }
                });
            }
        }

    private void showPlayers(Team team) {
        int teamId = team.getId();
        List<Player> players = TeamDAO.build().getPlayersByTeam(teamId);

        if (players.isEmpty()) {
            Utils.ShowAlert("No hay jugadores asignados a este equipo.");
        } else {
            String playerNames = players.stream()
                    .map(Player::getNickname)
                    .collect(Collectors.joining(", "));
            Utils.ShowAlert("Jugadores asignados a " + team.getName() + ":  ( " + playerNames +" )");
        }
    }

}

